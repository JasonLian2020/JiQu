package com.sulikeji.pipixia.publish.di.module;

import android.app.Dialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.sulikeji.pipixia.publish.mvp.contract.PublishDetailContract;
import com.sulikeji.pipixia.publish.mvp.model.PublishModel;
import com.sulikeji.pipixia.publish.mvp.ui.adapter.PublishDetailAdapter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.armscomponent.commonres.dialog.ProgresDialog;


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
@Module
public abstract class PublishDetailModule {

    @Binds
    abstract PublishDetailContract.Model bindPublishModel(PublishModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(PublishDetailContract.View view) {
        return new GridLayoutManager(view.getActivity(), view.getSpanCount());
    }

    @ActivityScope
    @Provides
    static BaseQuickAdapter providePublishDetailAdapter(PublishDetailContract.View view) {
        return new PublishDetailAdapter(null);
    }

    @ActivityScope
    @Provides
    static Dialog provideDialog(PublishDetailContract.View view) {
        ProgresDialog progresDialog = new ProgresDialog(view.getActivity());
        progresDialog.setCancelable(false);
        progresDialog.setCanceledOnTouchOutside(false);
        return progresDialog;
    }
}