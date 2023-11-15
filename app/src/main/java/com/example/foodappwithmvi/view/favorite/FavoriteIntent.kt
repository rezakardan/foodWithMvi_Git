package com.example.foodappwithmvi.view.favorite

sealed class FavoriteIntent {

    object GetAllFoods:FavoriteIntent()
}