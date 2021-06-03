package com.georgesamuel.capitertask.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.georgesamuel.capitertask.model.ProductDetails


@Database(entities = [ProductDetails::class],
version = 1,
exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
abstract fun appDao(): AppDao

}