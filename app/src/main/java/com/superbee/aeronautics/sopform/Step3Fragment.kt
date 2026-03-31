package com.superbee.aeronautics.sopform

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.superbee.aeronautics.R

class Step3Fragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()

    private lateinit var etClassification: EditText
    private lateinit var etCategory: EditText
    private lateinit var etMaxWeight: EditText
    private lateinit var etPowerSource: EditText
    private lateinit var etOtherSpecs: EditText
    private lateinit var btnChoosePhoto: Button
    private lateinit var ivDronePhoto: ImageView
    private lateinit var btnNext: Button
    private lateinit var btnBack: Button

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etClassification = view.findViewById(R.id.et_classification)
        etCategory = view.findViewById(R.id.et_category)
        etMaxWeight = view.findViewById(R.id.et_max_weight)
        etPowerSource = view.findViewById(R.id.et_power_source)
        etOtherSpecs = view.findViewById(R.id.et_other_specs)
        btnChoosePhoto = view.findViewById(R.id.btn_choose_photo)
        ivDronePhoto = view.findViewById(R.id.iv_drone_photo)
        btnNext = view.findViewById(R.id.btn_next)
        btnBack = view.findViewById(R.id.btn_back)

        // Restore saved data if available
        viewModel.classification.value?.let { etClassification.setText(it) }
        viewModel.category.value?.let { etCategory.setText(it) }
        viewModel.maxWeight.value?.let { etMaxWeight.setText(it) }
        viewModel.powerSource.value?.let { etPowerSource.setText(it) }
        viewModel.otherSpecs.value?.let { etOtherSpecs.setText(it) }

        // Restore image if it exists
        viewModel.photoUri.value?.let {
            ivDronePhoto.setImageURI(Uri.parse(it))
        }

        btnChoosePhoto.setOnClickListener {
            openFileChooser()
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnNext.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_step3_to_step4)
        }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data?.data != null) {
            imageUri = data.data
            ivDronePhoto.setImageURI(imageUri)
            viewModel.photoUri.value = imageUri.toString()
        }
    }

    private fun saveData() {
        viewModel.classification.value = etClassification.text.toString()
        viewModel.category.value = etCategory.text.toString()
        viewModel.maxWeight.value = etMaxWeight.text.toString()
        viewModel.powerSource.value = etPowerSource.text.toString()
        viewModel.otherSpecs.value = etOtherSpecs.text.toString()
    }
}
