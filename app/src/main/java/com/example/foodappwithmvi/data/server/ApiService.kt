package com.example.foodappwithmvi.data.server

import com.example.foodappwithmvi.data.model.ResponseCategoriesList
import com.example.foodappwithmvi.data.model.ResponseFoodsList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    suspend fun foodRandom():Response<ResponseFoodsList>


    @GET("categories.php")
    suspend fun categoriesList():Response<ResponseCategoriesList>

    @GET("search.php")
    suspend fun foodList(@Query("f")letter:String):Response<ResponseFoodsList>


    @GET("search.php")
    suspend fun searchFood(@Query("s")letter:String):Response<ResponseFoodsList>


    @GET("filter.php")
    suspend fun foodsByCategory(@Query("c") letter: String): Response<ResponseFoodsList>

    @GET("lookup.php")
    suspend fun foodDetail(@Query("i") id: Int): Response<ResponseFoodsList>
}