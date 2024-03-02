package com.example.foodrecipesapplication.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapplication.R
import com.example.foodrecipesapplication.data.Repository
import com.example.foodrecipesapplication.models.FoodRecipe
import com.example.foodrecipesapplication.network.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class FoodRecipesViewModel @Inject constructor(
    private val application: Application, private val repository: Repository
) : AndroidViewModel(application) {
    var foodRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRandomFoodsRecipes(queries: Map<String, String>) {
        viewModelScope.launch {
            getSafeApiCall(queries)
        }
    }

    private suspend fun getSafeApiCall(queries: Map<String, String>) {
        foodRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.getRandomRecipes(queries)
                foodRecipesResponse.value = handleApiResponse(response)
            } catch (e: Exception) {

            }
        } else {
            foodRecipesResponse.value =
                NetworkResult.Error(application.getString(R.string.not_connected_to_internet))
        }
    }

    private fun handleApiResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> return NetworkResult.Error("Connection Timeout!")

            response.code() == 402 -> return NetworkResult.Error("Try After Sometimes.") // API Key Limit.

            response.body()!!.recipes.isEmpty() -> return NetworkResult.Error("No Recipes Found")

            response.isSuccessful -> return  NetworkResult.Success(response.body()!!)

            else -> return  NetworkResult.Error(response.message())
        }
    }

    fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}