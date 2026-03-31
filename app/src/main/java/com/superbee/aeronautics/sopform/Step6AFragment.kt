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

class Step6AFragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()

    private lateinit var etTemperature: EditText
    private lateinit var etRelativeHumidity: EditText
    private lateinit var etWind: EditText
    private lateinit var btnNext: Button
    private lateinit var btnBack: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step6a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTemperature = view.findViewById(R.id.et_temperature)
        etRelativeHumidity = view.findViewById(R.id.et_relative_humidity)
        etWind = view.findViewById(R.id.et_wind)
        btnNext = view.findViewById(R.id.btn_next)
        btnBack = view.findViewById(R.id.btn_back)

        // Restore saved data if any
        viewModel.temperature.value?.let { etTemperature.setText(it) }
        viewModel.relativeHumidity.value?.let { etRelativeHumidity.setText(it) }
        viewModel.wind.value?.let { etWind.setText(it) }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnNext.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_step6A_to_step6B)
        }
    }

    private fun saveData() {
        viewModel.temperature.value = etTemperature.text.toString()
        viewModel.relativeHumidity.value = etRelativeHumidity.text.toString()
        viewModel.wind.value = etWind.text.toString()
    }
}
