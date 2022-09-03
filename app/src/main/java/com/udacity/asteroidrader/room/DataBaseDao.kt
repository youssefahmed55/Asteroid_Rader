package com.udacity.asteroidrader.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface DataBaseDao {

    @Query("SELECT * FROM Asteroid ORDER BY closeApproachDate ASC")
    suspend fun getAllAsteroids(): List<Asteroid>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate Like :date")
    suspend fun getAllAsteroidByDay(date : String): List<Asteroid>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate ASC" )
    suspend fun getAllAsteroidWeek(startDate : String , endDate : String): List<Asteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAsteroids(asteroidsArrayList:ArrayList<Asteroid>)

    @Query("DELETE FROM Asteroid WHERE closeApproachDate Like :date")
    suspend fun deletePreviousDay(date : String)

    @Query("SELECT * FROM pictureOfDay")
    suspend fun getPictureOfDay(): PictureOfDay

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfDay(pictureOfDay: PictureOfDay)
}