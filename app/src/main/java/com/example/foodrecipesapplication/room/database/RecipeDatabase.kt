package com.example.foodrecipesapplication.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodrecipesapplication.room.converters.RecipesTypeConverter
import com.example.foodrecipesapplication.room.dao.RecipesDao
import com.example.foodrecipesapplication.room.entities.FoodRecipeEntity
import java.io.Serializable

@Database(entities = [FoodRecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipeDatabase : RoomDatabase(), Serializable {
    abstract fun recipesDao(): RecipesDao

}