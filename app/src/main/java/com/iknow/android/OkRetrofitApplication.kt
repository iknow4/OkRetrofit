package com.iknow.android

import android.app.Application
import com.iknow.okretrofit.OkHttpClientManager
import com.iknow.okretrofit.OkRetrofitManager

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2019/03/18 11:33 PM
 * version: 1.0
 * description:
 */
class OkRetrofitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //init OkRetrofit
        OkHttpClientManager.init(TestHttpContext())
        OkRetrofitManager.getInstance().init()
    }
}