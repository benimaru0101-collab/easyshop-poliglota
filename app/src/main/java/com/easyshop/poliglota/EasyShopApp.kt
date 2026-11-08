package com.easyshop.poliglota

import android.app.Application
import com.easyshop.poliglota.di.Servicos

class EasyShopApp : Application() {

    lateinit var servicos: Servicos
        private set

    override fun onCreate() {
        super.onCreate()
        servicos = Servicos(applicationContext)
    }
}
