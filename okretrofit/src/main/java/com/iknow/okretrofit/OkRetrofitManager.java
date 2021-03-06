package com.iknow.okretrofit;

import com.iknow.okretrofit.interceptors.HeaderInterceptor;
import com.iknow.okretrofit.utils.JSONUtil;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Hashtable;

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/11/20 10:29 AM
 * version: 1.0
 * description:可构建多个service并支持缓存
 */
@SuppressWarnings({ "unchecked", "WeakerAccess", "unused" })
public class OkRetrofitManager {

  private static OkHttpClient mOkHttpClient;
  private Converter.Factory mGlobalConverterFactory;
  private static String BASE_URL = NetworkHost.PRO_HOST;
  private Hashtable<Integer, Object> mCache = new Hashtable<>();

  private static final class SingletonHolder {
    private static OkRetrofitManager INSTANCE = new OkRetrofitManager();
  }

  private OkRetrofitManager() {
  }

  public static OkRetrofitManager getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public void init() {
    init(null);
  }

  public void init(Converter.Factory factory) {
    OkHttpClient client = OkHttpClientManager.getDefaultClient();
    OkHttpClient.Builder httpBuilder = client.newBuilder();
    httpBuilder.interceptors().add(new HeaderInterceptor());
    mOkHttpClient = httpBuilder.build();
    mGlobalConverterFactory = factory;
    if(mGlobalConverterFactory == null)
      mGlobalConverterFactory = GsonConverterFactory.create(JSONUtil.INSTANCE.getDefaultGson());
  }


  /**
   * @param type 接口类型
   */
  public <T> T getService(Class<T> type) {
    return getService(type, Schedulers.io());
  }

  /**
   * @param type 接口类型
   * @param scheduler 提交线程
   */
  public <T> T getService(Class<T> type, Scheduler scheduler) {
    return getService(type, scheduler, BASE_URL);
  }

  /**
   * @param type 接口类型
   * @param scheduler 提交线程
   * @param host 指定的域名
   */
  public <T> T getService(Class<T> type, Scheduler scheduler, String host) {
    return getService(type, scheduler, host, null);
  }

  /**
   * @param type 接口类型
   * @param scheduler 提交线程
   * @param host 指定的域名
   * @param converter json序列化实现
   */
  public <T> T getService(Class<T> type, Scheduler scheduler, String host, Converter.Factory converter) {
    int key = generateKey(type, host, converter);
    Object service = mCache.get(key);
    if (service != null) {
      return (T) service;
    }
    Retrofit.Builder builder = new Retrofit.Builder();
    String url = host == null ? BASE_URL : host;
    if(mOkHttpClient == null) init();
    Retrofit retrofit = builder.client(mOkHttpClient)
        .baseUrl(url)
        .addConverterFactory(mGlobalConverterFactory)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    service = retrofit.create(type);
    mCache.put(key, service);
    return (T) service;
  }

  private static <T> int generateKey(Class<T> type, String host, Converter.Factory converter) {
    return (type.getName() + (host == null ? "" : host) + (converter == null ? "" : converter.getClass().getName()))
        .hashCode();
  }
}
