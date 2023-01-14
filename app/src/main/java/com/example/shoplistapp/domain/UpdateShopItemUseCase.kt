package com.example.shoplistapp.domain

class UpdateShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun updateItem(item: ShopItem) {
        shopListRepository.updateItem(item)
    }

}