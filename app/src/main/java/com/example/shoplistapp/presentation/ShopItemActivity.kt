package com.example.shoplistapp.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.shoplistapp.R
import com.example.shoplistapp.databinding.ActivityShopItemBinding
import com.example.shoplistapp.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnItemEditingFinishedListener {

    private lateinit var binding: ActivityShopItemBinding
    private var screenMode = MODE_UNKNOWN
    private var itemId = ShopItem.NOT_SPECIFIED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        if (savedInstanceState == null) launchRightMode()

    }


    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(itemId)
            else -> throw RuntimeException("Param screen mode is absent")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) throw RuntimeException("Param screen mode is absent")
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) throw RuntimeException("Unknown mode $mode")
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(SHOP_ITEM_ID)) throw RuntimeException("Param shop item id is absent")
            itemId = intent.getIntExtra(SHOP_ITEM_ID, ShopItem.NOT_SPECIFIED_ID)
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(SHOP_ITEM_ID, itemId)
            return intent
        }
    }

    override fun onItemEditingFinish() {
        finish()
    }
}