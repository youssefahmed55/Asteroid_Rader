package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidrader.main.MainRepo
import com.udacity.asteroidrader.room.AppDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow


class MainViewModel(val app: Application) : AndroidViewModel(app) {
    var intentChannel = Channel<MainIntent>(Channel.UNLIMITED)


    private val _mutableStateFlow = MutableStateFlow<MainViewStates>(MainViewStates.Idle)
    val states : MutableStateFlow<MainViewStates> get() = _mutableStateFlow

    private val _mutableLiveDataPhoto = MutableLiveData<PictureOfDay>()
    val pic : MutableLiveData<PictureOfDay> get() = _mutableLiveDataPhoto

    private val TAG = "MainViewModel"
    val handler = CoroutineExceptionHandler() { coroutineContext, throwable -> _mutableStateFlow.value = MainViewStates.Error(throwable.message!!)}

    private val dataBase = AppDatabase.getInstance(getApplication<Application>().applicationContext)
    private val mainRepo = MainRepo(dataBase)

    init {
        processIntent()
    }
    fun processIntent(){

        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect{
                when(it){
                    is MainIntent.GetAsteroids -> reduceIntent(it.startDate,it.endDate)
                }
            }
        }
    }

    fun reduceIntent(startDate:String,endDate : String){

        viewModelScope.launch( Dispatchers.IO + handler ) {

            launch {val asteroids = async {mainRepo.getAsteroids(startDate,endDate)}
                    mainRepo.insertAllAsteroids(asteroids.await())
            }.join()

            launch { getAllAsteroidWeek(startDate,endDate) }.join()

            launch {
                val photo = async { mainRepo.getPictureOfDayR() }
                mainRepo.insertPictureOfDay(photo.await())
            }.join()

            launch { getPictureOfDay() }

        }
    }



    //Get All List Asteroid To DataBase
    fun getAllAsteroids(){
        viewModelScope.launch(Dispatchers.IO + handler) {
           val data = mainRepo.getAllAsteroids()
            _mutableStateFlow.value= MainViewStates.ShowAsteroid(data.toMutableList())
        }

    }
    //Get List Asteroid Of Today From DataBase
    fun getAllAsteroidByDay(date : String){
        viewModelScope.launch(Dispatchers.IO + handler) {
            val data = mainRepo.getAllAsteroidByDay(date)
            _mutableStateFlow.value= MainViewStates.ShowAsteroid(data.toMutableList())
        }
    }
    //Get List Asteroid Of Week From DataBase
    fun getAllAsteroidWeek(startDate : String,endDate: String){
        viewModelScope.launch(Dispatchers.IO + handler) {
            val data = mainRepo.getAllAsteroidWeek(startDate,endDate)
            _mutableStateFlow.value= MainViewStates.ShowAsteroid(data.toMutableList())
        }
    }
    //Get PictureOfDay Model From DataBase
    fun getPictureOfDay(){
        Log.d(TAG, "getPictureOfDay: Here")
        viewModelScope.launch(Dispatchers.IO + handler) {
            val data = mainRepo.getPictureOfDayD()
            withContext(Dispatchers.Main){
                Log.d(TAG, "getPictureOfDay: " + data)
                _mutableLiveDataPhoto.value = data
            }
        }
    }



}