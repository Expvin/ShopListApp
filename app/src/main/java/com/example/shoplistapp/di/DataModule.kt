package com.example.shoplistapp.di

import android.app.Application
import com.example.shoplistapp.data.ShopListRepositoryImpl
import com.example.shoplistapp.data.room.AppDatabase
import com.example.shoplistapp.data.room.ShopItemDao
import com.example.shoplistapp.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {
        @Provides
        fun provideShopItemDao(application: Application): ShopItemDao {
            return AppDatabase.getInstance(application).getDao()
        }
    }

}