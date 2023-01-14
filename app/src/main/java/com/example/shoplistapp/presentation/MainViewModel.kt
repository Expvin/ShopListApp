package com.example.shoplistapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplistapp.data.ShopListRepositoryImpl
import com.example.shoplistapp.domain.*

class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)
    private val deleteItemUseCase = DeleteItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteItemUseCase.deleteItem(shopItem)
    }

    fun changedEnabledState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        updateShopItemUseCase.updateItem(newItem)
    }

}