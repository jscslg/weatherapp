package com.jscode.weatherapp.models

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "cities_table")
data class CityData(
    @PrimaryKey val name: String,
    val lat: Double,
    val lon: Double
): Parcelable

@Entity(tableName = "weather_table")
data class WeatherData(
    @PrimaryKey val id:String,
    @ColumnInfo(name = "city_name") val cityName: String,
    val date: Long,
    val main:String,
    val desc: String,
    val icon: String,
    @ColumnInfo(name = "min_temp") val minTemp: Double,
    @ColumnInfo(name = "max_temp") val maxTemp: Double
)
