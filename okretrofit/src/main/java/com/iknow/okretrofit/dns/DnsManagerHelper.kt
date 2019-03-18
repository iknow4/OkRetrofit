package com.iknow.okretrofit.dns

import com.qiniu.android.dns.DnsManager
import com.qiniu.android.dns.IResolver
import com.qiniu.android.dns.NetworkInfo
import com.qiniu.android.dns.local.AndroidDnsServer
import com.qiniu.android.dns.local.Resolver

import java.net.InetAddress
import java.util.ArrayList

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/11/14 11:22 PM
 * version: 1.0
 * description:
 */
object DnsManagerHelper {

    val dnsManager: DnsManager?
        get() = InstanceHolder.sInstance

    private object InstanceHolder {
        val sInstance = initDnsManager()
    }

    private fun initDnsManager(): DnsManager? {
        val rs = ArrayList<IResolver>(2)
        try {
            val r1 = AndroidDnsServer.defaultResolver()
            rs.add(r1)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {
            val r2 = Resolver(InetAddress.getByName("119.29.29.29"))
            rs.add(r2)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return if (rs.size == 0) {
            null
        } else DnsManager(NetworkInfo.normal, rs.toTypedArray<IResolver>())

    }
}
