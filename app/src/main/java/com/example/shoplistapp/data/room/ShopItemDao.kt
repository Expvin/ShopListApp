package com.example.shoplistapp.data.room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopItemDb(shopItem: ShopItemDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShopItemProvider(shopItem: ShopItemDbModel)

    @Delete
    suspend fun deleteShopItemDb(shopItem: ShopItemDbModel)

    @Query("DELETE FROM shop_items WHERE id=:shopItemId")
    fun deleteShopItemProvider(shopItemId: Int): Int

    @Query("SELECT * FROM shop_items")
    fun getAllShopItemsDb(): LiveData<List<ShopItemDbModel>>

    @Query("SELECT * FROM shop_items")
    fun getAllShopItemsDbCursor(): Cursor

    @Query("SELECT * FROM shop_items WHERE id=:itemId LIMIT 1")
    suspend fun getShopItemDb(itemId: Int): ShopItemDbModel
}