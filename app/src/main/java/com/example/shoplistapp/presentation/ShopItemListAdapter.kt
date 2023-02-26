package com.example.shoplistapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.shoplistapp.R
import com.example.shoplistapp.databinding.ItemShopDisableBinding
import com.example.shoplistapp.databinding.ItemShopEnabledBinding
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
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),
        layout, parent, false)
        return ShopListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding
        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(item)
            true
        }
        binding.root.setOnClickListener {
            onClickListener?.invoke(item)
        }
        when (binding) {
            is ItemShopEnabledBinding -> {
                binding.shopItem = item
            }
            is ItemShopDisableBinding -> {
                binding.shopItem = item
            }
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