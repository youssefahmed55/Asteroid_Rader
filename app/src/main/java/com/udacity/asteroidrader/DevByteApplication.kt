package com.udacity.asteroidrader

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.work.*
import com.udacity.asteroidrader.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val TAG = "DevByteApplication"
class DevByteApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Started ")
        setupRecurringWork()
    }
    private fun setupRecurringWork() {
        applicationScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .build()

           /* val workRequest:WorkRequest = OneTimeWorkRequest.Builder(RefreshDataWorker::class.java).setConstraints(constraints).build()
            WorkManager.getInstance(applicationContext).enqueue(workRequest)*/
            val repeatingRequest = PeriodicWorkRequest.Builder(RefreshDataWorker::class.java,1, TimeUnit.DAYS)
               .setConstraints(constraints)
               .addTag(RefreshDataWorker.WORK_NAME)
               .build()

            WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
               RefreshDataWorker.WORK_NAME,
               ExistingPeriodicWorkPolicy.KEEP,
               repeatingRequest)
        }




    }


}