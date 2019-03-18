package com.iknow.android

import com.iknow.okretrofit.HttpConfig
import com.iknow.okretrofit.IHttpContext

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2019/03/18 11:31 PM
 * version: 1.0
 * description:
 */
class TestHttpContext : IHttpContext{
    override fun provideHttpConfig(key: Int): HttpConfig {

    }

    override fun provideUserAgent(): String {

    }

    override fun isPrivateHost(host: String?): Boolean {
    }
}