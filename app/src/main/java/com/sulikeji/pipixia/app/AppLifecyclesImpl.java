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
import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;

import com.blankj.utilcode.util.Utils;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.utils.ArmsUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.services.ForegroundServiceConfig;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.mob.MobSDK;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.sulikeji.pipixia.BuildConfig;
import com.sulikeji.pipixia.R;
import com.tencent.bugly.crashreport.CrashReport;

import java.net.Proxy;

import es.dmoral.toasty.Toasty;
import me.jessyan.autosize.AutoSizeConfig;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;


/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {
        MultiDex.install(base);  //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
    }

    @Override
    public void onCreate(@NonNull Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        //初始化全局可用的Applicaiton
        Utils.init(application);
        //leakCanary内存泄露检查
        ArmsUtils.obtainAppComponentFromContext(application).extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        //bugly
        CrashReport.initCrashReport(application, "4b36ddf083", true);
        CrashReport.setAppVersion(application, BuildConfig.VERSION_NAME);
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new BezierCircleHeader(context));
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(0xff11bbff));
        //Toasty
        Toasty.Config.getInstance()
                .allowQueue(false)
                .apply();
        //GSYVideoPlayer
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);//EXO模式
        if (BuildConfig.DEBUG) Debuger.enable();//日志打开
        else Debuger.disable();
        //AndroidAutoSize
        AutoSizeConfig.getInstance().setExcludeFontScale(true);//是否屏蔽系统字体大小对 AndroidAutoSize 的影响
        //MobSDK
        MobSDK.init(application);
        //FileDownloader
        initFileDownloader(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    private void initFileDownloader(@NonNull Application application) {
        FileDownloadLog.NEED_LOG = BuildConfig.DEBUG;
        final String channelId = "Test";
        Notification notification = new NotificationCompat.Builder(application, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Downloading")
                .setContentTitle("FileDownloader Demo")
                .build();
        ForegroundServiceConfig config = new ForegroundServiceConfig.Builder()
                .notification(notification)
                .notificationChannelId(channelId)
                .notificationChannelName("name")
                .needRecreateChannelId(true) // if your channel id is created before, you can ignore this configuration and you don't need to provide channel id and channel name
                .notificationId(R.mipmap.ic_launcher)
                .build();
        FileDownloader.setupOnApplicationOnCreate(application)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                ))
                .foregroundServiceConfig(config)
                .commit();
    }
}
