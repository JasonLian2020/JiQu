package com.sulikeji.pipixia.portal.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.FragmentScope;
import com.sulikeji.pipixia.portal.di.module.PortalImageModule;
import com.sulikeji.pipixia.portal.mvp.contract.PortalImageContract;
import com.sulikeji.pipixia.portal.mvp.ui.fragment.PortalImageFragment;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/17/2019 15:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = PortalImageModule.class, dependencies = AppComponent.class)
public interface PortalImageComponent {
    void inject(PortalImageFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PortalImageComponent.Builder view(PortalImageContract.View view);

        PortalImageComponent.Builder appComponent(AppComponent appComponent);

        PortalImageComponent build();
    }
}