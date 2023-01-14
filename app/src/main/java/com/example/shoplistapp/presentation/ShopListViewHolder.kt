package com.example.shoplistapp.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplistapp.R

class ShopListViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val titleTV = view.findViewById<TextView>(R.id.item_name)
    val countTV = view.findViewById<TextView>(R.id.item_count)
}