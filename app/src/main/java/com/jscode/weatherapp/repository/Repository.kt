package com.jscode.weatherapp.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.jscode.weatherapp.models.CityData
import com.jscode.weatherapp.models.WeatherData
import com.jscode.weatherapp.repository.db.WeatherDao
import com.jscode.weatherapp.repository.rest.RestApi
import com.jscode.weatherapp.util.Resource
import com.jscode.weatherapp.util.convertUtcToDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class Repository(private val weatherDao: WeatherDao) {
    @WorkerThread
    suspend fun fetchCity(name: String) = RestApi.retrofitService.getCityData(name)
    @WorkerThread
    suspend fun addCity(city: CityData) = weatherDao.addCity(city)
    @WorkerThread
    suspend fun getCities() = weatherDao.getCities()
    @WorkerThread
    suspend fun deleteCity(name: String) = weatherDao.deleteCity(name)
    @WorkerThread
    suspend fun getWeatherData(city: CityData) = flow{
        val data = weatherDao.getWeatherData(city.name).first()
        try {
            val current = System.currentTimeMillis()
            var shouldFetch = false
            for(i in 0..4){
                val date = current+ TimeUnit.DAYS.toMillis(i.toLong())
                var found = false
                data.forEach {
                    if(convertUtcToDate(it.date)== convertUtcToDate(date)) found=true
                }
                if(!found){
                    shouldFetch=true
                    break
                }
            }
            val flow = if(shouldFetch) {
                emit(Resource.Loading(data))
                val fetch = RestApi.retrofitService.getWeatherData(city.lat, city.lon)
                val list = mutableListOf<WeatherData>()
                fetch.weatherList.forEach {
                    list.add(
                        WeatherData(
                            "${city.name}_${it.date*1000}",
                            city.name,
                            it.date*1000,
                            it.weather[0].header,
                            it.weather[0].desc,
                            it.weather[0].icon,
                            it.temp.min,
                            it.temp.max
                        )
                    )
                }
                weatherDao.addWeatherData(list)
                weatherDao.getWeatherData(city.name).map { Resource.Success(it) }
            } else weatherDao.getWeatherData(city.name).map { Resource.Success(it) }
            emitAll(flow)
        } catch (e: Exception) {
            emit(Resource.Error(e,data))
        }
    }

    suspend fun deleteOldWeatherData(yesterday: Long) = weatherDao.deleteOldWeatherData(yesterday)
}