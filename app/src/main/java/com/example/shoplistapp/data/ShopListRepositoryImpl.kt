package com.example.shoplistapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.shoplistapp.data.room.AppDatabase
import com.example.shoplistapp.data.room.ShopItemDao
import com.example.shoplistapp.data.room.ShopItemMapper
import com.example.shoplistapp.domain.ShopItem
import com.example.shoplistapp.domain.ShopListRepository
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor
    (application: Application,
     private val shopItemDao: ShopItemDao): ShopListRepository {

    override suspend fun addItem(item: ShopItem) {
        shopItemDao.insertShopItemDb(ShopItemMapper().entityToDbModel(item))
    }

    override suspend fun deleteItem(item: ShopItem) {
        shopItemDao.deleteShopItemDb(ShopItemMapper().entityToDbModel(item))
    }

    override suspend fun getShopItem(id: Int): ShopItem {
        val item = shopItemDao.getShopItemDb(id)
        return ShopItemMapper().dbModelToEntity(item)
    }

    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        shopItemDao.getAllShopItemsDb()
    ) {
        ShopItemMapper().dbModelListToEntity(it)
    }

// 2 Способ через MediatorLiveData
//        MediatorLiveData<List<ShopItem>>().apply {
//        addSource(shopItemDao.getAllShopItemsDb()) {
//            value = ShopItemMapper().dbModelListToEntity(it)
//        }
//    }


    override suspend fun updateItem(item: ShopItem) {
        shopItemDao.insertShopItemDb(ShopItemMapper().entityToDbModel(item))
    }

}