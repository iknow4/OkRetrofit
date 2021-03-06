package com.iknow.okretrofit;

import android.util.LruCache;
import com.iknow.okretrofit.dns.HttpDns;
import com.iknow.okretrofit.interceptors.CacheInterceptor;
import com.iknow.okretrofit.interceptors.HttpExceptionInterceptor;
import com.iknow.okretrofit.interceptors.RefreshMobileTokenInterceptor;
import io.reactivex.annotations.NonNull;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.net.ProxySelector;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/11/20 2:25 PM
 * version: 1.0
 * description:
 */
@SuppressWarnings({ "unused", "ResultOfMethodCallIgnored", "WeakerAccess" })
public class OkHttpClientManager {

  private static IHttpContext sContext;
  private static volatile boolean sInitialised = false;

  private static final LruCache<Integer, OkHttpClient> sOkHttpClientCache = new LruCache<Integer, OkHttpClient>(8) {
    @Override
    protected OkHttpClient create(Integer key) {
      HttpConfig config = sContext.provideHttpConfig(key);
      return createOkHttpClient(config);
    }
  };

  public static void init(@NonNull IHttpContext provider) {
    if (sInitialised) return;
    sInitialised = true;
    sContext = provider;
  }

  public static OkHttpClient getClient(int key) {
    OkHttpClient client;
    synchronized (sOkHttpClientCache) {
      client = sOkHttpClientCache.get(key);
    }
    return client;
  }

  public static OkHttpClient getDefaultClient() {
    return getClient(IHttpContext.CLIENT_DEFAULT);
  }

  private static OkHttpClient createOkHttpClient(HttpConfig config) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .readTimeout(config.readTimeout, TimeUnit.SECONDS)
        .connectTimeout(config.connectTimeout, TimeUnit.SECONDS)
        .writeTimeout(config.writeTimeout, TimeUnit.SECONDS)
        .proxySelector(ProxySelector.getDefault())
        .protocols(Collections.singletonList(Protocol.HTTP_1_1))
        .dns(new HttpDns());

    int cacheSize = 100 * 1024 * 1024; // 100 MiB
    File cacheDirectory = new File(OkRetrofitContext.getContext().getCacheDir(), "HttpCache");
    if (!cacheDirectory.isDirectory()) {
      cacheDirectory.delete();
    }
    if (!cacheDirectory.exists()) {
      cacheDirectory.mkdirs();
    }
    Cache cache = new Cache(cacheDirectory, cacheSize);
    builder.cache(cache);

    if (config.interceptors != null && config.interceptors.size() > 0) {
      for (Interceptor interceptor : config.interceptors) {
        builder.addInterceptor(interceptor);
      }
    }

    if (config.networkInterceptors != null && config.networkInterceptors.size() > 0) {
      for (Interceptor interceptor : config.networkInterceptors) {
        builder.addNetworkInterceptor(interceptor);
      }
    }
    builder.addInterceptor(new CacheInterceptor());
    builder.addInterceptor(new RefreshMobileTokenInterceptor());
    builder.addInterceptor(new HttpExceptionInterceptor());
    builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
    //Executor executor = ContextUtils.getExecutor();
    //if (null != executor && executor instanceof ExecutorService) {
    //  builder.dispatcher(new Dispatcher((ExecutorService) executor));
    //}
    return builder.build();
  }

  public static void clearCache() {
    Map<Integer, OkHttpClient> clientMap = sOkHttpClientCache.snapshot();
    for (OkHttpClient client : clientMap.values()) {
      if (null == client) {
        continue;
      }

      Cache cache = client.cache();
      if (null == cache) {
        continue;
      }

      try {
        cache.evictAll();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
