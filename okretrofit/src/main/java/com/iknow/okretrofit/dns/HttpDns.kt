package com.iknow.okretrofit.dns

import com.qiniu.android.dns.Domain
import okhttp3.Dns

import java.net.InetAddress
import java.net.UnknownHostException
import java.util.ArrayList

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/11/15 3:21 PM
 * version: 1.0
 * description:
 */
class HttpDns : Dns {

    @Throws(UnknownHostException::class)
    override fun lookup(hostname: String): List<InetAddress>? {
        val dns = DnsManagerHelper.dnsManager
        try {
            val queryInetAdress = dns!!.queryInetAdress(Domain(hostname))
            if (queryInetAdress.size > 0) {
                val inetAddresses = ArrayList<InetAddress>()
                for (inetAddress in queryInetAdress) {
                    inetAddresses.add(inetAddress)
                }
                return inetAddresses
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val inetAddresses = SYSTEM.lookup(hostname)

        var localDnsResolve = ""
        if (inetAddresses != null && inetAddresses.size > 0) {
            for (inetAddress in inetAddresses) {
                localDnsResolve = "localDns lookup:" + hostname + " -> ip:" + inetAddress.hostAddress
            }
        }

        return inetAddresses
    }

    companion object {
        private val SYSTEM = Dns.SYSTEM
    }
}
