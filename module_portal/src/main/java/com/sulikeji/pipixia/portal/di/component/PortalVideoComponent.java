package com.sulikeji.pipixia.portal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.sulikeji.pipixia.portal.di.module.PortalVideoModule;
import com.sulikeji.pipixia.portal.mvp.contract.PortalVideoContract;

import com.jess.arms.di.scope.FragmentScope;
import com.sulikeji.pipixia.portal.mvp.ui.fragment.PortalVideoFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/17/2019 15:17
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = PortalVideoModule.class, dependencies = AppComponent.class)
public interface PortalVideoComponent {
    void inject(PortalVideoFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PortalVideoComponent.Builder view(PortalVideoContract.View view);

        PortalVideoComponent.Builder appComponent(AppComponent appComponent);

        PortalVideoComponent build();
    }
}