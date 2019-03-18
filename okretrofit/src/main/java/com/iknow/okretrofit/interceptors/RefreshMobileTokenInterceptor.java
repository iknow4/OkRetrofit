package com.iknow.okretrofit.interceptors;

import calibur.core.manager.UserSystem;
import calibur.core.utils.ISharedPreferencesKeys;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/12/15 12:12 AM
 * version: 1.0
 * description:
 */
public class RefreshMobileTokenInterceptor implements Interceptor {

  @Override public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    String requestUrl = request.url().encodedPath();
    //    if ("/door/current_user".equalsIgnoreCase(requestUrl)) {
//      String token = response.header(ISharedPreferencesKeys.MOBILE_TOKEN);
//      UserSystem.getInstance().updateUserToken(token);
//    }
    return chain.proceed(request);
  }
}
