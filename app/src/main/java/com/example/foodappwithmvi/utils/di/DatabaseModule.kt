package com.example.foodappwithmvi.utils.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodappwithmvi.data.database.FoodDatabase
import com.example.foodappwithmvi.data.database.FoodEntity
import com.example.foodappwithmvi.utils.FOOD_DB_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {


 @Provides
 @Singleton
 fun provideFoodEntity()=FoodEntity()


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context)=Room.databaseBuilder(context,FoodDatabase::class.java,
        FOOD_DB_DATABASE)
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun provideDao(db:FoodDatabase)=db.foodDao()


}