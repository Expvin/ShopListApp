package com.example.shoplistapp.data.room

import com.example.shoplistapp.domain.ShopItem
import javax.inject.Inject

class ShopItemMapper @Inject constructor() {

    fun entityToDbModel(shopItem: ShopItem) = ShopItemDbModel(id = shopItem.id,
    name = shopItem.name, count = shopItem.count, enabled = shopItem.enabled)

    fun dbModelToEntity(shopItem: ShopItemDbModel) = ShopItem(id = shopItem.id,
        name = shopItem.name, count = shopItem.count, enabled = shopItem.enabled)

    fun dbModelListToEntity(shopItems: List<ShopItemDbModel>): List<ShopItem>{
        return shopItems.map { dbModelToEntity(it) }
    }
}