package com.georgesamuel.capitertask.di

import android.content.Context
import androidx.room.Room
import com.georgesamuel.capitertask.db.AppDao
import com.georgesamuel.capitertask.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {
    @Singleton
    @Provides
    fun provideAppDataBase(context: Context):AppDatabase{
     return Room.databaseBuilder(context,AppDatabase::class.java,"capitertask")
         .build()
    }
    @Singleton
    @Provides
    fun provideAppDao(appDatabase: AppDatabase):AppDao{
        return appDatabase.appDao()
    }

}