package com.udacity.asteroidradar.main

import com.udacity.asteroidradar.Asteroid


sealed class MainViewStates{
    object Idle : MainViewStates()
    data class ShowAsteroid(val asteroidMutableList: MutableList<Asteroid>?) : MainViewStates()
    data class Error(val error : String) : MainViewStates()

}
