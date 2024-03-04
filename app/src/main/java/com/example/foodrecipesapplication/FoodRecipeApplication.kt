package com.example.foodrecipesapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodRecipeApplication : Application(){

    init {
        instance = this
    }

    companion object{
        lateinit var instance: FoodRecipeApplication

        fun getApplicationContext() = instance.applicationContext
    }
}