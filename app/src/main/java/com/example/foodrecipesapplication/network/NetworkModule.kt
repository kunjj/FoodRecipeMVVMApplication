package com.example.foodrecipesapplication.network

import com.example.foodrecipesapplication.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder().client(okHttpClient).addConverterFactory(gsonConverterFactory)
            .baseUrl(Constant.BASE_URL).build()

    @Provides
    @Singleton
    fun apiServices(retrofit: Retrofit): FoodRecipeAPI = retrofit.create(FoodRecipeAPI::class.java)
}