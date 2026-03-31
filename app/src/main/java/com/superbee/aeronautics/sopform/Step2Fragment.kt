package com.superbee.aeronautics.sopform

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.superbee.aeronautics.R
import java.util.Calendar

class Step2Fragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()

    private lateinit var etCropName: AutoCompleteTextView
    private lateinit var etVariety: EditText
    private lateinit var etType: EditText
    private lateinit var etSpacing: EditText
    private lateinit var etDateSowing: EditText
    private lateinit var etStageOfCrop: EditText
    private lateinit var etShadedArea: EditText
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        etCropName = view.findViewById(R.id.et_crop_name)
        etVariety = view.findViewById(R.id.et_variety)
        etType = view.findViewById(R.id.et_type)
        etSpacing = view.findViewById(R.id.et_spacing)
        etDateSowing = view.findViewById(R.id.et_date_sowing)
        etStageOfCrop = view.findViewById(R.id.et_stage_of_crop)
        etShadedArea = view.findViewById(R.id.et_shaded_area)
        btnBack = view.findViewById(R.id.btn_back)
        btnNext = view.findViewById(R.id.btn_next)

        // Load saved data if any
        viewModel.cropName.value?.let { etCropName.setText(it) }
        viewModel.variety.value?.let { etVariety.setText(it) }
        viewModel.type.value?.let { etType.setText(it) }
        viewModel.spacing.value?.let { etSpacing.setText(it) }
        viewModel.dateSowing.value?.let { etDateSowing.setText(it) }
        viewModel.cropStage.value?.let { etStageOfCrop.setText(it) }
        viewModel.shadedArea.value?.let { etShadedArea.setText(it) }

        // ✅ Crop Name AutoComplete setup
        val cropNames = listOf(
            "Rice", "Cotton", "Maize", "Groundnut", "Pigeon Pea", "Safflower",
            "Sesame", "Soybean", "Sugarcane", "Wheat", "Barley", "Millet",
            "Rye", "Oats", "Sunflower", "Mustard", "Chickpea", "Lentil",
            "Cowpea", "Mung Bean", "Black Gram", "Tomato", "Potato", "Brinjal",
            "Cabbage", "Cauliflower", "Onion", "Garlic", "Chili", "Okra"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cropNames)
        etCropName.setAdapter(adapter)

        // Show all options immediately when tapped or focused & enable filtering while typing
        etCropName.threshold = 0
        etCropName.setOnClickListener { etCropName.showDropDown() }
        etCropName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) etCropName.showDropDown()
        }

        // DatePicker for Date of Sowing
        etDateSowing.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val monthDisplay = selectedMonth + 1 // Month is 0-based
                    val dateString = "$selectedDay/$monthDisplay/$selectedYear"
                    etDateSowing.setText(dateString)
                }, year, month, day
            )
            datePicker.show()
        }

        // Navigation buttons
        btnBack.setOnClickListener { findNavController().navigateUp() }
        btnNext.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_step2_to_step3)
        }
    }

    private fun saveData() {
        viewModel.cropName.value = etCropName.text.toString()
        viewModel.variety.value = etVariety.text.toString()
        viewModel.type.value = etType.text.toString()
        viewModel.spacing.value = etSpacing.text.toString()
        viewModel.dateSowing.value = etDateSowing.text.toString()
        viewModel.cropStage.value = etStageOfCrop.text.toString()
        viewModel.shadedArea.value = etShadedArea.text.toString()
    }
}
