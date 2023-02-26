package com.example.shoplistapp.data.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getDao(): ShopItemDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private val DB_NAME = "shop_items.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application, AppDatabase::class.java, DB_NAME
                )
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}