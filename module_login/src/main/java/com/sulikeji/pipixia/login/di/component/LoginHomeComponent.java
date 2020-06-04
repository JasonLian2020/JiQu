package com.sulikeji.pipixia.login.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.sulikeji.pipixia.login.di.module.LoginHomeModule;
import com.sulikeji.pipixia.login.mvp.contract.LoginHomeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.sulikeji.pipixia.login.mvp.ui.activity.LoginHomeActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 16:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = LoginHomeModule.class, dependencies = AppComponent.class)
public interface LoginHomeComponent {
    void inject(LoginHomeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LoginHomeComponent.Builder view(LoginHomeContract.View view);

        LoginHomeComponent.Builder appComponent(AppComponent appComponent);

        LoginHomeComponent build();
    }
}