package com.example.shoplistapp.di

import android.app.Application
import com.example.shoplistapp.data.ShopListProvider
import com.example.shoplistapp.presentation.MainActivity
import com.example.shoplistapp.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(provider: ShopListProvider)

    fun inject(fragment: ShopItemFragment)

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(@BindsInstance application: Application): ApplicationComponent

    }

}