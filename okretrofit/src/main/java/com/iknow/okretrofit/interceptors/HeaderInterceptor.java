package com.iknow.okretrofit.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/11/20 10:42 AM
 * version: 1.0
 * description:
 */
public class HeaderInterceptor implements Interceptor {

  @Override public Response intercept(Chain chain) throws IOException{
    Request.Builder requestBuilder = chain.request().newBuilder();
    Request request = requestBuilder.build();
    return chain.proceed(request);
  }
}
