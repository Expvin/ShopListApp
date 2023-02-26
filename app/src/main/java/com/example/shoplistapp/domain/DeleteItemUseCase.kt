package com.example.shoplistapp.domain

import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {

    suspend fun deleteItem(item: ShopItem) {
        shopListRepository.deleteItem(item)
    }

}