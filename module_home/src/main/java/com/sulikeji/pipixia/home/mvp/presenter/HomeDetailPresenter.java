package com.sulikeji.pipixia.home.mvp.presenter;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.sulikeji.pipixia.home.mvp.contract.HomeDetailContract;
import com.sulikeji.pipixia.home.mvp.model.entity.FeedBean;

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commonservice.EventBusHub;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_FAIL;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_NORMAL;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 11:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class HomeDetailPresenter extends BasePresenter<HomeDetailContract.Model, HomeDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    AppManager mAppManager;
    @Inject
    BaseQuickAdapter<FeedListBean, BaseViewHolder> mAdapter;
    @Inject
    List<FeedListBean> mList;

    private int preListSize;
    /**
     * 记录最后一条帖子的id，用于翻页
     */
    private int lastId;
    /**
     * 是否请求成功，用于刷新更新时间
     */
    private boolean success;
    /**
     * 是否返回空数据
     */
    private boolean emptyResponse;
    /**
     * 是否有更多数据[true表示没有更多数据，false表示有更多数据]
     */
    private boolean noMoreData;

    @Inject
    public HomeDetailPresenter(HomeDetailContract.Model model, HomeDetailContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
        this.mAdapter = null;
        this.mList = null;
    }

    private static final int TYPE_LIKE = 1;
    private static final int TYPE_DISLIKE = 2;

    private static final int STATUS_CONFRIM = 0;
    private static final int STATUS_CANCEL = 1;

    public void requestDatas(final boolean pullToRefresh) {
        requestFeedList(pullToRefresh);
    }

    public void confirmLike(FeedListBean item, int position) {
        requestPostLike(item.getId(), TYPE_LIKE, STATUS_CONFRIM, item, position);
    }

    public void cancelLike(FeedListBean item, int position) {
        requestPostLike(item.getId(), TYPE_LIKE, STATUS_CANCEL, item, position);
    }

    public void confirmDislike(FeedListBean item, int position) {
        requestPostLike(item.getId(), TYPE_DISLIKE, STATUS_CONFRIM, item, position);
    }

    public void cancelDislike(FeedListBean item, int position) {
        requestPostLike(item.getId(), TYPE_DISLIKE, STATUS_CANCEL, item, position);
    }

    public void showFeedback(FeedListBean item, int position, View view) {
        mRootView.showFeedbackPopup(item, position, view);
    }

    public void feedback(int portalId, int type) {
        requestPostFeedback(portalId, type);
    }

    public void collectPortal(FeedListBean item) {
        requestPostCollectPortal(item);
    }

    public void cancelCollectPortal(FeedListBean item) {
        requestPostCancelCollectPortal(item);
    }

    private void requestFeedList(final boolean pullToRefresh) {
        if (pullToRefresh) lastId = 0;
        mModel.getFeedList(20, mRootView.getCategoryId(), lastId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.finishRefresh(success, emptyResponse, noMoreData);//下拉刷新
                    else
                        mRootView.finishLoadMore(success, emptyResponse, noMoreData);//上拉加载更多
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<FeedBean>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<FeedBean> data) {
                        if (data.isSuccess()) {
                            //当前返回的帖子+剩余的帖子
                            int responseAndSurplusSize = data.getData().getTotal();
                            int pageSize = data.getData().getSize();
                            List<FeedListBean> feedList = data.getData().getFeedList();
                            if (pullToRefresh) {
                                if (feedList != null && !feedList.isEmpty()) {
                                    //有更多数据
                                    success = true;
                                    emptyResponse = false;
                                    noMoreData = responseAndSurplusSize <= pageSize;
                                    lastId = feedList.get(feedList.size() - 1).getId();
                                    mList.clear();
                                    mList.addAll(feedList);
                                    mAdapter.setNewData(mList);
                                } else {
                                    //没有更多数据
                                    success = true;
                                    emptyResponse = true;
                                    noMoreData = true;
                                    mList.clear();
                                    mAdapter.setNewData(mList);
                                }
                            } else {
                                if (feedList != null && !feedList.isEmpty()) {
                                    //有更多数据
                                    success = true;
                                    emptyResponse = false;
                                    noMoreData = responseAndSurplusSize <= pageSize;
                                    lastId = feedList.get(feedList.size() - 1).getId();
                                    preListSize = mList.size();
                                    mList.addAll(feedList);
                                    mAdapter.notifyItemRangeInserted(preListSize, feedList.size());
                                } else {
                                    //没有更多数据
                                    success = true;
                                    emptyResponse = true;
                                    noMoreData = true;
                                }
                            }
                        } else {
                            success = false;
                            emptyResponse = false;
                            noMoreData = false;
                            mRootView.showMessage(data.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        //刷新失败
                        success = false;
                        emptyResponse = false;
                        noMoreData = false;
                    }
                });
    }

    private void requestPostLike(int portalId, int type, int status, FeedListBean item, int position) {
        mModel.postLike(portalId, type, status)
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
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<Integer>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<Integer> data) {
                        if (data.isSuccess()) {
                            if (type == TYPE_LIKE) {
                                //点赞操作
                                if (status == STATUS_CONFRIM) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(EventBusHub.PORTAL_KEY_PORTALID, portalId);
                                    bundle.putInt(EventBusHub.PORTAL_KEY_LIKECOUNT, data.getData());
                                    EventBus.getDefault().post(bundle, EventBusHub.PORTAL_LIKESUCCESS);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(EventBusHub.PORTAL_KEY_PORTALID, portalId);
                                    bundle.putInt(EventBusHub.PORTAL_KEY_LIKECOUNT, data.getData());
                                    EventBus.getDefault().post(bundle, EventBusHub.PORTAL_CANCELLIKESUCCESS);
                                }
                            } else {
                                //踩操作
                                if (status == STATUS_CONFRIM) {
                                    item.setIsLike(STATUS_FAIL);
                                    item.setLikeCount(data.getData());
                                } else {
                                    item.setIsLike(STATUS_NORMAL);
                                    item.setLikeCount(data.getData());
                                }
                                mAdapter.notifyItemChanged(position);
                            }
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private void requestPostFeedback(int portalId, int type) {
        mModel.postFeedback(portalId, type)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse data) {
                        if (data.isSuccess()) {
                            mRootView.showMessage("将减少推荐类似内容");
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private void requestPostCollectPortal(FeedListBean item) {
        mModel.postCollectPortal(item.getId())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<Integer>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<Integer> data) {
                        if (data.isSuccess()) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(EventBusHub.PORTAL_KEY_PORTALID, item.getId());
                            bundle.putInt(EventBusHub.PORTAL_KEY_FAVORITEID, data.getData());
                            EventBus.getDefault().post(bundle, EventBusHub.PORTAL_FAVORITESUCCESS);
                            mRootView.showMessage("收藏成功");
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private void requestPostCancelCollectPortal(FeedListBean item) {
        mModel.postCancelCollectPortal(item.getId(), item.getFavoriteId())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse data) {
                        if (data.isSuccess()) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(EventBusHub.PORTAL_KEY_PORTALID, item.getId());
                            bundle.putInt(EventBusHub.PORTAL_KEY_FAVORITEID, -1);
                            EventBus.getDefault().post(bundle, EventBusHub.PORTAL_CANCELFAVORITESUCCESS);
                            mRootView.showMessage("已取消收藏");
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }
}
