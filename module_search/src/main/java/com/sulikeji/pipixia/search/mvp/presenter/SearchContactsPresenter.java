package com.sulikeji.pipixia.search.mvp.presenter;

import android.app.Application;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.sulikeji.pipixia.search.mvp.contract.SearchContactsContract;
import com.sulikeji.pipixia.search.mvp.model.entity.ContactsBean;
import com.sulikeji.pipixia.search.mvp.model.entity.ContactsListBean;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commoncore.ApplicationCache;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/26/2019 18:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SearchContactsPresenter extends BasePresenter<SearchContactsContract.Model, SearchContactsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    List<ContactsBean> contactsList;
    @Inject
    BaseQuickAdapter mAdapter;

    @Inject
    public SearchContactsPresenter(SearchContactsContract.Model model, SearchContactsContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mApplication = null;
        this.contactsList = null;
        this.mAdapter = null;
    }

    public void getContactsList() {
        UserBean userBean = ApplicationCache.getUserBean();
        int userId;
        if (userBean != null) {
            userId = userBean.getUserId();
        } else {
            userId = 0;
        }
        mModel.getFollowList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<ContactsListBean>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<ContactsListBean> data) {
                        if (data.isSuccess()) {
                            ContactsListBean listBean = data.getData();
                            if (listBean != null) {
                                contactsList = listBean.getList();
                                mAdapter.setNewData(contactsList);
                            } else {
                                mRootView.showMessage("数据解析异常1");
                            }
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }
}
