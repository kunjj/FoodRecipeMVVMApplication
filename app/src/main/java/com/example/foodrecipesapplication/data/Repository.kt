package com.example.foodrecipesapplication.data

import com.example.foodrecipesapplication.models.FoodRecipe
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getRandomRecipies(queries: Map<String, String>): Response<FoodRecipe> =
        remoteDataSource.getRandomRecipies(queries)
}