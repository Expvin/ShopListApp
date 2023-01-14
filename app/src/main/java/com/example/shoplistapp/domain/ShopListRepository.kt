package com.example.shoplistapp.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addItem(item: ShopItem)

    fun deleteItem(item: ShopItem)

    fun getShopItem(id: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>

    fun updateItem(item: ShopItem)
}