package com.example.foodappwithmvi.utils.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


@Provides
@Singleton
fun provideConnectivityManager(@ApplicationContext context: Context)=context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager


    @Provides
    @Singleton
    fun provideNetworkRequest()=NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()



}