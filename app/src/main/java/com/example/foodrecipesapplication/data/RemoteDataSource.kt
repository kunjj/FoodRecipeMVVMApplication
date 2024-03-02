package com.example.foodrecipesapplication.data

import com.example.foodrecipesapplication.models.FoodRecipe
import com.example.foodrecipesapplication.network.FoodRecipeAPI
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val foodRecipeAPI: FoodRecipeAPI) {
    suspend fun getRandomRecipes(queries: Map<String, String>): Response<FoodRecipe> =
        foodRecipeAPI.getRandomFoodRecipes(queries)
}