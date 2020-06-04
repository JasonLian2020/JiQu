package com.sulikeji.pipixia.home.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.sulikeji.pipixia.home.mvp.contract.HomeContract;
import com.sulikeji.pipixia.home.mvp.model.entity.CategoryListBean;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commonsdk.utils.sp.UserPreUtil;
import me.jessyan.armscomponent.commonservice.PreferencesHub;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 11:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    Gson mGson;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mApplication = null;
        this.mGson = null;
    }

    /**
     * 1.先拿缓存
     * 2.再请求网络
     */
    public void getCategoryList() {
        List<CategoryListBean> categoryList = getCategoryListByCache();
        if (categoryList != null) {
            mRootView.updateSuccess(categoryList);
            requestCategoryList(false);
        } else {
            requestCategoryList(true);
        }
    }

    private List<CategoryListBean> getCategoryListByCache() {
        List<CategoryListBean> categoryList = null;
        String json = UserPreUtil.get(PreferencesHub.HOME_KEY_CATEGORYLIST, "");
        if (!TextUtils.isEmpty(json)) {
            categoryList = mGson.fromJson(json, new TypeToken<List<CategoryListBean>>() {
            }.getType());
        }
        return categoryList;
    }

    private void saveCategoryList(List<CategoryListBean> categoryList) {
        String json = null;
        if (categoryList != null) {
            json = mGson.toJson(categoryList);
        }
        UserPreUtil.save(PreferencesHub.HOME_KEY_CATEGORYLIST, json);
    }

    private void requestCategoryList(boolean isUpdateUI) {
        mModel.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<List<CategoryListBean>>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<List<CategoryListBean>> data) {
                        if (data.isSuccess()) {
                            //更新UI
                            if (isUpdateUI) mRootView.updateSuccess(data.getData());
                            //保存缓存
                            saveCategoryList(data.getData());
                        } else {
                            mRootView.updateFail(data.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.updateFail("请求失败");
                    }
                });
    }
}
