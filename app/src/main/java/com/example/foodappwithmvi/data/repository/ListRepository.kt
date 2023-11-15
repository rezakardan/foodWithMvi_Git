package com.example.foodappwithmvi.data.repository

import com.example.foodappwithmvi.data.database.FoodDao
import com.example.foodappwithmvi.data.server.ApiService
import javax.inject.Inject

class ListRepository @Inject constructor(private val api: ApiService) {


    suspend fun randomFood() = api.foodRandom()

    suspend fun categoryFood() = api.categoriesList()

    suspend fun foodList(letter: String) = api.foodList(letter)


    suspend fun searchFood(letter: String) = api.searchFood(letter)


    suspend fun foodByCategory(letter: String) = api.foodsByCategory(letter)






}