package com.jscode.weatherapp.models

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class RestWeatherData(
    @SerializedName("daily") val weatherList: List<RestWeatherItem>
)

data class RestWeatherItem(
    @SerializedName("dt") val date:Long,
    val temp: RestTemperature,
    val weather: List<RestWeather>
)

data class RestWeather (
    @SerializedName("main") val header: String,
    @SerializedName("description") val desc: String,
    val icon: String
)

data class RestTemperature(
    val min: Double,
    val max: Double
)