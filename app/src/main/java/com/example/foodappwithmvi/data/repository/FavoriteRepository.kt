package com.example.foodappwithmvi.data.repository

import com.example.foodappwithmvi.data.database.FoodDao
import javax.inject.Inject

class FavoriteRepository@Inject constructor(private val dao:FoodDao) {

    suspend fun getAllFoods()=dao.getAllFoods()



}