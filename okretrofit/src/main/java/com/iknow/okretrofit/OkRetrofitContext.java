package com.iknow.okretrofit;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/11/20 1:35 PM
 * version: 1.0
 * description:OkRetrofit的上下文.
 */
public class OkRetrofitContext {
  @SuppressLint("StaticFieldLeak")
  private static Context context;

  public static void setContext(Context context) {
    OkRetrofitContext.context = context;
  }

  public static  Context getContext() {
    return context;
  }
}
