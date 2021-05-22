package com.jscode.weatherapp.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jscode.weatherapp.models.CityData
import com.jscode.weatherapp.models.WeatherData

@Database(entities = [WeatherData::class, CityData::class], version = 6,exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object{
        @Volatile
        private var INSTANCE: WeatherDatabase?=null

        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java,"weather_database").fallbackToDestructiveMigration()
                    .build()
                INSTANCE =instance
                instance
            }
        }
    }
}