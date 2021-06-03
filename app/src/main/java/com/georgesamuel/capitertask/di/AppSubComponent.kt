package com.georgesamuel.capitertask.di

import com.georgesamuel.capitertask.ui.CartActivity
import com.georgesamuel.capitertask.ui.MainActivity
import dagger.Subcomponent

@AppScope
@Subcomponent(modules = [CapiterAppModule::class])
interface AppSubComponent {
    fun inject(mainActivity: MainActivity)

    fun inject(cartActivity: CartActivity)

    @Subcomponent.Factory
    interface Factory{
        fun create():AppSubComponent
    }

}