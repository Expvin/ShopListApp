package com.example.shoplistapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplistapp.domain.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
     private val getShopListUseCase: GetShopListUseCase,
     private val updateShopItemUseCase: UpdateShopItemUseCase,
     private val deleteItemUseCase: DeleteItemUseCase): ViewModel() {

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteItemUseCase.deleteItem(shopItem)
        }

    }

    fun changedEnabledState(shopItem: ShopItem) {
        viewModelScope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            updateShopItemUseCase.updateItem(newItem)
        }

    }

}