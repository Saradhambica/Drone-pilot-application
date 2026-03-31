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

class Step1Fragment : Fragment() {

    private val viewModel: SopViewModel by activityViewModels()

    private lateinit var etLocation: EditText
    private lateinit var etLatLong: EditText
    private lateinit var etAltitude: EditText
    private lateinit var etSurrounding: EditText
    private lateinit var btnNext: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etLocation = view.findViewById(R.id.et_location)
        etLatLong = view.findViewById(R.id.et_lat_long)
        etAltitude = view.findViewById(R.id.et_altitude)
        etSurrounding = view.findViewById(R.id.et_surrounding)
        btnNext = view.findViewById(R.id.btn_next)

        // Load saved data if any
        viewModel.location.value?.let { etLocation.setText(it) }
        viewModel.latLong.value?.let { etLatLong.setText(it) }
        viewModel.altitude.value?.let { etAltitude.setText(it) }
        viewModel.surrounding.value?.let { etSurrounding.setText(it) }

        btnNext.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_step1_to_step2)
        }
    }

    private fun saveData() {
        viewModel.location.value = etLocation.text.toString()
        viewModel.latLong.value = etLatLong.text.toString()
        viewModel.altitude.value = etAltitude.text.toString()
        viewModel.surrounding.value = etSurrounding.text.toString()
    }
}