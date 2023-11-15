package com.example.foodappwithmvi.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodappwithmvi.utils.FOOD_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFood(food: FoodEntity)

    @Delete
    suspend fun deleteFood(food: FoodEntity)


    @Query("SELECT*FROM $FOOD_TABLE")
    fun getAllFoods(): Flow<MutableList<FoodEntity>>


    @Query("SELECT EXISTS (SELECT 1 FROM $FOOD_TABLE  WHERE id =:id)")
    fun existFood(id: Int): Flow<Boolean>

}