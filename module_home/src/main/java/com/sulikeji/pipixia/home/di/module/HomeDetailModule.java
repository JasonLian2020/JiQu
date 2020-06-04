package com.sulikeji.pipixia.home.di.module;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.scope.FragmentScope;
import com.sulikeji.pipixia.home.mvp.contract.HomeDetailContract;
import com.sulikeji.pipixia.home.mvp.model.HomeModel;
import com.sulikeji.pipixia.home.mvp.ui.adapter.HomeListAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commonres.dialog.ProgresDialog;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 11:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class HomeDetailModule {

    @Binds
    abstract HomeDetailContract.Model bindHomeModel(HomeModel model);

    @FragmentScope
    @Provides
    static Dialog provideDialog(HomeDetailContract.View view) {
        ProgresDialog progresDialog = new ProgresDialog(view.getFragment().getContext());
        progresDialog.setCancelable(false);
        progresDialog.setCanceledOnTouchOutside(false);
        return progresDialog;
    }

    @FragmentScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(HomeDetailContract.View view) {
        return new LinearLayoutManager(view.getFragment().getContext());
    }

    @FragmentScope
    @Provides
    static List<FeedListBean> provideHomeList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    static BaseQuickAdapter<FeedListBean, BaseViewHolder> provideHomeListAdapter(HomeDetailContract.View view, List<FeedListBean> list) {
        return new HomeListAdapter(list);
    }
}