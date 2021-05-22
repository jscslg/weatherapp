package com.jscode.weatherapp.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jscode.weatherapp.models.CityData
import com.jscode.weatherapp.models.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("Select * from cities_table")
    suspend fun getCities(): List<CityData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCity(city: CityData)

    @Query("Delete from cities_table where name = :name")
    suspend fun deleteCity(name: String)

    @Query("Select * from weather_table where city_name = :cityName order by date asc")
    fun getWeatherData(cityName: String): Flow<List<WeatherData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeatherData(weatherDataList: List<WeatherData>)

    @Query("Delete from weather_table where date < :yesterday")
    suspend fun deleteOldWeatherData(yesterday: Long)
}