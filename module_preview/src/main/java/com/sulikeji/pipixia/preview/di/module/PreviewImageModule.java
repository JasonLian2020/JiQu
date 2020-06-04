package com.sulikeji.pipixia.preview.di.module;

import com.sulikeji.pipixia.preview.mvp.contract.PreviewImageContract;
import com.sulikeji.pipixia.preview.mvp.model.PreviewModel;

import dagger.Binds;
import dagger.Module;


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
@Module
public abstract class PreviewImageModule {

    @Binds
    abstract PreviewImageContract.Model bindPreviewModel(PreviewModel model);
}