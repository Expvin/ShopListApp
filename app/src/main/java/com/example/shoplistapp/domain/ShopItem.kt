package com.example.shoplistapp.domain

data class ShopItem(
    val name: String,
    val count: Int,
    val enabled: Boolean,
    var id: Int = NOT_SPECIFIED_ID,
) {
    companion object {
        const val NOT_SPECIFIED_ID = -1
    }
}
