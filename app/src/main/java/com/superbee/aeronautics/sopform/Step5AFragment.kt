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

class Step5AFragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()

    private lateinit var etTargetDisease: EditText
    private lateinit var etFormulationType: EditText
    private lateinit var etPesticideName: EditText
    private lateinit var etConcentration: EditText
    private lateinit var etDosage: EditText
    private lateinit var etWaterVolume: EditText
    private lateinit var btnNext: Button
    private lateinit var btnBack: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step5a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTargetDisease = view.findViewById(R.id.et_target_disease)
        etFormulationType = view.findViewById(R.id.et_formulation_type)
        etPesticideName = view.findViewById(R.id.et_pesticide_name)
        etConcentration = view.findViewById(R.id.et_concentration)
        etDosage = view.findViewById(R.id.et_dosage)
        etWaterVolume = view.findViewById(R.id.et_water_volume)
        btnNext = view.findViewById(R.id.btn_next)
        btnBack = view.findViewById(R.id.btn_back)

        // Restore saved data if any
        viewModel.targetDisease.value?.let { etTargetDisease.setText(it) }
        viewModel.formulationType.value?.let { etFormulationType.setText(it) }
        viewModel.pesticideName.value?.let { etPesticideName.setText(it) }
        viewModel.concentration.value?.let { etConcentration.setText(it) }
        viewModel.dosage.value?.let { etDosage.setText(it) }
        viewModel.waterVolume.value?.let { etWaterVolume.setText(it) }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnNext.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_step5A_to_step5B)
        }
    }

    private fun saveData() {
        viewModel.targetDisease.value = etTargetDisease.text.toString()
        viewModel.formulationType.value = etFormulationType.text.toString()
        viewModel.pesticideName.value = etPesticideName.text.toString()
        viewModel.concentration.value = etConcentration.text.toString()
        viewModel.dosage.value = etDosage.text.toString()
        viewModel.waterVolume.value = etWaterVolume.text.toString()
    }
}
