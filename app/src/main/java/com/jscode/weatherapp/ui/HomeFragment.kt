package com.jscode.weatherapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jscode.weatherapp.R
import com.jscode.weatherapp.adapters.CitiesAdapter
import com.jscode.weatherapp.databinding.FragmentHomeBinding
import com.jscode.weatherapp.util.Resource

class HomeFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_addFragment)
            true
        }
        R.id.action_contact -> {
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_contactFragment)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        requireActivity().title = "Weather App"
        binding.citiesList.layoutManager = LinearLayoutManager(requireContext())
        adapter = CitiesAdapter(CitiesAdapter.Companion.CityOnClick {
            view?.findNavController()
                ?.navigate(HomeFragmentDirections.actionHomeFragmentToWeatherFragment(it))
        })
        binding.citiesList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCities().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> adapter.submitList(it.data)
                is Resource.Loading -> { }
                is Resource.Error -> { }
            }
        }
    }
}