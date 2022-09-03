package com.udacity.asteroidradar.api



import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidrader.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

private val retrofit2 = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()


private const val API_KEY = ""    //<-You Can Write API KEY Here
interface AsteroidApiService {

  @GET("neo/rest/v1/feed?api_key=$API_KEY")
  suspend fun getAsteroids(@Query("start_date") startDate : String , @Query("end_date") endDate : String) : String

  @GET("planetary/apod?api_key=$API_KEY")
  suspend fun getPictureOfDay(): PictureOfDay
}

object AsteroidApi{
    val retrofitService : AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
    val retrofitService2 : AsteroidApiService by lazy {
        retrofit2.create(AsteroidApiService::class.java)
    }
}