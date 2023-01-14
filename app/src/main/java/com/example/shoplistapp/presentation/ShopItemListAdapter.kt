package com.example.shoplistapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoplistapp.R
import com.example.shoplistapp.domain.ShopItem


class ShopItemListAdapter : ListAdapter<ShopItem, ShopListViewHolder>(ShopItemDiffCallback()) {


    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val layout = when (viewType) {
            ENABLED -> R.layout.item_shop_enabled
            DISABLED -> R.layout.item_shop_disable
            else -> throw RuntimeException("Unknown ViewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val item = getItem(position)
        holder.titleTV.text = item.name
        holder.countTV.text = item.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(item)
            true
        }
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item.enabled) {
            return ENABLED
        } else {
            return DISABLED
        }
    }

    companion object {
        const val ENABLED = 1
        const val DISABLED = 0
        const val MAX_RV_PULL = 15
    }

}