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
import com.superbee.aeronautics.sopform.SopViewModel

class Step4Fragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()

    private lateinit var etTankCapacity: EditText
    private lateinit var etNozzleMounting: EditText
    private lateinit var etBoomLength: EditText
    private lateinit var etNozzleCount: EditText
    private lateinit var etNozzleType: EditText
    private lateinit var etConeAngle: EditText
    private lateinit var etDropletSize: EditText
    private lateinit var etFlowRate: EditText
    private lateinit var etOperatingPressure: EditText

    private lateinit var btnBack: Button
    private lateinit var btnNext: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTankCapacity = view.findViewById(R.id.et_tank_capacity)
        etNozzleMounting = view.findViewById(R.id.et_nozzle_mounting)
        etBoomLength = view.findViewById(R.id.et_boom_length)
        etNozzleCount = view.findViewById(R.id.et_nozzle_count)
        etNozzleType = view.findViewById(R.id.et_nozzle_type)
        etConeAngle = view.findViewById(R.id.et_cone_angle)
        etDropletSize = view.findViewById(R.id.et_droplet_size)
        etFlowRate = view.findViewById(R.id.et_flow_rate)
        etOperatingPressure = view.findViewById(R.id.et_operating_pressure)

        btnBack = view.findViewById(R.id.btn_back)
        btnNext = view.findViewById(R.id.btn_next)

        // Load saved data if any
        viewModel.tankCapacity.value?.let { etTankCapacity.setText(it) }
        viewModel.nozzleMounting.value?.let { etNozzleMounting.setText(it) }
        viewModel.boomLength.value?.let { etBoomLength.setText(it) }
        viewModel.nozzleCount.value?.let { etNozzleCount.setText(it) }
        viewModel.nozzleType.value?.let { etNozzleType.setText(it) }
        viewModel.coneAngle.value?.let { etConeAngle.setText(it) }
        viewModel.dropletSize.value?.let { etDropletSize.setText(it) }
        viewModel.flowRate.value?.let { etFlowRate.setText(it) }
        viewModel.operatingPressure.value?.let { etOperatingPressure.setText(it) }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnNext.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_step4_to_step5)
        }
    }

    private fun saveData() {
        viewModel.tankCapacity.value = etTankCapacity.text.toString()
        viewModel.nozzleMounting.value = etNozzleMounting.text.toString()
        viewModel.boomLength.value = etBoomLength.text.toString()
        viewModel.nozzleCount.value = etNozzleCount.text.toString()
        viewModel.nozzleType.value = etNozzleType.text.toString()
        viewModel.coneAngle.value = etConeAngle.text.toString()
        viewModel.dropletSize.value = etDropletSize.text.toString()
        viewModel.flowRate.value = etFlowRate.text.toString()
        viewModel.operatingPressure.value = etOperatingPressure.text.toString()
    }
}
