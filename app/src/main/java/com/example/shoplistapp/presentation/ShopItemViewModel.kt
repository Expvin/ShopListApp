package com.example.shoplistapp.presentation

import android.app.Application
import androidx.lifecycle.*
import com.example.shoplistapp.data.ShopListRepositoryImpl
import com.example.shoplistapp.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val getShopItemUseCase: GetShopItemUseCase,
    private val updateShopItemUseCase: UpdateShopItemUseCase,
    private val addShopItemUseCase: AddShopItemUseCase) : ViewModel() {

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
        viewModelScope.launch {
            val item = getShopItemUseCase.getShopItem(itemId)
            _shopItem.value = item
        }
    }

    fun updateShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValidInput = validInput(name, count)
        if (fieldValidInput) {
            viewModelScope.launch {
                _shopItem.value?.let {
                    val item = it.copy(name = name, count = count)
                    updateShopItemUseCase.updateItem(item)
                    closeActivity()
                }
            }

        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValidInput = validInput(name, count)
        viewModelScope.launch {
            if (fieldValidInput) addShopItemUseCase.addItem(ShopItem(name, count, true))
            closeActivity()
        }

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