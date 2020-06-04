package com.sulikeji.pipixia.search.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.sulikeji.pipixia.search.mvp.contract.SearchContactsContract;
import com.sulikeji.pipixia.search.mvp.model.SearchModel;
import com.sulikeji.pipixia.search.mvp.model.entity.ContactsBean;
import com.sulikeji.pipixia.search.mvp.ui.adapter.SearchContactsAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


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
@Module
public abstract class SearchContactsModule {

    @Binds
    abstract SearchContactsContract.Model bindSearchModel(SearchModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(SearchContactsContract.View view) {
        return new LinearLayoutManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static List<ContactsBean> provideContactsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static BaseQuickAdapter provideSearchContactsAdapter(SearchContactsContract.View view, List<ContactsBean> contactsList) {
        return new SearchContactsAdapter(contactsList);
    }
}