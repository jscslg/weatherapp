package com.jscode.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jscode.weatherapp.databinding.WeatherItemBinding
import com.jscode.weatherapp.models.WeatherData

class WeatherAdapter: ListAdapter<WeatherData, WeatherAdapter.ViewHolder>(WeatherAdapterDiff) {
    class ViewHolder(private val binding: WeatherItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WeatherData) {
            binding.item = item
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WeatherItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
    companion object{
        val WeatherAdapterDiff = object : DiffUtil.ItemCallback<WeatherData>(){
            override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}