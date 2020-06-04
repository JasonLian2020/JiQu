package com.sulikeji.pipixia.mine.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.scope.FragmentScope;
import com.sulikeji.pipixia.mine.R;
import com.sulikeji.pipixia.mine.mvp.contract.MineHomeContract;
import com.sulikeji.pipixia.mine.mvp.model.MineHomeModel;
import com.sulikeji.pipixia.mine.mvp.model.entity.MineBtnBean;

import java.util.ArrayList;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


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
@Module
public abstract class MineHomeModule {

    @Binds
    abstract MineHomeContract.Model bindMineHomeModel(MineHomeModel model);

    @FragmentScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(MineHomeContract.View view) {
        return new LinearLayoutManager(view.getFragment().getContext());
    }

    @FragmentScope
    @Provides
    static BaseQuickAdapter provideMineListAdapter(MineHomeContract.View view) {
        return new BaseQuickAdapter<MineBtnBean, BaseViewHolder>(R.layout.mine_item_btn, new ArrayList<>()) {
            @Override
            protected void convert(BaseViewHolder helper, MineBtnBean item) {

            }
        };
    }
}