package com.example.shoplistapp.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.shoplistapp.ShopApplication
import com.example.shoplistapp.data.room.ShopItemDao
import com.example.shoplistapp.data.room.ShopItemMapper
import com.example.shoplistapp.domain.ShopItem
import javax.inject.Inject

class ShopListProvider: ContentProvider() {

    private val component by lazy {
        (context as ShopApplication).component
    }

    @Inject
    lateinit var shopItemDao: ShopItemDao

    @Inject
    lateinit var mapper: ShopItemMapper

    private val uriMather = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.example.shoplistapp", "shop_list", GET_SHOP_TABLE_QUERY )
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        return when (uriMather.match(p0)) {
            GET_SHOP_TABLE_QUERY -> {
                shopItemDao.getAllShopItemsDbCursor()
            }
            else -> null
        }
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        when(uriMather.match(p0)) {
            GET_SHOP_TABLE_QUERY -> {if (p1 == null) return null
                val id = p1.getAsInteger("id")
                val name = p1.getAsString("name")
                val count = p1.getAsInteger("count")
                val enabled = p1.getAsBoolean("enabled")
                val shopItem = ShopItem(name = name,
                    count = count, enabled = enabled, id = id)
                shopItemDao.insertShopItemProvider(mapper.entityToDbModel(shopItem))
            }
        }
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        when(uriMather.match(p0)) {
            GET_SHOP_TABLE_QUERY -> {
                val shopItemId = p2?.get(0)?.toInt() ?: return -1
                return shopItemDao.deleteShopItemProvider(shopItemId)
            }
        }
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        when(uriMather.match(p0)) {
            GET_SHOP_TABLE_QUERY -> {
                val id = p1?.getAsInteger("id") ?: return -1
                val name = p1?.getAsString("name") ?: return -1
                val count = p1?.getAsInteger("count") ?: return -1
                val enabled = p1?.getAsBoolean("enabled") ?: return -1
                val shopItem = ShopItem(name = name,
                    count = count, enabled = enabled, id = id)
                shopItemDao.insertShopItemProvider(mapper.entityToDbModel(shopItem))
                return 1
            }
        }
        return 0
    }

    companion object {
        const val GET_SHOP_TABLE_QUERY = 222
    }
}