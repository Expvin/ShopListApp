package com.example.shoplistapp.domain

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun addItem(item: ShopItem) {
        shopListRepository.addItem(item)
    }

}