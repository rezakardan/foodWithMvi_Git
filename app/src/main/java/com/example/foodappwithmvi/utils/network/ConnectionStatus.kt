package com.example.foodappwithmvi.utils.network

import kotlinx.coroutines.flow.Flow

interface ConnectionStatus {

enum class Status{Available,UnAvailable,Losing,Lost}


    fun observe():Flow<Status>







}