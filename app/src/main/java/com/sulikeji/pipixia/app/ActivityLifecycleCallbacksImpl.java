package com.sulikeji.pipixia.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.utils.ArmsUtils;

import timber.log.Timber;

public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {
    private static final String EXTRA_IS_INIT_TITLEBAR = "isInitTitlebar";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Timber.tag("UI").i(activity + " - onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Timber.tag("UI").i(activity + " - onActivityStarted");
        if (!activity.getIntent().getBooleanExtra(EXTRA_IS_INIT_TITLEBAR, false)) {
            //由于加强框架的兼容性,故将 setContentView 放到 onActivityCreated 之后,onActivityStarted 之前执行
            //而 findViewById 必须在 Activity setContentView() 后才有效,所以将以下代码从之前的 onActivityCreated 中移动到 onActivityStarted 中执行
            activity.getIntent().putExtra(EXTRA_IS_INIT_TITLEBAR, true);
            //这里全局给Activity设置toolbar和title,你想象力有多丰富,这里就有多强大,以前放到BaseActivity的操作都可以放到这里
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_titlebar") != null) {
                //左边按钮：返回或者关闭
                View publicTitlebarLeft = ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_titlebar_left");
                if (publicTitlebarLeft != null) {
                    publicTitlebarLeft.setOnClickListener(v -> activity.onBackPressed());
                }
                //中间标题
                TextView publicTitlebarTitle = ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_titlebar_title");
                if (publicTitlebarTitle != null && !TextUtils.isEmpty(activity.getTitle())) {
                    publicTitlebarTitle.setText(activity.getTitle());
                }
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Timber.tag("UI").i(activity + " - onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Timber.tag("UI").i(activity + " - onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Timber.tag("UI").i(activity + " - onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Timber.tag("UI").i(activity + " - onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Timber.tag("UI").i(activity + " - onActivityDestroyed");
        //横竖屏切换或配置改变时, Activity 会被重新创建实例, 但 Bundle 中的基础数据会被保存下来,移除该数据是为了保证重新创建的实例可以正常工作
        activity.getIntent().removeExtra(EXTRA_IS_INIT_TITLEBAR);
    }
}
