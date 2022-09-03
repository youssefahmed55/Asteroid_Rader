package com.udacity.asteroidrader.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Database(entities = [Asteroid::class,PictureOfDay::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val  dataBaseDao : DataBaseDao

    companion object{

        @Volatile
        private var  INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase{
            synchronized(this){
               var instance = INSTANCE

               if(instance == null){
                   instance = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"myDataBase")
                       .fallbackToDestructiveMigration()
                       .build()
                   INSTANCE = instance
               }

                return instance
           }


        }

    }
}