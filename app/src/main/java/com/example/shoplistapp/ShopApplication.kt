package com.example.shoplistapp

import android.app.Application
import com.example.shoplistapp.di.DaggerApplicationComponent

class ShopApplication: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }

}