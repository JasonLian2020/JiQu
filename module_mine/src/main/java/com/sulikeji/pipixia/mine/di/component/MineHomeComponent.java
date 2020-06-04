package com.sulikeji.pipixia.mine.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.FragmentScope;
import com.sulikeji.pipixia.mine.di.module.MineHomeModule;
import com.sulikeji.pipixia.mine.mvp.contract.MineHomeContract;
import com.sulikeji.pipixia.mine.mvp.ui.fragment.MineHomeFragment;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/15/2019 17:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = MineHomeModule.class, dependencies = AppComponent.class)
public interface MineHomeComponent {
    void inject(MineHomeFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MineHomeComponent.Builder view(MineHomeContract.View view);

        MineHomeComponent.Builder appComponent(AppComponent appComponent);

        MineHomeComponent build();
    }
}