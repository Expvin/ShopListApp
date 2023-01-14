package com.example.shoplistapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoplistapp.domain.ShopItem
import com.example.shoplistapp.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl: ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = sortedSetOf<ShopItem>( {o1, o2 -> o1.id.compareTo(o2.id)} )
    private var autoIncrementId = 0

    init {
        for (i in 0 until 100) {
            val item = ShopItem("Element $i", 1, Random.nextBoolean())
            addItem(item)
        }
    }

    override fun addItem(item: ShopItem) {
        if (item.id == ShopItem.NOT_SPECIFIED_ID) item.id = autoIncrementId++
        shopList.add(item)
        updateList()
    }

    override fun deleteItem(item: ShopItem) {
        shopList.remove(item)
        updateList()
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList.find {
            it.id == id
        } ?: throw RuntimeException("Element with id - $id not found")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    override fun updateItem(item: ShopItem) {
        val oldElementId = getShopItem(item.id)
        deleteItem(oldElementId)
        addItem(item)
    }

    private fun updateList() {
        shopListLD.value = shopList.toList()
    }
}