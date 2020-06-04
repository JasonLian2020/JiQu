package com.sulikeji.pipixia.portal.di.module;

import com.sulikeji.pipixia.portal.mvp.contract.PortalCommentContract;
import com.sulikeji.pipixia.portal.mvp.model.PortalActivityModel;

import dagger.Binds;
import dagger.Module;


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
@Module
public abstract class PortalCommentModule {

    @Binds
    abstract PortalCommentContract.Model bindPortalModel(PortalActivityModel model);
}