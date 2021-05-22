package com.jscode.weatherapp.ui

import android.util.Log
import androidx.lifecycle.*
import com.jscode.weatherapp.models.CityData
import com.jscode.weatherapp.models.WeatherData
import com.jscode.weatherapp.repository.Repository
import com.jscode.weatherapp.util.Resource
import com.jscode.weatherapp.util.convertUtcToDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainViewModel(private val repo: Repository) : ViewModel() {
    companion object {
        const val TAG = "MainViewModel"
    }

    private val _snack = MutableLiveData<String>()
    val snack: LiveData<String>
        get() = _snack
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    init {
        viewModelScope.launch {
            repo.deleteOldWeatherData(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
        }
    }

    fun getCities() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getCities()))
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            _snack.postValue("Something Went Wrong")
            emit(Resource.Error<List<CityData>>(e))
        }
    }

    fun getWeatherData(city: CityData) = flow {
        repo.getWeatherData(city).collect {
            if (it is Resource.Success && it.data!=null) {
                val filteredData = mutableListOf<WeatherData>()
                val today = convertUtcToDate(System.currentTimeMillis())
                var indexOfToday = 0
                for(i in it.data.indices) {
                    if(convertUtcToDate(it.data[i].date)==today) {
                        indexOfToday=i
                        break
                    }
                }
                var index = indexOfToday
                while(index < it.data.size && index-indexOfToday < 5){
                    filteredData.add(it.data[index])
                    index++
                }
                emit(Resource.Success(filteredData))
            }
            else emit(it)
        }
    }

    fun addCity(city: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            repo.addCity(repo.fetchCity(city.split(" ").joinToString("%20"))[0])
            _navigateBack.postValue(true)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val message = when(e.message.toString().trim()){
                "Index: 0, Size: 0" -> "City Not Found"
                "HTTP 401 Unauthorized" -> "API access key is invalid"
                else -> "Something Went Wrong"
            }
            _snack.postValue(message)
        }
    }

    fun deleteCity(name: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            repo.deleteCity(name)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            _snack.postValue("Something Went Wrong")
        }
    }

    fun navigatedBack() = _navigateBack.postValue(false)
}