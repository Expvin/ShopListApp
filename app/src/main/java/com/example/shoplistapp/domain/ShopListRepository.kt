package com.example.shoplistapp.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    suspend fun addItem(item: ShopItem)

    suspend fun deleteItem(item: ShopItem)

    suspend fun getShopItem(id: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>

    suspend fun updateItem(item: ShopItem)
}