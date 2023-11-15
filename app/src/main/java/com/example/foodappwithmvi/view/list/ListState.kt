package com.example.foodappwithmvi.view.list

import com.example.foodappwithmvi.data.model.ResponseCategoriesList
import com.example.foodappwithmvi.data.model.ResponseFoodsList
import retrofit2.Response

sealed class ListState{

object Idle:ListState()
    data class SpinnerList(val list:List<Char>):ListState()

data class FoodRandom(val food:ResponseFoodsList.Meal?):ListState()


    data class Error(val message:String):ListState()

    data class CategoryList(val list:MutableList<ResponseCategoriesList.Category>):ListState()

    object LoadCategoryProgress:ListState()

    object LoadFoodListProgress:ListState()

    data class FoodList(val foodList:MutableList<ResponseFoodsList.Meal>):ListState()

    object EmptyList:ListState()



}
