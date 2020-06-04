package com.sulikeji.pipixia.search.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.sulikeji.pipixia.search.di.module.SearchContactsModule;
import com.sulikeji.pipixia.search.mvp.contract.SearchContactsContract;
import com.sulikeji.pipixia.search.mvp.ui.activity.SearchContactsActivity;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/26/2019 18:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SearchContactsModule.class, dependencies = AppComponent.class)
public interface SearchContactsComponent {
    void inject(SearchContactsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SearchContactsComponent.Builder view(SearchContactsContract.View view);

        SearchContactsComponent.Builder appComponent(AppComponent appComponent);

        SearchContactsComponent build();
    }
}