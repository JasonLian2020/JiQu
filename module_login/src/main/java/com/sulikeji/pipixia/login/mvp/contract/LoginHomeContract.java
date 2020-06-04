package com.sulikeji.pipixia.login.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.sulikeji.pipixia.login.mvp.model.entity.LoginInfoBean;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;


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
public interface LoginHomeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        /**
         * 获取验证码成功
         *
         * @param isRestart 是否重新发送
         * @param message
         */
        void getAuthCodeSuccess(boolean isRestart, String message);

        /**
         * 倒计时开始前
         */
        void updateUIByBeforeCountDown();

        /**
         * 倒计时进行中
         */
        void updateUIByCountDown(int progress);

        /**
         * 倒计时完成
         */
        void updateUIByCompleteCountDown();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<PublicBaseResponse> getAuthCode(String mobile);

        /**
         * 登录（注册）
         *
         * @param userLogin 登录账号
         * @param code      验证码
         */
        Observable<PublicBaseResponse<LoginInfoBean>> login(String userLogin, String code);
    }
}
