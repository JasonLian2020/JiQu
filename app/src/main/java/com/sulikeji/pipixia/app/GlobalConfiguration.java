/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sulikeji.pipixia.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.integration.ConfigModule;
import com.jess.arms.utils.ArmsUtils;
import com.squareup.leakcanary.RefWatcher;
import com.sulikeji.pipixia.BuildConfig;
import com.sulikeji.pipixia.mvp.model.api.Api;

import java.io.IOException;
import java.util.List;

import me.jessyan.armscomponent.commonsdk.CommonSdkBuildConfig;
import me.jessyan.armscomponent.commonsdk.core.GlobalHttpHandlerImpl;
import me.jessyan.armscomponent.commonsdk.http.SSLSocketClient;
import me.jessyan.armscomponent.commonsdk.imgaEngine.Strategy.CommonGlideImageLoaderStrategy;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;


/**
 * ================================================
 * 组件的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * CommonSDK 中已有 {@link me.jessyan.armscomponent.commonsdk.core.GlobalConfiguration} 配置有组件可公用的配置信息
 * 这里用来配置一些组件自身私有的配置信息
 *
 * @see com.jess.arms.base.delegate.AppDelegate
 * @see com.jess.arms.integration.ManifestParser
 * Created by JessYan on 12/04/2017 17:25
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public final class GlobalConfiguration implements ConfigModule {
    static {
        CommonSdkBuildConfig.LOG_DEBUG = BuildConfig.LOG_DEBUG;
        CommonSdkBuildConfig.USE_CANARY = BuildConfig.USE_CANARY;
    }

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.baseurl(Api.APP_DOMAIN)
                .addInterceptor(new TokenInterceptor())
                .imageLoaderStrategy(new CommonGlideImageLoaderStrategy())
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))
                .responseErrorListener(new ResponseErrorListenerImpl())
                .gsonConfiguration((context1, gsonBuilder) -> {
                    gsonBuilder.registerTypeAdapter(Integer.class, new TypeAdapter<Integer>() {
                        @Override
                        public void write(JsonWriter out, Integer value) throws IOException {
                            out.value(String.valueOf(value));
                        }

                        @Override
                        public Integer read(JsonReader in) throws IOException {
                            if (in.peek() == JsonToken.NULL) {
                                in.nextNull();
                                return 0;
                            }
                            String nextString = in.nextString();
                            if (TextUtils.isEmpty(nextString)) return 0;
                            try {
                                return Integer.valueOf(nextString);
                            } catch (NumberFormatException e1) {
                                try {
                                    return (int) (float) Float.valueOf(nextString);
                                } catch (NumberFormatException e2) {
                                    return 0;
                                }
                            }
                        }
                    });
                    gsonBuilder.setLenient()
                            .serializeNulls()//支持序列化null的参数
                            .enableComplexMapKeySerialization();//支持将序列化key为object的map,默认只能序列化key为string的map
                })
                .okhttpConfiguration((context1, okHttpBuilder) -> {
                    okHttpBuilder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getTrustManager());
                    okHttpBuilder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
                    //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
                    RetrofitUrlManager.getInstance().with(okHttpBuilder);
                })
                .rxCacheConfiguration((context1, rxCacheBuilder) -> {//这里可以自己自定义配置RxCache的参数
                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                    return null;
                });
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                ((RefWatcher) ArmsUtils
                        .obtainAppComponentFromContext(f.getActivity())
                        .extras()
                        .get(RefWatcher.class.getName()))
                        .watch(f);
            }
        });
    }

}
