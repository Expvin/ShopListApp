package com.example.shoplistapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplistapp.data.ShopListRepositoryImpl
import com.example.shoplistapp.domain.*

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    //Input Name error
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    //Input Count error
    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    //Get item
    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    //Activity finish
    private val _shouldCloseActivity = MutableLiveData<Unit>()
    val shouldCloseActivity: LiveData<Unit>
        get() = _shouldCloseActivity

    fun getShopItem(itemId: Int) {
        val item = getShopItemUseCase.getShopItem(itemId)
        _shopItem.value = item
    }

    fun updateShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValidInput = validInput(name, count)
        if (fieldValidInput) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                updateShopItemUseCase.updateItem(item)
                closeActivity()
            }
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValidInput = validInput(name, count)
        if (fieldValidInput) addShopItemUseCase.addItem(ShopItem(name, count, true))
        closeActivity()
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: java.lang.Exception) {
            0
        }
    }

    private fun validInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            result = false
            _errorInputName.value = true
        }
        if (count <= 0) {
            result = false
            _errorInputCount.value = true
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun closeActivity() {
        _shouldCloseActivity.value = Unit
    }

}