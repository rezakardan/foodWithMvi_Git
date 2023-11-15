package com.example.foodappwithmvi.view.detail

import com.example.foodappwithmvi.data.database.FoodEntity

sealed class DetailIntent{

    data class FoodDetail(val id:Int):DetailIntent()

    data class ExistFood(val id:Int):DetailIntent()


    data class SaveFood(val food: FoodEntity):DetailIntent()

    data class DeleteFood(val food: FoodEntity):DetailIntent()

    object BackToHome:DetailIntent()





}
