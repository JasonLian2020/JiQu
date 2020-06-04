package com.sulikeji.pipixia.preview.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.sulikeji.pipixia.preview.di.module.PreviewImageModule;
import com.sulikeji.pipixia.preview.mvp.contract.PreviewImageContract;
import com.sulikeji.pipixia.preview.mvp.ui.activity.PreviewImageActivity;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/11/2019 10:03
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PreviewImageModule.class, dependencies = AppComponent.class)
public interface PreviewImageComponent {
    void inject(PreviewImageActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PreviewImageComponent.Builder view(PreviewImageContract.View view);

        PreviewImageComponent.Builder appComponent(AppComponent appComponent);

        PreviewImageComponent build();
    }
}