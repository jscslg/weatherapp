package com.jscode.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jscode.weatherapp.R
import com.jscode.weatherapp.adapters.WeatherAdapter
import com.jscode.weatherapp.databinding.FragmentWeatherBinding
import com.jscode.weatherapp.util.Resource
import kotlinx.coroutines.flow.collect

class WeatherFragment : Fragment() {
    private val args: WeatherFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weather_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_delete -> {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Are you sure you want to delete this city ?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteCity(args.city.name)
                    view?.findNavController()?.navigateUp()
                }
                .show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_weather,container,false)
        requireActivity().title = "Weather"
        binding.city = args.city
        binding.weatherList.layoutManager = LinearLayoutManager(requireContext())
        adapter = WeatherAdapter()
        binding.weatherList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getWeatherData(args.city).collect {
                when (it) {
                    is Resource.Success -> {
                        adapter.submitList(it.data)
                    }
                    is Resource.Error -> { }
                    is Resource.Loading -> { }
                }
            }
        }
    }
}