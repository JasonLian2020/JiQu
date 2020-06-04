package com.sulikeji.pipixia.portal.mvp.presenter;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.sulikeji.pipixia.portal.mvp.contract.PortalDetailContract;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentListBean;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentPostBean;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commoncore.KeyUtils;
import me.jessyan.armscomponent.commoncore.MediaUtils;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ImagePostBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.MediaBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.VideoPostBean;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.QiniuBean;
import me.jessyan.armscomponent.commonservice.EventBusHub;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


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
@ActivityScope
public class PortalDetailPresenter extends BasePresenter<PortalDetailContract.Model, PortalDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    /**
     * 记录最后的页面，用于翻页
     */
    private int commentListPage;
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

    private final UploadManager uploadManager;

    @Inject
    public PortalDetailPresenter(PortalDetailContract.Model model, PortalDetailContract.View rootView) {
        super(model, rootView);

        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .zone(FixedZone.zone2)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void getCommentList(int portalId, final boolean pullToRefresh) {
        requestGetCommentlist(portalId, pullToRefresh);
    }

    public void getPortalDetail(int portalId) {
        requestGetPortalDetail(portalId);
    }

    public void portalCollect(int portalId) {
        requestPostPortalCollect(portalId);
    }

    public void portalCancelCollect(int portalId, int favoriteId) {
        requestPostPortalCancelCollect(portalId, favoriteId);
    }

    public void like(int portalId) {
        requestPostLike(portalId, STATUS_CONFRIM);
    }

    public void cancelLike(int portalId) {
        requestPostLike(portalId, STATUS_CANCEL);
    }

    public void follow(int userId) {
        requestPostFollow(userId, true);
    }

    public void cancelFollow(int userId) {
        requestPostFollow(userId, false);
    }

    public void commentLike(int commentId, int portalId, boolean isParent) {
        requestPostCommentLike(commentId, portalId, true, isParent);
    }

    public void commentCancelLike(int commentId, int portalId, boolean isParent) {
        requestPostCommentLike(commentId, portalId, false, isParent);
    }

    public void replyComment(CommentPostBean bean) {
        requestPostReplyComment(bean);
    }

    public void getQiniuToken(String commentKey, boolean isFromImage, List<MediaBean> selectedList, String videoThumbPath) {
        requestGetQiniuToken(commentKey, isFromImage, selectedList, videoThumbPath);
    }

    public void deleteComment(int commentId, int parentId) {
        requestPostDeleteComment(commentId, parentId);
    }

    private void requestGetCommentlist(int portalId, final boolean pullToRefresh) {
        if (pullToRefresh) commentListPage = 1;
        mModel.getCommentlist(portalId, commentListPage, 10)
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
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<CommentListBean>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<CommentListBean> data) {
                        int total = data.getData().getTotal();//数据库总数
                        List<FeedListBean> commentList = data.getData().getList();
                        BaseQuickAdapter adapter = mRootView.getPortalCommentAdapter();
                        if (data.isSuccess()) {
                            if (pullToRefresh) {
                                if (commentList != null && !commentList.isEmpty()) {
                                    //有更多数据
                                    success = true;
                                    emptyResponse = false;
                                    noMoreData = commentList.size() >= total;
                                    adapter.setNewData(commentList);
                                } else {
                                    //没有更多数据
                                    success = true;
                                    emptyResponse = true;
                                    noMoreData = true;
                                    adapter.setNewData(null);
                                }
                            } else {
                                if (commentList != null && !commentList.isEmpty()) {
                                    //有更多数据
                                    success = true;
                                    emptyResponse = false;
                                    noMoreData = commentList.size() + adapter.getData().size() >= total;
                                    adapter.addData(commentList);
                                } else {
                                    //没有更多数据
                                    success = true;
                                    emptyResponse = true;
                                    noMoreData = true;
                                }
                            }
                            ++commentListPage;
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

    private void requestGetPortalDetail(int portalId) {
        mModel.getPortalDetail(portalId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<FeedListBean>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<FeedListBean> data) {
                        if (data.isSuccess()) {
                            mRootView.detailResultSuccess(data.getData());
                        } else {
                            mRootView.detailResultError(data.getMessage());
                        }
                    }
                });
    }

    private void requestPostPortalCollect(int portalId) {
        mModel.postPortalCollect(portalId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<Integer>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<Integer> data) {
                        if (data.isSuccess()) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(EventBusHub.PORTAL_KEY_PORTALID, portalId);
                            bundle.putInt(EventBusHub.PORTAL_KEY_FAVORITEID, data.getData());
                            EventBus.getDefault().post(bundle, EventBusHub.PORTAL_FAVORITESUCCESS);
                            mRootView.showMessage("收藏成功");
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private void requestPostPortalCancelCollect(int portalId, int favoriteId) {
        mModel.postPortalCancelCollect(portalId, favoriteId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse data) {
                        if (data.isSuccess()) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(EventBusHub.PORTAL_KEY_PORTALID, portalId);
                            bundle.putInt(EventBusHub.PORTAL_KEY_FAVORITEID, -1);
                            EventBus.getDefault().post(bundle, EventBusHub.PORTAL_CANCELFAVORITESUCCESS);
                            mRootView.showMessage("已取消收藏");
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private static final int STATUS_CONFRIM = 0;
    private static final int STATUS_CANCEL = 1;

    private void requestPostLike(int portalId, int status) {
        mModel.postLike(portalId, 1, status)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<Integer>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<Integer> data) {
                        if (data.isSuccess()) {
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
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    /**
     * 关注/取消关注
     *
     * @param userId   用户ID
     * @param isFollow true表示已关注，false表示取消关注（未关注）
     */
    private void requestPostFollow(int userId, boolean isFollow) {
        mModel.postFollow(userId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse data) {
                        if (data.isSuccess()) {
                            if (isFollow) {
                                //已关注
                                Bundle bundle = new Bundle();
                                bundle.putInt(EventBusHub.PORTAL_KEY_USERID, userId);
                                EventBus.getDefault().post(bundle, EventBusHub.PORTAL_FOLLOWSUCCESS);
                                mRootView.showMessage("关注成功");
                            } else {
                                //取消关注
                                Bundle bundle = new Bundle();
                                bundle.putInt(EventBusHub.PORTAL_KEY_USERID, userId);
                                EventBus.getDefault().post(bundle, EventBusHub.PORTAL_CANCELFOLLOWSUCCESS);
                                mRootView.showMessage("已取消关注");
                            }
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论ID
     * @param portalId  帖子ID
     * @param isLike    true表示点赞，false表示取消点赞
     * @param isParent  true表示父级评论，false表示子级评论
     */
    private void requestPostCommentLike(int commentId, int portalId, boolean isLike, boolean isParent) {
        mModel.postCommentLike(commentId, portalId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<Integer>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<Integer> data) {
                        if (data.isSuccess()) {
                            if (isLike) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(EventBusHub.PORTAL_KEY_COMMONID, commentId);
                                bundle.putInt(EventBusHub.PORTAL_KEY_PORTALID, portalId);
                                bundle.putInt(EventBusHub.PORTAL_KEY_LIKECOUNT, data.getData());
                                bundle.putBoolean(EventBusHub.PORTAL_KEY_ISPARENTCOMMENT, isParent);
                                EventBus.getDefault().post(bundle, EventBusHub.PORTAL_COMMENTLIKESUCCESS);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(EventBusHub.PORTAL_KEY_COMMONID, commentId);
                                bundle.putInt(EventBusHub.PORTAL_KEY_PORTALID, portalId);
                                bundle.putInt(EventBusHub.PORTAL_KEY_LIKECOUNT, data.getData());
                                bundle.putBoolean(EventBusHub.PORTAL_KEY_ISPARENTCOMMENT, isParent);
                                EventBus.getDefault().post(bundle, EventBusHub.PORTAL_COMMENTCANCELLIKESUCCESS);
                            }
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private void requestPostReplyComment(CommentPostBean bean) {
        mModel.postReplyComment(bean)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<FeedListBean>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<FeedListBean> data) {
                        if (data.isSuccess()) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(EventBusHub.PORTAL_KEY_FEEDLISTBEAN, data.getData());
                            bundle.putString(EventBusHub.PORTAL_KEY_COMMENTKEY, bean.getCommentKey());
                            bundle.putInt(EventBusHub.PORTAL_KEY_PORTALID, bean.getPortalId());
                            bundle.putInt(EventBusHub.PORTAL_KEY_PARENTID, bean.getParentId());
                            bundle.putInt(EventBusHub.PORTAL_KEY_TOPARENTID, bean.getToParentId());
                            bundle.putInt(EventBusHub.PORTAL_KEY_TOUSERID, bean.getToUserId());
                            if (bean.getParentId() == -1) {
                                EventBus.getDefault().post(bundle, EventBusHub.PORTAL_REPLYPORTALSUCCESS);
                            } else {
                                EventBus.getDefault().post(bundle, EventBusHub.PORTAL_REPLYCOMMENTSUCCESS);
                            }
                            mRootView.showMessage("评论成功");
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private void requestGetQiniuToken(String commentKey, boolean isFromImage, List<MediaBean> selectedList, String videoThumbPath) {
        mModel.getQiniuToken()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<QiniuBean>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<QiniuBean> data) {
                        if (data.isSuccess()) {
                            if (isFromImage)
                                uploadImage(data.getData(), commentKey, selectedList, videoThumbPath);
                            else
                                uploadVideo(data.getData(), commentKey, selectedList, videoThumbPath);
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private void requestPostDeleteComment(int commentId, int parentId) {
        mModel.postDeleteComment(commentId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse data) {
                        if (data.isSuccess()) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(EventBusHub.PORTAL_KEY_COMMONID, commentId);
                            bundle.putInt(EventBusHub.PORTAL_KEY_PARENTID, parentId);
                            EventBus.getDefault().post(bundle, EventBusHub.PORTAL_DELETECOMMENTSUCCESS);
                            mRootView.showMessage("评论已删除");
                        } else {
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private List<ImagePostBean> successList;
    private List<ImagePostBean> failList;

    private void uploadImage(QiniuBean qiniuBean, String commentKey, List<MediaBean> selectedList, String videoThumbPath) {
        //上传图片，存在多张
        if (successList == null) successList = new ArrayList<>();
        else successList.clear();
        if (failList == null) failList = new ArrayList<>();
        else failList.clear();
        for (int i = 0; i < selectedList.size(); i++) {
            MediaBean mediaBean = selectedList.get(i);
            uploadImage(commentKey, selectedList, mediaBean, qiniuBean.getImageToken());
        }
    }

    private void uploadImage(final String commentKey, final List<MediaBean> selectedList, final MediaBean mediaBean, final String imageToken) {
        uploadManager.put(new File(mediaBean.getPath()), KeyUtils.getQiniuKey(), imageToken,
                (key, info, response) -> {
                    //处理上传结果
                    ImagePostBean bean = new ImagePostBean(key, mediaBean.getWidth(), mediaBean.getHeight());
                    if (info.isOK()) {
                        successList.add(bean);
                    } else {
                        failList.add(bean);
                    }
                    uplodaImageComplete(commentKey, selectedList, successList, failList);
                },
                null);
    }

    private void uplodaImageComplete(String commentKey, List<MediaBean> selectedList, List<ImagePostBean> successList, List<ImagePostBean> failList) {
        if (selectedList.size() != successList.size() + failList.size()) return;
        if (failList.isEmpty()) {
            //成功
            Bundle bundle = new Bundle();
            bundle.putString(EventBusHub.PORTAL_KEY_COMMENTKEY, commentKey);
            bundle.putParcelableArrayList(EventBusHub.PORTAL_KEY_IMAGELIST, (ArrayList<ImagePostBean>) successList);
            EventBus.getDefault().post(bundle, EventBusHub.PORTAL_UPLOADIMAGESUCCESS);
        } else {
            //失败
            mRootView.showMessage("上传图片失败");
        }
    }

    private void uploadVideo(QiniuBean qiniuBean, String commentKey, List<MediaBean> selectedList, String videoThumbPath) {
        //上传视频，存在缩略图
        MediaBean mediaBean = selectedList.get(0);
        //判断缩略图是否存在
        if (!TextUtils.isEmpty(videoThumbPath) && new File(videoThumbPath).exists()) {
            //存在
            processUploadViodeAndThumb(commentKey, mediaBean, qiniuBean, videoThumbPath);
        } else {
            //不存在
            MediaUtils.getVideoThumb(mediaBean, result -> {
                if (TextUtils.isEmpty(result)) {
                    //获取失败
                    mRootView.showMessage("上传视频失败(0)");
                    return;
                }
                processUploadViodeAndThumb(commentKey, mediaBean, qiniuBean, result);
            });
        }
    }

    private void processUploadViodeAndThumb(String commentKey, MediaBean mediaBean, QiniuBean qiniuBean, String videoThumbPath) {
        final String[] successVideoKey = new String[1];
        final String[] successThumbKey = new String[1];
        isUploadVideo = false;
        isUploadThumb = false;
        uploadManager.put(new File(mediaBean.getPath()), KeyUtils.getQiniuKey(), qiniuBean.getVideoToken(),
                (key, info, response) -> {
                    //处理上传结果
                    if (info.isOK()) {
                        successVideoKey[0] = key;
                    } else {
                        successVideoKey[0] = null;
                    }
                    isUploadVideo = true;
                    uplodaVideoComplete(commentKey, mediaBean, successVideoKey[0], successThumbKey[0]);
                },
                null);
        uploadManager.put(new File(videoThumbPath), KeyUtils.getQiniuKey(), qiniuBean.getImageToken(),
                (key, info, response) -> {
                    //处理上传结果
                    if (info.isOK()) {
                        successThumbKey[0] = key;
                    } else {
                        successThumbKey[0] = null;
                    }
                    isUploadThumb = true;
                    uplodaVideoComplete(commentKey, mediaBean, successVideoKey[0], successThumbKey[0]);
                },
                null);
    }

    private boolean isUploadVideo = false;
    private boolean isUploadThumb = false;

    private boolean isUploadComplete() {
        return isUploadVideo && isUploadThumb;
    }

    private void uplodaVideoComplete(String commentKey, MediaBean mediaBean, String successVideoKey, String successThumbKey) {
        if (!isUploadComplete()) return;
        if (!TextUtils.isEmpty(successVideoKey) && !TextUtils.isEmpty(successThumbKey)) {
            //成功
            if (mediaBean.getWidth() != 0 && mediaBean.getHeight() != 0) {
                VideoPostBean bean = new VideoPostBean();
                bean.setPath(successVideoKey);
                bean.setHeight(mediaBean.getHeight());
                bean.setWidth(mediaBean.getWidth());
                bean.setThumbnail(successThumbKey);
                sendResultByUploadVideo(commentKey, bean);
            } else {
                MediaUtils.getVideoInfo(mediaBean, result -> {
                    if (result == null) {
                        mRootView.showMessage("上传视频失败(1)");
                        return;
                    }
                    VideoPostBean bean = new VideoPostBean();
                    bean.setPath(successVideoKey);
                    bean.setHeight(mediaBean.getHeight());
                    bean.setWidth(mediaBean.getWidth());
                    bean.setThumbnail(successThumbKey);
                    sendResultByUploadVideo(commentKey, bean);
                });
            }
        } else {
            //失败
            mRootView.showMessage("上传视频失败(2)");
        }
    }

    private void sendResultByUploadVideo(String commentKey, VideoPostBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString(EventBusHub.PORTAL_KEY_COMMENTKEY, commentKey);
        bundle.putParcelable(EventBusHub.PORTAL_KEY_VIDEOBEAN, bean);
        EventBus.getDefault().post(bundle, EventBusHub.PORTAL_UPLOADVIDEOSUCCESS);
    }
}
