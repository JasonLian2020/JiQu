package com.sulikeji.pipixia.portal.di.module;

import com.sulikeji.pipixia.portal.mvp.contract.PortalDetailContract;
import com.sulikeji.pipixia.portal.mvp.model.PortalActivityModel;

import dagger.Binds;
import dagger.Module;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/17/2019 14:24
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class PortalDetailModule {

    @Binds
    abstract PortalDetailContract.Model bindPortalModel(PortalActivityModel model);
}