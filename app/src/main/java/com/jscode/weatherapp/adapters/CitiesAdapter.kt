package com.jscode.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jscode.weatherapp.databinding.CityListItemBinding
import com.jscode.weatherapp.models.CityData

class CitiesAdapter(private val onClickHandler: CityOnClick): ListAdapter<CityData,CitiesAdapter.ViewHolder>(CitiesAdapterDiff) {
    class ViewHolder(private val binding: CityListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CityData, onClickHandler: CityOnClick) {
            binding.item = item
            binding.onClickHandler = onClickHandler
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CityListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object {
        private val CitiesAdapterDiff = object : DiffUtil.ItemCallback<CityData>() {
            override fun areItemsTheSame(oldItem: CityData, newItem: CityData): Boolean {
                return oldItem===newItem
            }

            override fun areContentsTheSame(oldItem: CityData, newItem: CityData): Boolean {
                return oldItem.name==newItem.name
            }
        }
        class CityOnClick(private val clickListener: (data: CityData) -> Unit) {
            fun onClickCity(data: CityData) = clickListener(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,onClickHandler)
    }
}