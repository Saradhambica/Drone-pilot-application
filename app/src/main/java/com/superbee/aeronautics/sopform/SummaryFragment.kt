package com.superbee.aeronautics.sopform

import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
//import com.superbee.aeronautics.MainActivity
import com.superbee.aeronautics.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.net.Uri
import com.superbee.aeronautics.DroneDashboardActivity



class SummaryFragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()
    private lateinit var successLayout: CardView
    private lateinit var btnLaunchQgc: MaterialButton
    private lateinit var btnDashboard: MaterialButton
    private lateinit var btnDownloadPdf: MaterialButton
    private lateinit var tvSummary: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI
        successLayout = view.findViewById(R.id.success_layout)
        btnLaunchQgc = view.findViewById(R.id.btnLaunchQgc)
        btnDashboard = view.findViewById(R.id.btn_dashboard)
        btnDownloadPdf = view.findViewById(R.id.btn_download_pdf)
        tvSummary = view.findViewById(R.id.tv_summary)

        // ✅ Display success banner for 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            successLayout.visibility = View.GONE
        }, 3000)

        // ✅ Display full recap from ViewModel
        val summaryText = buildString {
            appendLine("📍 Location: ${viewModel.location.value}")
            appendLine("🌐 Lat/Long: ${viewModel.latLong.value}")
            appendLine("⛰️ Altitude: ${viewModel.altitude.value}")
            appendLine("🏞️ Surrounding: ${viewModel.surrounding.value}")
            appendLine()
            appendLine("🌾 Crop: ${viewModel.cropName.value}")
            appendLine("🌱 Variety: ${viewModel.variety.value}")
            appendLine("🧪 Type: ${viewModel.type.value}")
            appendLine("📏 Spacing: ${viewModel.spacing.value}")
            appendLine("📅 Sowing Date: ${viewModel.dateSowing.value}")
            appendLine("🌿 Crop Stage: ${viewModel.cropStage.value}")
            appendLine("🌳 Shaded Area: ${viewModel.shadedArea.value}")
            appendLine()
            appendLine("🚁 Drone Classification: ${viewModel.classification.value}")
            appendLine("🛸 Category: ${viewModel.category.value}")
            appendLine("⚖️ Max Weight: ${viewModel.maxWeight.value}")
            appendLine("🔋 Power Source: ${viewModel.powerSource.value}")
            appendLine("🔧 Other Specs: ${viewModel.otherSpecs.value}")
            appendLine()
            appendLine("💧 Tank Capacity: ${viewModel.tankCapacity.value}")
            appendLine("🔩 Nozzle Mounting: ${viewModel.nozzleMounting.value}")
            appendLine("📏 Boom Length: ${viewModel.boomLength.value}")
            appendLine("🌊 Nozzle Count: ${viewModel.nozzleCount.value}")
            appendLine("🌸 Nozzle Type: ${viewModel.nozzleType.value}")
            appendLine("🔺 Cone Angle: ${viewModel.coneAngle.value}")
            appendLine("💦 Droplet Size: ${viewModel.dropletSize.value}")
            appendLine("🌊 Flow Rate: ${viewModel.flowRate.value}")
            appendLine("⚡ Operating Pressure: ${viewModel.operatingPressure.value}")
            appendLine()
            appendLine("🧪 Target Disease: ${viewModel.targetDisease.value}")
            appendLine("🧴 Formulation Type: ${viewModel.formulationType.value}")
            appendLine("💊 Pesticide Name: ${viewModel.pesticideName.value}")
            appendLine("🔬 Concentration: ${viewModel.concentration.value}")
            appendLine("💧 Dosage: ${viewModel.dosage.value}")
            appendLine("💦 Water Volume: ${viewModel.waterVolume.value}")
            appendLine()
            appendLine("🌾 Target Deficiency: ${viewModel.targetDeficiency2.value}")
            appendLine("🧴 Formulation Type 2: ${viewModel.formulationType2.value}")
            appendLine("💊 Nutrient Name: ${viewModel.cropNutrientName2.value}")
            appendLine("🧪 Nutrient Type: ${viewModel.nutrientType2.value}")
            appendLine("🔬 Concentration 2: ${viewModel.concentration2.value}")
            appendLine("💧 Dosage 2: ${viewModel.dosage2.value}")
            appendLine("💦 Water Volume 2: ${viewModel.waterVolume2.value}")
            appendLine()
            appendLine("🌡️ Temperature: ${viewModel.temperature.value}")
            appendLine("💧 Relative Humidity: ${viewModel.relativeHumidity.value}")
            appendLine("🌬️ Wind: ${viewModel.wind.value}")
            appendLine("🛫 Flight Modes: ${viewModel.flightModes.value}")
            appendLine("🌾 Height Canopy: ${viewModel.heightCanopy.value}")
            appendLine("➡️ Swath: ${viewModel.swath.value}")
            appendLine("↔️ Overlap: ${viewModel.overlap.value}")
            appendLine("📏 Spray Width: ${viewModel.sprayWidth.value}")
            appendLine("🧭 Flight Direction: ${viewModel.flightDirection.value}")
            appendLine("⏰ Time of Spray: ${viewModel.timeOfSpray.value}")
            appendLine("🔁 Multiple Flight: ${viewModel.multipleFlight.value}")
            appendLine()
            appendLine("⚖️ Field Capacity (Theoretical): ${viewModel.fieldCapacityTheoretical.value}")
            appendLine("⚖️ Field Capacity (Actual): ${viewModel.fieldCapacityActual.value}")
            appendLine("💧 VMD: ${viewModel.vmd.value}")
            appendLine("💧 NMD: ${viewModel.nmd.value}")
            appendLine("💦 Droplet Density: ${viewModel.dropletDensity.value}")
            appendLine("📊 Spray Uniformity: ${viewModel.sprayUniformity.value}")
            appendLine("✅ Control Efficiency: ${viewModel.controlEfficiency.value}")
            appendLine("🌱 Phytotoxicity: ${viewModel.phytoToxicity.value}")
            appendLine("🌟 Efficacy: ${viewModel.efficacy.value}")
            appendLine("📝 Other Observations: ${viewModel.otherObservations.value}")
        }.trimIndent()

        tvSummary.text = summaryText

        // ✅ Launch GCS Dashboard
        btnLaunchQgc.setOnClickListener {
            val intent = Intent(requireActivity(), DroneDashboardActivity::class.java)
            startActivity(intent)
        }

        // ✅ Dashboard button
       /* btnDashboard.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        } */

        // ✅ Download PDF
        btnDownloadPdf.setOnClickListener {
            generatePdf(summaryText)
        }
    }

    private fun generatePdf(summaryText: String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = android.graphics.Paint()
        paint.textSize = 12f
        var yPosition = 50f

        summaryText.split("\n").forEach { line ->
            canvas.drawText(line, 40f, yPosition, paint)
            yPosition += 20f
        }

        pdfDocument.finishPage(page)

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, "SOP_Submission.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(requireContext(), "📄 PDF saved to Downloads!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Error saving PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }
}
