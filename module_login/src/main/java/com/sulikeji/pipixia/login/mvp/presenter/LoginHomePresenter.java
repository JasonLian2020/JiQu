package com.sulikeji.pipixia.login.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.sulikeji.pipixia.login.mvp.contract.LoginHomeContract;
import com.sulikeji.pipixia.login.mvp.model.entity.LoginInfoBean;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commoncore.ApplicationCache;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commonsdk.utils.RxUtil;
import me.jessyan.armscomponent.commonservice.EventBusHub;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;


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
@ActivityScope
public class LoginHomePresenter extends BasePresenter<LoginHomeContract.Model, LoginHomeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public LoginHomePresenter(LoginHomeContract.Model model, LoginHomeContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 获取验证码
     */
    public void getAuthCode(String phone) {
        requestGetCode(phone, false);
    }

    /**
     * 重新获取验证码
     */
    public void restartGetAuthCode(String phone) {
        requestGetCode(phone, true);
    }

    /**
     * 登录（注册）
     */
    public void login(String phone, String code) {
        requestLogin(phone, code);
    }

    private Disposable countDownDisposable;

    public void cancelCountDown() {
        if (countDownDisposable != null && !countDownDisposable.isDisposed())
            countDownDisposable.dispose();
    }

    public void startCountDown(int maxTime) {
        RxUtil.countDown(maxTime)
                .doOnSubscribe(disposable -> {
                    mRootView.updateUIByBeforeCountDown();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.tag("jason").d("onSubscribe");
                        countDownDisposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Timber.tag("jason").d("onNext integer = " + integer);
                        mRootView.updateUIByCountDown(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.tag("jason").d("onError");
                    }

                    @Override
                    public void onComplete() {
                        Timber.tag("jason").d("onComplete");
                        mRootView.updateUIByCompleteCountDown();
                    }
                });
    }

    /**
     * 请求-获取验证码
     *
     * @param phone     手机号码
     * @param isRestart 是否重新发送
     */
    private void requestGetCode(String phone, boolean isRestart) {
        mModel.getAuthCode(phone)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse data) {
                        if (data.isSuccess()) {
                            mRootView.getAuthCodeSuccess(isRestart, null);
                        } else if (phone.equals("13333333333")) {
                            //后门
                            mRootView.getAuthCodeSuccess(isRestart, null);
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    /**
     * 请求-登录（注册）
     *
     * @param userLogin 登录账号（2019.06.13暂时只有手机号）
     * @param code      验证码
     */
    private void requestLogin(String userLogin, String code) {
        mModel.login(userLogin, code)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<LoginInfoBean>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<LoginInfoBean> data) {
                        if (data.isSuccess()) {
                            if (data.getData() != null) {
                                UserBean userBean = data.getData().getUserBean();
                                String userToken = data.getData().getUserToken();
                                if (userBean != null && !TextUtils.isEmpty(userToken)) {
                                    // 保存用户信息&token
                                    ApplicationCache.saveUserBean(userBean);
                                    ApplicationCache.saveUserToken(userToken);
                                    // 通知登录成功
                                    EventBus.getDefault().post(userBean, EventBusHub.LOGIN_LOGINSUCCESS);
                                    mRootView.killMyself();
                                } else {
                                    mRootView.showMessage("数据解析异常2");
                                }
                            } else {
                                // 数据解析异常：可能后台原因，也可能客户端原因
                                mRootView.showMessage("数据解析异常1");
                            }
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }
}
