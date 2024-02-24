package com.example.foodrecipesapplication.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipesapplication.FoodRecipeApplication
import com.example.foodrecipesapplication.data.Repository
import com.example.foodrecipesapplication.models.FoodRecipe
import com.example.foodrecipesapplication.network.NetworkResult
import javax.inject.Inject

class foodRecipeViewModel @Inject constructor(
    application: FoodRecipeApplication, repository: Repository
) : AndroidViewModel(application) {

    var foodRecipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
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