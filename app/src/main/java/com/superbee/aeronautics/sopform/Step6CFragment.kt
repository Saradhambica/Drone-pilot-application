package com.superbee.aeronautics.sopform

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.superbee.aeronautics.R


class Step6CFragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()

    private lateinit var etFieldCapacityTheoretical: EditText
    private lateinit var etFieldCapacityActual: EditText
    private lateinit var etVMD: EditText
    private lateinit var etNMD: EditText
    private lateinit var etDropletDensity: EditText
    private lateinit var etSprayUniformity: EditText
    private lateinit var etControlEfficiency: EditText
    private lateinit var etPhytoToxicity: EditText
    private lateinit var etEfficacy: EditText
    private lateinit var etOtherObservations: EditText

    private lateinit var btnSubmit: Button
    private lateinit var btnBack: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_step6c, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize fields
        etFieldCapacityTheoretical = view.findViewById(R.id.et_field_capacity_theoretical)
        etFieldCapacityActual = view.findViewById(R.id.et_field_capacity_actual)
        etVMD = view.findViewById(R.id.et_vmd)
        etNMD = view.findViewById(R.id.et_nmd)
        etDropletDensity = view.findViewById(R.id.et_droplet_density)
        etSprayUniformity = view.findViewById(R.id.et_spray_uniformity)
        etControlEfficiency = view.findViewById(R.id.et_control_efficiency)
        etPhytoToxicity = view.findViewById(R.id.et_phyto_toxicity)
        etEfficacy = view.findViewById(R.id.et_efficacy)
        etOtherObservations = view.findViewById(R.id.et_other_observations)

        btnBack = view.findViewById(R.id.btn_back)
        btnSubmit = view.findViewById(R.id.btn_submit)

        // Restore previous data if any
        restoreDataFromViewModel()

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }



        btnSubmit.setOnClickListener {
            btnSubmit.isEnabled = false
            saveStep6CData()
            Toast.makeText(requireContext(), "Submit clicked. Preparing data...", Toast.LENGTH_SHORT).show()
            submitAllDataToFirebase()
        }
    }

    private fun saveStep6CData() {
        viewModel.fieldCapacityTheoretical.value = etFieldCapacityTheoretical.text.toString()
        viewModel.fieldCapacityActual.value = etFieldCapacityActual.text.toString()
        viewModel.vmd.value = etVMD.text.toString()
        viewModel.nmd.value = etNMD.text.toString()
        viewModel.dropletDensity.value = etDropletDensity.text.toString()
        viewModel.sprayUniformity.value = etSprayUniformity.text.toString()
        viewModel.controlEfficiency.value = etControlEfficiency.text.toString()
        viewModel.phytoToxicity.value = etPhytoToxicity.text.toString()
        viewModel.efficacy.value = etEfficacy.text.toString()
        viewModel.otherObservations.value = etOtherObservations.text.toString()
        Log.d("Step6C", "Step6C data saved to ViewModel")
    }

    private fun restoreDataFromViewModel() {
        viewModel.fieldCapacityTheoretical.value?.let { etFieldCapacityTheoretical.setText(it) }
        viewModel.fieldCapacityActual.value?.let { etFieldCapacityActual.setText(it) }
        viewModel.vmd.value?.let { etVMD.setText(it) }
        viewModel.nmd.value?.let { etNMD.setText(it) }
        viewModel.dropletDensity.value?.let { etDropletDensity.setText(it) }
        viewModel.sprayUniformity.value?.let { etSprayUniformity.setText(it) }
        viewModel.controlEfficiency.value?.let { etControlEfficiency.setText(it) }
        viewModel.phytoToxicity.value?.let { etPhytoToxicity.setText(it) }
        viewModel.efficacy.value?.let { etEfficacy.setText(it) }
        viewModel.otherObservations.value?.let { etOtherObservations.setText(it) }
    }

    private fun submitAllDataToFirebase() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            btnSubmit.isEnabled = true
            return
        }

        val userId = currentUser.uid
        val timestamp = System.currentTimeMillis()

        val sopData = hashMapOf(
            "userId" to userId,
            "timestamp" to timestamp,
            "submittedAt" to com.google.firebase.Timestamp.now(),

            // All steps data from ViewModel
            "location" to (viewModel.location.value ?: ""),
            "latLong" to (viewModel.latLong.value ?: ""),
            "altitude" to (viewModel.altitude.value ?: ""),
            "surrounding" to (viewModel.surrounding.value ?: ""),
            "cropName" to (viewModel.cropName.value ?: ""),
            "variety" to (viewModel.variety.value ?: ""),
            "type" to (viewModel.type.value ?: ""),
            "spacing" to (viewModel.spacing.value ?: ""),
            "sowingDate" to (viewModel.dateSowing.value ?: ""),
            "cropStage" to (viewModel.cropStage.value ?: ""),
            "shadedArea" to (viewModel.shadedArea.value ?: ""),

            "classification" to (viewModel.classification.value ?: ""),
            "category" to (viewModel.category.value ?: ""),
            "maxWeight" to (viewModel.maxWeight.value ?: ""),
            "powerSource" to (viewModel.powerSource.value ?: ""),
            "otherSpecs" to (viewModel.otherSpecs.value ?: ""),
            "photoUri" to (viewModel.photoUri.value ?: ""),

            "tankCapacity" to (viewModel.tankCapacity.value ?: ""),
            "nozzleMounting" to (viewModel.nozzleMounting.value ?: ""),
            "boomLength" to (viewModel.boomLength.value ?: ""),
            "nozzleCount" to (viewModel.nozzleCount.value ?: ""),
            "nozzleType" to (viewModel.nozzleType.value ?: ""),
            "coneAngle" to (viewModel.coneAngle.value ?: ""),
            "dropletSize" to (viewModel.dropletSize.value ?: ""),
            "flowRate" to (viewModel.flowRate.value ?: ""),
            "operatingPressure" to (viewModel.operatingPressure.value ?: ""),

            "targetDisease" to (viewModel.targetDisease.value ?: ""),
            "formulationType" to (viewModel.formulationType.value ?: ""),
            "pesticideName" to (viewModel.pesticideName.value ?: ""),
            "concentration" to (viewModel.concentration.value ?: ""),
            "dosage" to (viewModel.dosage.value ?: ""),
            "waterVolume" to (viewModel.waterVolume.value ?: ""),

            "targetDeficiency2" to (viewModel.targetDeficiency2.value ?: ""),
            "formulationType2" to (viewModel.formulationType2.value ?: ""),
            "cropNutrientName2" to (viewModel.cropNutrientName2.value ?: ""),
            "nutrientType2" to (viewModel.nutrientType2.value ?: ""),
            "concentration2" to (viewModel.concentration2.value ?: ""),
            "dosage2" to (viewModel.dosage2.value ?: ""),
            "waterVolume2" to (viewModel.waterVolume2.value ?: ""),

            "temperature" to (viewModel.temperature.value ?: ""),
            "relativeHumidity" to (viewModel.relativeHumidity.value ?: ""),
            "wind" to (viewModel.wind.value ?: ""),
            "flightModes" to (viewModel.flightModes.value ?: ""),
            "heightCanopy" to (viewModel.heightCanopy.value ?: ""),
            "swath" to (viewModel.swath.value ?: ""),
            "overlap" to (viewModel.overlap.value ?: ""),
            "sprayWidth" to (viewModel.sprayWidth.value ?: ""),
            "flightDirection" to (viewModel.flightDirection.value ?: ""),
            "timeOfSpray" to (viewModel.timeOfSpray.value ?: ""),
            "multipleFlight" to (viewModel.multipleFlight.value ?: ""),

            "fieldCapacityTheoretical" to (viewModel.fieldCapacityTheoretical.value ?: ""),
            "fieldCapacityActual" to (viewModel.fieldCapacityActual.value ?: ""),
            "vmd" to (viewModel.vmd.value ?: ""),
            "nmd" to (viewModel.nmd.value ?: ""),
            "dropletDensity" to (viewModel.dropletDensity.value ?: ""),
            "sprayUniformity" to (viewModel.sprayUniformity.value ?: ""),
            "controlEfficiency" to (viewModel.controlEfficiency.value ?: ""),
            "phytoToxicity" to (viewModel.phytoToxicity.value ?: ""),
            "efficacy" to (viewModel.efficacy.value ?: ""),
            "otherObservations" to (viewModel.otherObservations.value ?: "")
        )

        Log.d("Step6C", "Submitting SOP data: $sopData")

        FirebaseFirestore.getInstance().collection("sop_submissions")
            .add(sopData)
            .addOnSuccessListener { documentRef ->
                viewModel.submissionId.value = documentRef.id
                Toast.makeText(requireContext(), "SOP submitted successfully!", Toast.LENGTH_SHORT).show()
                Log.d("Step6C", "Document added successfully: ${documentRef.id}")

                // Navigate to SummaryFragment
                findNavController().navigate(R.id.action_step6C_to_SummaryFragment)

                btnSubmit.isEnabled = true
            }
            .addOnFailureListener { e ->
                Log.e("Step6C", "Submission failed", e)
                Toast.makeText(requireContext(), "Submission failed: ${e.message}", Toast.LENGTH_LONG).show()
                btnSubmit.isEnabled = true
            }
    }
}
