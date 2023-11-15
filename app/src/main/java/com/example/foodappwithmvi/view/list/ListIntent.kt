package com.example.foodappwithmvi.view.list

import com.example.foodappwithmvi.data.model.ResponseFoodsList

sealed class ListIntent{


object SpinnerList:ListIntent()

    object FoodRandom:ListIntent()


    object CategoryList:ListIntent()


    data class FoodList(val letter:String):ListIntent()


    data class SearchFood(val letter:String):ListIntent()


    data class ChooseFoodByCategory(val letter:String):ListIntent()
}
