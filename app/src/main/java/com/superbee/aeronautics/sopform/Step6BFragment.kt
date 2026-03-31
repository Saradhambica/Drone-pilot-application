package com.superbee.aeronautics.sopform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.superbee.aeronautics.R

class Step6BFragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()

    private lateinit var etFlightModes: EditText
    private lateinit var etHeightCanopy: EditText
    private lateinit var etSwath: EditText
    private lateinit var etOverlap: EditText
    private lateinit var etSprayWidth: EditText
    private lateinit var etFlightDirection: EditText
    private lateinit var etTimeOfSpray: EditText
    private lateinit var etMultipleFlight: EditText

    private lateinit var btnNext: Button
    private lateinit var btnBack: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step6b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etFlightModes = view.findViewById(R.id.et_flight_modes)
        etHeightCanopy = view.findViewById(R.id.et_height_canopy)
        etSwath = view.findViewById(R.id.et_swath)
        etOverlap = view.findViewById(R.id.et_overlap)
        etSprayWidth = view.findViewById(R.id.et_spray_width)
        etFlightDirection = view.findViewById(R.id.et_flight_direction)
        etTimeOfSpray = view.findViewById(R.id.et_time_of_spray)
        etMultipleFlight = view.findViewById(R.id.et_multiple_flight)

        btnBack = view.findViewById(R.id.btn_back)
        btnNext = view.findViewById(R.id.btn_next)

        // Restore saved data if any
        viewModel.flightModes.value?.let { etFlightModes.setText(it) }
        viewModel.heightCanopy.value?.let { etHeightCanopy.setText(it) }
        viewModel.swath.value?.let { etSwath.setText(it) }
        viewModel.overlap.value?.let { etOverlap.setText(it) }
        viewModel.sprayWidth.value?.let { etSprayWidth.setText(it) }
        viewModel.flightDirection.value?.let { etFlightDirection.setText(it) }
        viewModel.timeOfSpray.value?.let { etTimeOfSpray.setText(it) }
        viewModel.multipleFlight.value?.let { etMultipleFlight.setText(it) }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnNext.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_step6B_to_step6C)
        }
    }

    private fun saveData() {
        viewModel.flightModes.value = etFlightModes.text.toString()
        viewModel.heightCanopy.value = etHeightCanopy.text.toString()
        viewModel.swath.value = etSwath.text.toString()
        viewModel.overlap.value = etOverlap.text.toString()
        viewModel.sprayWidth.value = etSprayWidth.text.toString()
        viewModel.flightDirection.value = etFlightDirection.text.toString()
        viewModel.timeOfSpray.value = etTimeOfSpray.text.toString()
        viewModel.multipleFlight.value = etMultipleFlight.text.toString()
    }
}
