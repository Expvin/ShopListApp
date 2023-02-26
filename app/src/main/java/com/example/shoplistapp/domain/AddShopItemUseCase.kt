package com.example.shoplistapp.domain

import javax.inject.Inject

class AddShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {

    suspend fun addItem(item: ShopItem) {
        shopListRepository.addItem(item)
    }

}