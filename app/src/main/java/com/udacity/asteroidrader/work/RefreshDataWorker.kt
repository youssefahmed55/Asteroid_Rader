package com.udacity.asteroidrader.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidrader.Constants
import com.udacity.asteroidrader.main.MainRepo
import com.udacity.asteroidrader.room.AppDatabase
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class RefreshDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AppDatabase.getInstance(applicationContext)
        val mainRepo = MainRepo(database)
        val dates = getNextSevenDaysFormattedDates()

        return try {
            mainRepo.refreshData(getYesterdayDate(),dates.first(),dates.last()) // Delete Previous Day Then Refresh Data To DataBase For Coroutine Manager
            Result.success()
        }catch (e : HttpException){
            return Result.retry()
        }
    }

    private fun getYesterdayDate() : String{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.ENGLISH)

        return dateFormat.format(currentTime)
    }
}