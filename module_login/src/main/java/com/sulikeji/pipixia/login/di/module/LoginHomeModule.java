package com.sulikeji.pipixia.login.di.module;

import android.app.Dialog;

import com.jess.arms.di.scope.ActivityScope;
import com.sulikeji.pipixia.login.mvp.contract.LoginHomeContract;
import com.sulikeji.pipixia.login.mvp.model.LoginHomeModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.armscomponent.commonres.dialog.ProgresDialog;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 16:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class LoginHomeModule {

    @Binds
    abstract LoginHomeContract.Model bindLoginHomeModel(LoginHomeModel model);

    @ActivityScope
    @Provides
    static Dialog provideDialog(LoginHomeContract.View view) {
        ProgresDialog progresDialog = new ProgresDialog(view.getActivity());
        progresDialog.setCancelable(false);
        progresDialog.setCanceledOnTouchOutside(false);
        return progresDialog;
    }
}