package com.jscode.weatherapp.repository.rest

import com.google.gson.GsonBuilder
import com.jscode.weatherapp.models.CityData
import com.jscode.weatherapp.models.RestWeatherData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//ADD AN API KEY HERE FROM WEATHER API
private const val APP_ID = "paste_here"
private const val BASE_URL = "https://api.openweathermap.org"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface RestApiService {
    @GET("/data/2.5/onecall")
    suspend fun getWeatherData(
        @Query(value = "lat") lat: Double,
        @Query(value = "lon") lon: Double,
        @Query(value = "exclude") exclude: String = "current,minutely,hourly,alerts",
        @Query(value = "units") units: String = "metric",
        @Query(value = "appid") appid:String = APP_ID
    ): RestWeatherData

    @GET("/geo/1.0/direct")
    suspend fun getCityData(
        @Query(value = "q") name: String,
        @Query(value = "appid") appid:String = APP_ID
    ): List<CityData>
}

object RestApi {
    val retrofitService : RestApiService by lazy {
        retrofit.create(RestApiService::class.java)
    }
}