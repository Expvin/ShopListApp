package com.example.shoplistapp.domain

class DeleteItemUseCase(private val shopListRepository: ShopListRepository) {

    fun deleteItem(item: ShopItem) {
        shopListRepository.deleteItem(item)
    }

}