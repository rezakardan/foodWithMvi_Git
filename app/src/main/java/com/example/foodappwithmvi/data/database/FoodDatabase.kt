package com.example.foodappwithmvi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FoodEntity::class], exportSchema = false, version = 1)
abstract class FoodDatabase:RoomDatabase() {



    abstract fun foodDao():FoodDao
}