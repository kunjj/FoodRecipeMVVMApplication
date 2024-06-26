package com.example.foodrecipesapplication.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Length(
    @SerializedName("number")
    val number: Int,
    @SerializedName("unit")
    val unit: String
): Parcelable