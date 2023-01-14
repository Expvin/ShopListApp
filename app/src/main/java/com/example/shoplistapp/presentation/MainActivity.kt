package com.example.shoplistapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplistapp.R
import com.example.shoplistapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ShopItemFragment.OnItemEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopItemListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        setupFloatingButton()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
                adapter.submitList(it)
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
                viewModel.deleteShopItem(item)
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