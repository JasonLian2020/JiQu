package com.sulikeji.pipixia.app;

import java.io.IOException;

import me.jessyan.armscomponent.commoncore.ApplicationCache;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Authorization", ApplicationCache.getUserToken());
        return chain.proceed(builder.build());
    }
}
