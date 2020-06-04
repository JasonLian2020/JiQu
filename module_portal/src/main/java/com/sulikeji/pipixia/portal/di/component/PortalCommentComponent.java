package com.sulikeji.pipixia.portal.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.sulikeji.pipixia.portal.di.module.PortalCommentModule;
import com.sulikeji.pipixia.portal.mvp.contract.PortalCommentContract;
import com.sulikeji.pipixia.portal.mvp.ui.activity.PortalCommentActivity;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/14/2019 16:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PortalCommentModule.class, dependencies = AppComponent.class)
public interface PortalCommentComponent {
    void inject(PortalCommentActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PortalCommentComponent.Builder view(PortalCommentContract.View view);

        PortalCommentComponent.Builder appComponent(AppComponent appComponent);

        PortalCommentComponent build();
    }
}