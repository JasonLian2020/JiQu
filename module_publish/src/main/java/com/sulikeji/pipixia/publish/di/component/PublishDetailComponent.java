package com.sulikeji.pipixia.publish.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.sulikeji.pipixia.publish.di.module.PublishDetailModule;
import com.sulikeji.pipixia.publish.mvp.contract.PublishDetailContract;
import com.sulikeji.pipixia.publish.mvp.ui.activity.PublishDetailActivity;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/24/2019 17:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PublishDetailModule.class, dependencies = AppComponent.class)
public interface PublishDetailComponent {
    void inject(PublishDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PublishDetailComponent.Builder view(PublishDetailContract.View view);

        PublishDetailComponent.Builder appComponent(AppComponent appComponent);

        PublishDetailComponent build();
    }
}