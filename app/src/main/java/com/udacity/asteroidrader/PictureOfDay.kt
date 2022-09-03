package com.udacity.asteroidradar

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "pictureOfDay")
data class PictureOfDay(
                        @PrimaryKey
                        val id: Int = 1,
                        @Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)