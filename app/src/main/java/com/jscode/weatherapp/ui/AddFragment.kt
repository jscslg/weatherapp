package com.jscode.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.jscode.weatherapp.R
import com.jscode.weatherapp.databinding.FragmentAddBinding

class AddFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_add,container,false)
        requireActivity().title = "Add"
        binding.addButton.setOnClickListener {
            viewModel.addCity(binding.cityText.text.toString().trim())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigateBack.observe(viewLifecycleOwner) {
            if(it) {
                view.findNavController().navigateUp()
                viewModel.navigatedBack()
            }
        }
    }
}