package com.example.foodappwithmvi.view.favorite

import com.example.foodappwithmvi.data.database.FoodEntity

sealed class FavoriteState {



    object EmptyList:FavoriteState()
    data class GetAllFoods(val foodList:MutableList<FoodEntity>):FavoriteState()




}