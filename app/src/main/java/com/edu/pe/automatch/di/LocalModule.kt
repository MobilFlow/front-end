package com.edu.pe.automatch.di
import androidx.room.Room
import com.edu.pe.automatch.MyApplication
import com.edu.pe.automatch.data.local.AppDatabase

object LocalModule {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            MyApplication.instance.applicationContext,
            AppDatabase::class.java,
            "automatch.db"
        ).build()
    }

    fun provideDatabase(): AppDatabase = database

    fun provideUserDao() = database.userDao()
}