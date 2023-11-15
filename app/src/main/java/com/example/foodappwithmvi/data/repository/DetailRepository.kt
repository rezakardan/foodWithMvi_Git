package com.example.foodappwithmvi.data.repository

import com.example.foodappwithmvi.data.database.FoodDao
import com.example.foodappwithmvi.data.database.FoodEntity
import com.example.foodappwithmvi.data.server.ApiService
import javax.inject.Inject

class DetailRepository@Inject constructor(private val api:ApiService,private val dao:FoodDao) {

    suspend fun detailFood(id:Int)=api.foodDetail(id)

    suspend fun saveFood(food:FoodEntity)=dao.saveFood(food)

    suspend fun deleteFood(food: FoodEntity)=dao.deleteFood(food)

     fun existFood(id:Int)=dao.existFood(id)





}