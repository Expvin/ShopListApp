package com.example.shoplistapp.domain

import javax.inject.Inject

class UpdateShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {

    suspend fun updateItem(item: ShopItem) {
        shopListRepository.updateItem(item)
    }

}