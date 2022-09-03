package com.udacity.asteroidradar.main

import android.content.Context

sealed class MainIntent{
    data class GetAsteroids(val startDate:String , val endDate:String , val context: Context) : MainIntent()
}
