package com.sulikeji.pipixia.publish.mvp.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.sulikeji.pipixia.publish.R;
import com.sulikeji.pipixia.publish.mvp.contract.PublishDetailContract;
import com.sulikeji.pipixia.publish.mvp.model.entity.PublishPostBean;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jason.customdialog.CustomDialog;
import me.jason.imagepicker.ImagePicker;
import me.jason.imagepicker.MimeType;
import me.jason.imagepicker.internal.entity.CaptureStrategy;
import me.jessyan.armscomponent.commoncore.KeyUtils;
import me.jessyan.armscomponent.commoncore.MediaUtils;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ImagePostBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.MediaBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.VideoPostBean;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.QiniuBean;
import me.jessyan.armscomponent.commonservice.EventBusHub;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import me.jessyan.armscomponent.commonui.engine.Glide4Engine;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import timber.log.Timber;

import static com.sulikeji.pipixia.publish.mvp.ui.activity.PublishDetailActivity.REQUEST_CODE_CHECK_AT;
import static com.sulikeji.pipixia.publish.mvp.ui.activity.PublishDetailActivity.REQUEST_CODE_CHOOSE_AT;
import static com.sulikeji.pipixia.publish.mvp.ui.activity.PublishDetailActivity.REQUEST_CODE_CHOOSE_IMAGE;
import static com.sulikeji.pipixia.publish.mvp.ui.activity.PublishDetailActivity.REQUEST_CODE_CHOOSE_VIDEO;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_IMAGE;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_TEXT;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_VIDEO;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/24/2019 17:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class PublishDetailPresenter extends BasePresenter<PublishDetailContract.Model, PublishDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    Gson mGson;
    @Inject
    BaseQuickAdapter mAdapter;

    private final UploadManager uploadManager;

    @Inject
    public PublishDetailPresenter(PublishDetailContract.Model model, PublishDetailContract.View rootView) {
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
        this.mApplication = null;
        this.mGson = null;
        this.mAdapter = null;
    }

    public void clickChooseVideo() {
        KeyboardUtils.hideSoftInput(mRootView.getActivity());
        if (mRootView.isFromImage()) {
            new CustomDialog.Builder(mRootView.getActivity())
                    .title("选择图片将会丢弃视频内容")
                    .negativeText("取消")
                    .positiveText("依然选择")
                    .onPositive((dialog, dialogAction) -> chooseVideo())
                    .show();
            return;
        }
        chooseVideo();
    }

    public void clickChooseImage() {
        KeyboardUtils.hideSoftInput(mRootView.getActivity());
        if (mRootView.isFromVideo()) {
            new CustomDialog.Builder(mRootView.getActivity())
                    .title("选择视频将会丢弃图片内容")
                    .negativeText("取消")
                    .positiveText("依然选择")
                    .onPositive((dialog, dialogAction) -> chooseImage())
                    .show();
            return;
        }
        chooseImage();
    }

    public void clickChooseAt() {
        KeyboardUtils.hideSoftInput(mRootView.getActivity());
        ARouter.getInstance()
                .build(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY)
                .withTransition(R.anim.popup_up_in, R.anim.popup_up_out)
                .navigation(mRootView.getActivity(), REQUEST_CODE_CHOOSE_AT);
    }

    public void checkChooseAt() {
        KeyboardUtils.hideSoftInput(mRootView.getActivity());
        ARouter.getInstance()
                .build(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY)
                .withTransition(R.anim.popup_up_in, R.anim.popup_up_out)
                .navigation(mRootView.getActivity(), REQUEST_CODE_CHECK_AT);
    }

    public void clickPublish() {
        KeyboardUtils.hideSoftInput(mRootView.getActivity());
        if (isExitResource()) {
            if (mRootView.isFromImage()) {
                //文字+图片
                processPublishImage();
            } else {
                //文字+视频
                processPublishVideo();
            }
        } else {
            //纯文字
            processPublishText();
        }
    }

    public void clickDeleteImage(MediaBean item, int position) {
        List<MediaBean> selectedList = mRootView.getSelectedList();
        if (selectedList.size() == 9 && !MediaBean.NONE_PATH.equals(selectedList.get(8).getPath())) {
            mAdapter.remove(position);
            mAdapter.addData(MediaBean.emptyBean());
        } else {
            mAdapter.remove(position);
        }
        // 如果删除完，剩下最后一个
        if (selectedList.size() == 1 && MediaBean.NONE_PATH.equals(selectedList.get(0).getPath())) {
            mAdapter.remove(0);
        }
        mRootView.updateChooseImage();
    }

    public void clickDeleteVideo(MediaBean item, int position) {
        //视频就直接删除
        mAdapter.remove(position);
    }

    private String getAuthority() {
        return mApplication.getPackageName() + ".imagepicker.provider";
    }

    @SuppressLint("CheckResult")
    private void chooseVideo() {
        new RxPermissions((FragmentActivity) mRootView.getActivity())
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        //授权成功
                        ImagePicker.from(mRootView.getActivity())
                                .choose(MimeType.ofVideo())
                                .capture(true)
                                .captureStrategy(new CaptureStrategy(true, getAuthority(), ""))
                                .showSingleMediaType(true)
                                .imageEngine(new Glide4Engine())
                                .forResult(REQUEST_CODE_CHOOSE_VIDEO);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //授权失败
                    } else {
                        //授权失败，不能再次询问
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void chooseImage() {
        new RxPermissions((FragmentActivity) mRootView.getActivity())
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        //授权成功
                        ImagePicker.from(mRootView.getActivity())
                                .choose(MimeType.ofImage())
                                .maxSelectable(getMaxSelectable())
                                .capture(true)
                                .captureStrategy(new CaptureStrategy(true, getAuthority(), ""))
                                .showSingleMediaType(true)
                                .imageEngine(new Glide4Engine())
                                .forResult(REQUEST_CODE_CHOOSE_IMAGE);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //授权失败
                    } else {
                        //授权失败，不能再次询问
                    }
                });
    }

    private int getMaxSelectable() {
        List<MediaBean> selectedList = mRootView.getSelectedList();
        int currentSize;
        if (selectedList == null || selectedList.isEmpty()) {
            currentSize = 0;
        } else {
            currentSize = selectedList.size() - 1;
        }
        return 9 - currentSize;
    }

    private void processPublishText() {
        //纯文字
        PublishPostBean detailBean = new PublishPostBean();
        jointResultByText(detailBean);
        detailBean.setContentType(CONTENT_TYPE_TEXT);
        requestPublish(detailBean);
    }

    private void processPublishImage() {
        //文字+图片
        //1.先获取七牛token
        //2.上传图片到七牛云
        //3.再通过发帖接口post到服务器
        mRootView.showLoading();
        requestQiniuToken();
    }

    private void processPublishVideo() {
        //TODO:文字+视频
        //1.先获取七牛token
        //2.上传视频和缩略图到七牛云
        //3.再通过发帖接口post到服务器
        mRootView.showLoading();
        requestQiniuToken();
    }

    private void jointResultByText(PublishPostBean detailBean) {
        String json = mRootView.getJsonTextContent();
        Timber.tag("jason").d("jointResultByText = " + json);
        detailBean.setContent(json);
    }

    private void jointResultByImage(PublishPostBean detailBean, List<ImagePostBean> successList) {
        String json = mGson.toJson(successList);
        Timber.tag("jason").d("jointResultByImage = " + json);
        detailBean.setMore(json);
    }

    private void jointResultByVideo(PublishPostBean detailBean, VideoPostBean videoBean) {
        String videoJson = mGson.toJson(videoBean);
        Timber.tag("jason").d("jointResultByVideo = " + videoJson);
        detailBean.setMore(videoJson);
    }

    /**
     * 是否存在资源，图片或者视频
     */
    private boolean isExitResource() {
        List<MediaBean> selectedList = mRootView.getSelectedList();
        if (selectedList != null && !selectedList.isEmpty()) return true;
        else return false;
    }

    private void requestQiniuToken() {
        mModel.getQiniuToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse<QiniuBean>>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse<QiniuBean> data) {
                        if (data.isSuccess()) {
                            if (mRootView.isFromImage()) uploadImage(data.getData());
                            else uploadVideo(data.getData());
                        } else {
                            mRootView.hideLoading();
                            mRootView.showMessage(data.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.hideLoading();
                    }
                });
    }

    private void requestPublish(PublishPostBean detailBean) {
        mModel.postPublishMessage(detailBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PublicBaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PublicBaseResponse data) {
                        if (data.isSuccess()) {
                            //通知发帖成功
                            EventBus.getDefault().post(new Object(), EventBusHub.PUBLISH_PUBLISHSUCCESS);
                            mRootView.hideLoading();
                            mRootView.killMyself();
                        } else {
                            mRootView.hideLoading();
                            mRootView.showMessage(data.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.hideLoading();
                    }
                });
    }

    private List<MediaBean> formatSelectedList() {
        List<MediaBean> tempList = new ArrayList<>();
        List<MediaBean> selectedList = mRootView.getSelectedList();
        for (MediaBean mediaBean : selectedList) {
            if (MediaBean.NONE_PATH.equals(mediaBean.getPath())) continue;
            tempList.add(mediaBean);
        }
        return tempList;
    }

    private List<ImagePostBean> successList;
    private List<ImagePostBean> failList;

    private void uploadImage(QiniuBean qiniuBean) {
        //上传图片，存在多张
        if (successList == null) successList = new ArrayList<>();
        else successList.clear();
        if (failList == null) failList = new ArrayList<>();
        else failList.clear();
        List<MediaBean> selectedList = formatSelectedList();
        for (int i = 0; i < selectedList.size(); i++) {
            MediaBean mediaBean = selectedList.get(i);
            uploadImage(selectedList, mediaBean, qiniuBean.getImageToken());
        }
    }

    private void uploadImage(final List<MediaBean> selectedList, final MediaBean mediaBean, final String imageToken) {
        uploadManager.put(new File(mediaBean.getPath()), KeyUtils.getQiniuKey(), imageToken,
                (key, info, response) -> {
                    //处理上传结果
                    ImagePostBean bean = new ImagePostBean(key, mediaBean.getWidth(), mediaBean.getHeight());
                    if (info.isOK()) {
                        successList.add(bean);
                    } else {
                        failList.add(bean);
                    }
                    uplodaImageComplete(selectedList, successList, failList);
                },
                null);
    }

    private void uplodaImageComplete(List<MediaBean> selectedList, List<ImagePostBean> successList, List<ImagePostBean> failList) {
        if (selectedList.size() != successList.size() + failList.size()) return;
        if (failList.isEmpty()) {
            //成功
            PublishPostBean detailBean = new PublishPostBean();
            jointResultByText(detailBean);
            jointResultByImage(detailBean, successList);
            detailBean.setContentType(CONTENT_TYPE_IMAGE);
            requestPublish(detailBean);
        } else {
            //失败
            mRootView.showMessage("上传图片失败");
            mRootView.hideLoading();
        }
    }

    private void uploadVideo(QiniuBean qiniuBean) {
        //上传视频，存在缩略图
        List<MediaBean> selectedList = mRootView.getSelectedList();
        MediaBean mediaBean = selectedList.get(0);
        //判断缩略图是否存在
        String videoThumbPath = mRootView.getVideoThumbPath();
        if (!TextUtils.isEmpty(videoThumbPath) && new File(videoThumbPath).exists()) {
            //存在
            processUploadViodeAndThumb(mediaBean, qiniuBean, videoThumbPath);
        } else {
            //不存在
            MediaUtils.getVideoThumb(mediaBean, result -> {
                if (TextUtils.isEmpty(result)) {
                    //获取失败
                    mRootView.showMessage("上传视频失败(0)");
                    mRootView.hideLoading();
                    return;
                }
                processUploadViodeAndThumb(mediaBean, qiniuBean, result);
            });
        }
    }

    private void processUploadViodeAndThumb(MediaBean mediaBean, QiniuBean qiniuBean, String videoThumbPath) {
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
                    uplodaVideoComplete(mediaBean, successVideoKey[0], successThumbKey[0]);
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
                    uplodaVideoComplete(mediaBean, successVideoKey[0], successThumbKey[0]);
                },
                null);
    }

    private boolean isUploadVideo = false;
    private boolean isUploadThumb = false;

    private boolean isUploadComplete() {
        return isUploadVideo && isUploadThumb;
    }

    private void uplodaVideoComplete(MediaBean mediaBean, String successVideoKey, String successThumbKey) {
        if (!isUploadComplete()) return;
        if (!TextUtils.isEmpty(successVideoKey) && !TextUtils.isEmpty(successThumbKey)) {
            //成功
            if (mediaBean.getWidth() != 0 && mediaBean.getHeight() != 0) {
                PublishPostBean detailBean = new PublishPostBean();
                VideoPostBean videoBean = new VideoPostBean();
                videoBean.setPath(successVideoKey);
                videoBean.setHeight(mediaBean.getHeight());
                videoBean.setWidth(mediaBean.getWidth());
                videoBean.setThumbnail(successThumbKey);
                jointResultByText(detailBean);
                jointResultByVideo(detailBean, videoBean);
                detailBean.setContentType(CONTENT_TYPE_VIDEO);
                requestPublish(detailBean);
            } else {
                MediaUtils.getVideoInfo(mediaBean, result -> {
                    if (result == null) {
                        mRootView.showMessage("上传视频失败(1)");
                        mRootView.hideLoading();
                        return;
                    }
                    PublishPostBean detailBean = new PublishPostBean();
                    VideoPostBean videoBean = new VideoPostBean();
                    videoBean.setPath(successVideoKey);
                    videoBean.setHeight(mediaBean.getHeight());
                    videoBean.setWidth(mediaBean.getWidth());
                    videoBean.setThumbnail(successThumbKey);
                    jointResultByText(detailBean);
                    jointResultByVideo(detailBean, videoBean);
                    detailBean.setContentType(CONTENT_TYPE_VIDEO);
                    requestPublish(detailBean);
                });
            }
        } else {
            //失败
            mRootView.showMessage("上传视频失败(2)");
            mRootView.hideLoading();
        }
    }
}
