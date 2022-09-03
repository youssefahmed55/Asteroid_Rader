package com.udacity.asteroidrader.main


import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidrader.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import org.json.JSONObject


class MainRepo constructor(private val appDatabase: AppDatabase) {
    // Delete Previous Day Then Refresh Data To DataBase For Coroutine Manager
    suspend fun refreshData(yesterdayDate : String , startDate: String,endDate: String){
        withContext(Dispatchers.IO){
            launch { deletePreviousDay(yesterdayDate) }.join()
            launch { insertPictureOfDay(getPictureOfDayR()) }.join()
            launch { insertAllAsteroids(getAsteroids(startDate,endDate)) }

        }

    }
    //Get Asteroids Of Week From Api
    suspend fun getAsteroids(startDate : String, endDate : String) : ArrayList<Asteroid>{
       return parseAsteroidsJsonResult(JSONObject( AsteroidApi.retrofitService.getAsteroids(startDate,endDate)))
    }
    //Get PictureOfDay From Api
    suspend fun getPictureOfDayR(): PictureOfDay{
          return AsteroidApi.retrofitService2.getPictureOfDay()
    }

    //Room
    ///Get All List Asteroid From DataBase
    suspend fun getAllAsteroids(): List<Asteroid>{
        return appDatabase.dataBaseDao.getAllAsteroids()
    }
    //Get List Asteroid Of Today From DataBase
    suspend fun getAllAsteroidByDay(date : String): List<Asteroid>{
        return appDatabase.dataBaseDao.getAllAsteroidByDay(date)
    }
    //Get List Asteroid Of Week From DataBase
    suspend fun getAllAsteroidWeek(startDate : String, endDate :String): List<Asteroid>{
        return appDatabase.dataBaseDao.getAllAsteroidWeek(startDate,endDate)
    }
    //Get All List Asteroid To DataBase
    suspend fun insertAllAsteroids(asteroidsArrayList:ArrayList<Asteroid>){
        appDatabase.dataBaseDao.insertAllAsteroids(asteroidsArrayList)
    }
    //Delete Previous Day From DataBase
    suspend fun deletePreviousDay(date : String){
        appDatabase.dataBaseDao.deletePreviousDay(date)
    }

    //Get PictureOfDay Model From DataBase
    suspend fun getPictureOfDayD(): PictureOfDay {
        return appDatabase.dataBaseDao.getPictureOfDay()
    }
    //Insert PictureOfDay Model To DataBase
    suspend fun insertPictureOfDay(pictureOfDay: PictureOfDay){
        return appDatabase.dataBaseDao.insertPictureOfDay(pictureOfDay)
    }


}