package com.example.foodappwithmvi.utils.di

import com.example.foodappwithmvi.data.server.ApiService
import com.example.foodappwithmvi.utils.BASE_URL
import com.example.foodappwithmvi.utils.NETWORK_TIMEOUT
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

@Provides
@Singleton
fun provideBaseUrl()= BASE_URL


    @Provides
    @Singleton
    fun provideTimeOut()=NETWORK_TIMEOUT




    @Provides
    @Singleton
    fun provideGson():Gson=GsonBuilder().setLenient().create()


    @Provides
    @Singleton
    fun provideBodyInterceptor()=HttpLoggingInterceptor().apply {

        level=HttpLoggingInterceptor.Level.BODY



    }



    @Provides
    @Singleton
    fun provideClient(body:HttpLoggingInterceptor,time:Long)=OkHttpClient.Builder()
        .addInterceptor(body)
        .connectTimeout(time,TimeUnit.SECONDS)
        .readTimeout(time,TimeUnit.SECONDS)
        .writeTimeout(time,TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()



    @Provides
    @Singleton
    fun provideRetrofit(baseUrl:String,client:OkHttpClient,gson:Gson):ApiService=Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)




}