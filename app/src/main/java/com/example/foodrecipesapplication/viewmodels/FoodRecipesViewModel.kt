package com.example.foodrecipesapplication.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapplication.R
import com.example.foodrecipesapplication.models.FoodRecipe
import com.example.foodrecipesapplication.network.NetworkListener
import com.example.foodrecipesapplication.network.NetworkResponse
import com.example.foodrecipesapplication.repositories.FoodRecipesRepository
import com.example.foodrecipesapplication.room.entities.FoodRecipeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FoodRecipesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val foodRecipesRepository: FoodRecipesRepository
) : ViewModel() {
    private val networkListener: NetworkListener = NetworkListener()

    val readRecipes: LiveData<List<FoodRecipeEntity>> =
        foodRecipesRepository.readDatabase().asLiveData()

    private fun insertRecipesToRoomDatabase(foodRecipeEntity: FoodRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            foodRecipesRepository.insertFoodRecipes(foodRecipeEntity)
        }

    var foodRecipesResponse: MutableLiveData<NetworkResponse<FoodRecipe>> = MutableLiveData()

    var searchRecipesResponse: MutableLiveData<NetworkResponse<FoodRecipe>> = MutableLiveData()

    fun getRandomRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getSafeRandomRecipesApiCall(queries)
    }

    fun searchFoodRecipes(searchQuery: Map<String,String>) = viewModelScope.launch {
        getSafeSearchRecipesApiCall(searchQuery)
    }

    private suspend fun getSafeRandomRecipesApiCall(queries: Map<String, String>) {
        foodRecipesResponse.value = NetworkResponse.Loading()
        networkListener.isConnectedToInternet(context).collect { isConnected ->
            if (isConnected) {
                val response = foodRecipesRepository.getRandomRecipes(queries)
                foodRecipesResponse.value = handleResponse(response)
                val foodRecipes = foodRecipesResponse.value!!.data
                if (foodRecipes != null) offlineCacheRecipes(foodRecipes)
            } else foodRecipesResponse.value = NetworkResponse.Error(
                context.getString(
                    R.string.not_connected_to_internet
                )
            )
        }
    }

    private suspend fun getSafeSearchRecipesApiCall(queries: Map<String, String>) {
        searchRecipesResponse.value = NetworkResponse.Loading()
        networkListener.isConnectedToInternet(context).collect { isConnected ->
            if (isConnected) {
                val response = foodRecipesRepository.searchRecipes(queries)
                searchRecipesResponse.value = handleResponse(response)
            } else searchRecipesResponse.value = NetworkResponse.Error(
                context.getString(
                    R.string.not_connected_to_internet
                )
            )
        }
    }

    private fun offlineCacheRecipes(foodRecipes: FoodRecipe) =
        insertRecipesToRoomDatabase(FoodRecipeEntity(foodRecipe = foodRecipes))

    private fun handleResponse(response: Response<FoodRecipe>): NetworkResponse<FoodRecipe> {
        return when {
            response.message().contains("timeout") -> NetworkResponse.Error(
                context.getString(R.string.connection_timeout)
            )

            response.code() == 402 -> NetworkResponse.Error(
                context.getString(R.string.try_after_sometimes)
            )

            response.body()?.recipes.isNullOrEmpty() -> NetworkResponse.Error(
                context.applicationContext.getString(R.string.no_recipes_found)
            )

            response.isSuccessful -> NetworkResponse.Success(response.body()!!)

            else -> NetworkResponse.Error(response.message())
        }
    }
}