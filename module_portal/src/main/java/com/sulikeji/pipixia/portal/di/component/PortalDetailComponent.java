package com.sulikeji.pipixia.portal.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.sulikeji.pipixia.portal.di.module.PortalDetailModule;
import com.sulikeji.pipixia.portal.mvp.contract.PortalDetailContract;
import com.sulikeji.pipixia.portal.mvp.ui.activity.PortalDetailActivity;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/17/2019 14:24
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PortalDetailModule.class, dependencies = AppComponent.class)
public interface PortalDetailComponent {
    void inject(PortalDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PortalDetailComponent.Builder view(PortalDetailContract.View view);

        PortalDetailComponent.Builder appComponent(AppComponent appComponent);

        PortalDetailComponent build();
    }
}