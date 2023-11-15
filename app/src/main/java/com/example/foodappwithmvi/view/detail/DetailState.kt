package com.example.foodappwithmvi.view.detail

import com.example.foodappwithmvi.data.database.FoodEntity
import com.example.foodappwithmvi.data.model.ResponseFoodsList

sealed class DetailState{


object BackToHome:DetailState()

    object LoadProgress:DetailState()

    data class SaveFood(val unit:Unit):DetailState()

    data class DeleteFood(val unit:Unit):DetailState()

    data class FoodDetail(val food:ResponseFoodsList):DetailState()


    data class ExistFood(val isOrNo:Boolean):DetailState()

data class Error(val message:String):DetailState()


}
