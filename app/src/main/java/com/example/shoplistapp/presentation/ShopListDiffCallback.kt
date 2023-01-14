package com.example.shoplistapp.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.shoplistapp.domain.ShopItem

class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>): DiffUtil.Callback(){

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}