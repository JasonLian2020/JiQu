package com.sulikeji.pipixia.portal.di.module;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.FragmentScope;
import com.sulikeji.pipixia.portal.mvp.contract.PortalVideoContract;
import com.sulikeji.pipixia.portal.mvp.model.PortalFragmentModel;
import com.sulikeji.pipixia.portal.mvp.ui.adapter.PortalCommentAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/17/2019 15:17
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class PortalVideoModule {

    @Binds
    abstract PortalVideoContract.Model bindPortalModel(PortalFragmentModel model);

    @FragmentScope
    @Provides
    static List<FeedListBean> provideCommentList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    static BaseQuickAdapter providePortalCommentAdapter(PortalVideoContract.View view, List<FeedListBean> commentList) {
        return new PortalCommentAdapter(commentList);
    }
}