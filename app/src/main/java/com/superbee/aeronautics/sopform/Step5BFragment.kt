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

class Step5BFragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()

    private lateinit var etTargetDeficiency: EditText
    private lateinit var etFormulationType: EditText
    private lateinit var etCropNutrientName: EditText
    private lateinit var etNutrientType: EditText
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
        return inflater.inflate(R.layout.fragment_step5b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind views
        etTargetDeficiency = view.findViewById(R.id.et_target_deficiency2)
        etFormulationType = view.findViewById(R.id.et_formulation_type2)
        etCropNutrientName = view.findViewById(R.id.et_crop_nutrient_name2)
        etNutrientType = view.findViewById(R.id.et_nutrient_type2)
        etConcentration = view.findViewById(R.id.et_concentration2)
        etDosage = view.findViewById(R.id.et_dosage2)
        etWaterVolume = view.findViewById(R.id.et_water_volume2)
        btnNext = view.findViewById(R.id.btn_next)
        btnBack = view.findViewById(R.id.btn_back)


        // Restore saved data if any
        viewModel.targetDeficiency2.value?.let { etTargetDeficiency.setText(it) }
        viewModel.formulationType2.value?.let { etFormulationType.setText(it) }
        viewModel.cropNutrientName2.value?.let { etCropNutrientName.setText(it) }
        viewModel.nutrientType2.value?.let { etNutrientType.setText(it) }
        viewModel.concentration2.value?.let { etConcentration.setText(it) }
        viewModel.dosage2.value?.let { etDosage.setText(it) }
        viewModel.waterVolume2.value?.let { etWaterVolume.setText(it) }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnNext.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_step5B_to_step6A)
        }
    }

    private fun saveData() {
        viewModel.targetDeficiency2.value = etTargetDeficiency.text.toString()
        viewModel.formulationType2.value = etFormulationType.text.toString()
        viewModel.cropNutrientName2.value = etCropNutrientName.text.toString()
        viewModel.nutrientType2.value = etNutrientType.text.toString()
        viewModel.concentration2.value = etConcentration.text.toString()
        viewModel.dosage2.value = etDosage.text.toString()
        viewModel.waterVolume2.value = etWaterVolume.text.toString()
    }
}
