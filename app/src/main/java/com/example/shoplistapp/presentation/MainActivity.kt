package com.example.shoplistapp.presentation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplistapp.R
import com.example.shoplistapp.ShopApplication
import com.example.shoplistapp.databinding.ActivityMainBinding
import com.example.shoplistapp.domain.ShopItem
import javax.inject.Inject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), ShopItemFragment.OnItemEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopItemListAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (application as ShopApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        setupFloatingButton()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
                adapter.submitList(it)
        }
        thread {
            val cursor = contentResolver.query(Uri.parse("content://com.example.shoplistapp/shop_list"),
                null, null, null, null)
            while (cursor?.moveToNext() == true) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val count = cursor.getInt(cursor.getColumnIndexOrThrow("count"))
                val enabled = cursor.getInt(cursor.getColumnIndexOrThrow("enabled")) > 0
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled)
                Log.d("MainActivity", "$shopItem")
            }
            cursor?.close()
        }

    }

    private fun isOnPaneMode(): Boolean {
        return binding.mainFragmentContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupFloatingButton() {
        binding.mainFloatingButton.setOnClickListener {
            if (isOnPaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else launchFragment(ShopItemFragment.newInstanceAddItem())
        }
    }

    private fun setupAdapter() {
        adapter = ShopItemListAdapter()
        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.recycledViewPool.setMaxRecycledViews(
            ShopItemListAdapter.ENABLED, ShopItemListAdapter.MAX_RV_PULL)
        binding.mainRecyclerView.recycledViewPool.setMaxRecycledViews(
            ShopItemListAdapter.DISABLED, ShopItemListAdapter.MAX_RV_PULL)
        setupLongClick()
        setupClick()
        setupSwipe()
    }

    private fun setupSwipe() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
                    or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
//                viewModel.deleteShopItem(item)
                thread {
                    contentResolver.delete(Uri.parse("content://com.example.shoplistapp/shop_list"),
                        null, arrayOf(item.id.toString())
                    )
                }

            }
        }).attachToRecyclerView(binding.mainRecyclerView)
    }

    private fun setupClick() {
        adapter.onClickListener = {
            if (isOnPaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
        }
    }

    private fun setupLongClick() {
        adapter.onShopItemLongClickListener = {
            viewModel.changedEnabledState(it)
        }
    }

    override fun onItemEditingFinish() {
        supportFragmentManager.popBackStack()
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
    }
}