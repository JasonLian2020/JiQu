package com.sulikeji.pipixia.portal.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.mvp.IPresenter;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.sulikeji.pipixia.portal.R;
import com.sulikeji.pipixia.portal.app.MoreType;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentPostBean;
import com.sulikeji.pipixia.portal.mvp.ui.activity.PortalDetailActivity;
import com.sulikeji.pipixia.portal.mvp.ui.adapter.PortalCommentAdapter;
import com.sulikeji.pipixia.portal.mvp.ui.dialog.PortalInputDialog;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.jason.customdialog.CustomDialog;
import me.jason.imagepicker.ImagePicker;
import me.jason.imagepicker.IntentHub;
import me.jason.imagepicker.internal.entity.Item;
import me.jessyan.armscomponent.commoncore.ApplicationCache;
import me.jessyan.armscomponent.commoncore.DescUtils;
import me.jessyan.armscomponent.commoncore.KeyUtils;
import me.jessyan.armscomponent.commoncore.MediaUtils;
import me.jessyan.armscomponent.commoncore.ShareUtils;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ContentBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ImagePostBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.MediaBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.VideoPostBean;
import me.jessyan.armscomponent.commonservice.EventBusHub;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.easyat.FormatHelper;
import me.jessyan.armscomponent.commonui.easyat.SpanFactory;
import me.jessyan.armscomponent.commonui.mob.ShareHelper;
import me.jessyan.armscomponent.commonui.mob.ShareParams;
import me.jessyan.armscomponent.commonui.popup.DownloadPopup;
import me.jessyan.armscomponent.commonui.popup.MorePopup;
import me.jessyan.armscomponent.commonui.share.ShareOptions;
import me.jessyan.armscomponent.commonui.share.SharePopup;
import me.jessyan.armscomponent.commonui.share.SharePopupUtils;
import me.jessyan.armscomponent.commonui.share.ShareType;
import me.jessyan.armscomponent.commonui.util.ClipboardUtils;
import me.jessyan.armscomponent.commonui.util.DownloadUtils;
import me.jessyan.armscomponent.commonui.util.ToastUtils;
import timber.log.Timber;

import static com.sulikeji.pipixia.portal.mvp.ui.activity.PortalDetailActivity.REQUEST_CODE_CHECK_AT;
import static com.sulikeji.pipixia.portal.mvp.ui.activity.PortalDetailActivity.REQUEST_CODE_CHOOSE_AT;
import static com.sulikeji.pipixia.portal.mvp.ui.activity.PortalDetailActivity.REQUEST_CODE_CHOOSE_IMAGE;
import static com.sulikeji.pipixia.portal.mvp.ui.activity.PortalDetailActivity.REQUEST_CODE_CHOOSE_VIDEO;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_IMAGE;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_TEXT;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_VIDEO;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.EMPTY_COMMENT_ID;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_NORMAL;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_SUCCESS;

public abstract class PortalBaseFragment<P extends IPresenter> extends BaseFragment<P> {

    private PortalInputDialog inputDialog;

    private List<MediaBean> selectedList = new ArrayList<>();
    private int fromType;
    private String videoThumbPath;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (inputDialog != null) inputDialog.onDestory();
    }

    //=========================================================protected method
    protected PortalDetailActivity getDetailActivity() {
        return (PortalDetailActivity) getActivity();
    }

    protected void configUserAvatar(ImageView userAvatar) {
        UserBean userBean = getDetailActivity().getUserBean();
        Glide.with(mContext)
                .load(userBean == null ? "" : userBean.getUserAvatar())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(userAvatar);
    }

    protected void configUserNickname(TextView userNickname) {
        UserBean userBean = getDetailActivity().getUserBean();
        userNickname.setText(userBean == null ? "" : userBean.getUserNickname());
    }

    protected void configUserTime(TextView userTime) {
        FeedListBean item = getDetailActivity().getItem();
        userTime.setText(item == null ? "" : item.getCreateTime());
    }

    protected void configUserFollow(Button userFollow) {
        FeedListBean item = getDetailActivity().getItem();
        userFollow.setVisibility(isShowFollow() ? View.VISIBLE : View.GONE);
        userFollow.setSelected(item != null && item.getIsFollow() == STATUS_SUCCESS);
        userFollow.setText(item != null && item.getIsFollow() == STATUS_SUCCESS ? "已关注" : "关注");
        userFollow.setOnClickListener(v -> clickUserFollow());
    }

    protected void configPortalContent(TextView portalContent) {
        portalContent.setMovementMethod(LinkMovementMethod.getInstance());
        List<ContentBean> contentList = getDetailActivity().getContentList();
        if (contentList != null && !contentList.isEmpty()) {
            portalContent.setVisibility(View.VISIBLE);
            for (ContentBean contentBean : contentList) {
                if (ContentBean.TAG_AT.equals(contentBean.getTag())) {
                    ContentBean bean = new ContentBean();
                    bean.setTag(contentBean.getTag());
                    bean.setUserId(contentBean.getUserId());
                    bean.setUserName(contentBean.getUserName());
                    bean.setOnClickListener((widget, userId, userName) -> {
                        ToastUtils.normal("user = " + userId + " ,userName = " + userName);
                    });
                    portalContent.append(SpanFactory.newSpannable(bean.getSpannedName(), bean));
                } else {
                    portalContent.append(contentBean.getContent());
                }
            }
        } else {
            portalContent.setVisibility(View.GONE);
        }
    }

    protected void configRefreshLayout(SmartRefreshLayout refreshLayout) {
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(smartRefreshLayout -> getDetailActivity().getCommentList(false));
    }

    protected void configRecyclerView(RecyclerView recyclerView, BaseQuickAdapter mAdapter) {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //TODO:
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FeedListBean item = (FeedListBean) adapter.getItem(position);
            if (item == null) return;
            if (view.getId() == R.id.commentLike) {
                itemClickCommentLike(item, position);
            } else if (view.getId() == R.id.commentReply) {
                itemClickCommentReply(item, position);
            } else if (view.getId() == R.id.commentMore) {
                itemCLickCommentMore(item, position);
            }
        });
        ((PortalCommentAdapter) mAdapter).setMainUserId(getDetailActivity().getUserBean().getUserId());
        ((PortalCommentAdapter) mAdapter).setOnImageClickListener((imageView, position, imageList) -> {
            ARouter.getInstance()
                    .build(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY)
                    .withInt(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_POSITION, position)
                    .withParcelableArrayList(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_IMAGELIST, (ArrayList<ResourceBean>) imageList)
                    .withTransition(R.anim.public_preview_in_enter, R.anim.public_preview_in_exit)
                    .navigation(mContext);
        });
        mAdapter.bindToRecyclerView(recyclerView);
    }

    protected void configInputContent(TextView inputContent) {
        inputContent.setOnClickListener(v -> clickInputDialog());
    }

    protected void configInputLike(ImageView inputLike, TextView inputLikeCount) {
        FeedListBean item = getDetailActivity().getItem();
        inputLike.setSelected(item != null && item.getIsLike() == STATUS_SUCCESS);
        inputLike.setOnClickListener(v -> clickInputLike());
        inputLikeCount.setSelected(item != null && item.getIsLike() == STATUS_SUCCESS);
        inputLikeCount.setText(DescUtils.getLikeText(item != null ? item.getLikeCount() : 0));
    }

    protected void configInputComment(ImageView inputComment, TextView inputCommentCount) {
        FeedListBean item = getDetailActivity().getItem();
        inputComment.setOnClickListener(v -> clickInputComment());
        inputCommentCount.setText(DescUtils.getCommentText(item != null ? item.getCommentCount() : 0));
    }

    protected void configInputFavorite(ImageView inputFavorite) {
        FeedListBean item = getDetailActivity().getItem();
        inputFavorite.setSelected(item != null && item.getIsFavorite() == STATUS_SUCCESS);
        inputFavorite.setOnClickListener(v -> clickInputFavorite());
    }

    protected void configInputShare(ImageView inputShare) {
        inputShare.setOnClickListener(v -> clickInputShare());
    }

    protected void moveToPosition(RecyclerView recyclerView, int position) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        if (position <= firstItem) {
            recyclerView.scrollToPosition(position);
        } else if (position <= lastItem) {
            int top = recyclerView.getChildAt(position - firstItem).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            recyclerView.scrollToPosition(position);
        }
    }

    //=========================================================abstract method
    protected abstract ImageView getInputFavorite();

    protected abstract ImageView getInputLike();

    protected abstract TextView getInputLikeCount();

    protected abstract Button getUserFollow();

    protected abstract void updateDetailUI();

    public abstract BaseQuickAdapter getPortalCommentAdapter();

    public abstract void finishRefresh(boolean success, boolean emptyResponse, boolean noMoreData);

    public abstract void finishLoadMore(boolean success, boolean emptyResponse, boolean noMoreData);

    /**
     * 滚动到内容区域
     */
    public abstract void moveToContent();

    //=========================================================public method
    @Override
    public void setData(@Nullable Object data) {
        if (data instanceof Message) {
            switch (((Message) data).what) {
                case 0:
                    //更新帖子详情相关UI
                    updateDetailUI();
                    break;
                default:
                    //do something
                    break;
            }
        }
    }

    /**
     * onBackPressed的回调
     *
     * @return true表示Fragment处理返回，false表示Fragment不处理
     */
    public boolean onBackPressed() {
        return false;
    }

    public void processResultForChooseImage(Intent data) {
        ArrayList<Item> selectedItems = ImagePicker.obtainItemResult(data);
        ArrayList<String> selectedPaths = ImagePicker.obtainPathResult(data);
        ArrayList<Uri> selectedUris = ImagePicker.obtainUriResult(data);
        List<MediaBean> tempList = getSelectedList(selectedItems, selectedPaths, selectedUris);
        selectedList.addAll(tempList);
        fromType = ImagePicker.obtainFromType(data);
        //更新底部按钮
        updateChooseImage();
        updateChooseVideo();
        //赋值
        inputDialog.getAdapter().setFromImage(isFromImage());
        inputDialog.getAdapter().setNewData(selectedList);
        inputDialog.getRecyclerView().setVisibility(View.VISIBLE);
        inputDialog.getInputComment().postDelayed(this::requestFocusAndShowSoftInput, 250);
    }

    public void processResultForChooseVideo(Intent data) {
        ArrayList<Item> selectedItems = ImagePicker.obtainItemResult(data);
        ArrayList<String> selectedPaths = ImagePicker.obtainPathResult(data);
        ArrayList<Uri> selectedUris = ImagePicker.obtainUriResult(data);
        selectedList = getSelectedList(selectedItems, selectedPaths, selectedUris);
        fromType = ImagePicker.obtainFromType(data);
        //更新底部按钮
        updateChooseImage();
        updateChooseVideo();
        //检查视频信息是否完整
        checkVideoInfo();
        //赋值
        inputDialog.getAdapter().setFromImage(isFromImage());
        inputDialog.getAdapter().setNewData(selectedList);
        inputDialog.getRecyclerView().setVisibility(View.VISIBLE);
        inputDialog.getInputComment().postDelayed(this::requestFocusAndShowSoftInput, 250);
    }

    public void processResultForChooseAt(Intent data) {
        int userId = data.getIntExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERID, -1);
        String userName = data.getStringExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERNAME);
        ContentBean bean = new ContentBean();
        bean.setTag(ContentBean.TAG_AT);
        bean.setUserId(userId);
        bean.setUserName(userName);
        // 添加数据到编辑框
        inputDialog.getInputComment().getText().append(SpanFactory.newSpannable(bean.getSpannedName(), bean));
        inputDialog.getInputComment().postDelayed(this::requestFocusAndShowSoftInput, 250);
    }

    public void processResultForCheckAt(Intent data) {
        int userId = data.getIntExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERID, -1);
        String userName = data.getStringExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERNAME);
        ContentBean bean = new ContentBean();
        bean.setTag(ContentBean.TAG_AT);
        bean.setUserId(userId);
        bean.setUserName(userName);
        // 先删除@，再添加数据到编辑框
        int length = inputDialog.getInputComment().getText().length();
        inputDialog.getInputComment().getText().delete(length - 1, length);
        inputDialog.getInputComment().getText().append(SpanFactory.newSpannable(bean.getSpannedName(), bean));
        inputDialog.getInputComment().postDelayed(this::requestFocusAndShowSoftInput, 250);
    }

    //=========================================================private method
    private void clickInputDialog() {
        if (inputDialog == null) {
            inputDialog = new PortalInputDialog.Builder(getActivity())
                    .chooseImageForResult(REQUEST_CODE_CHOOSE_IMAGE)
                    .chooseVideoForResult(REQUEST_CODE_CHOOSE_VIDEO)
                    .chooseAtForResult(REQUEST_CODE_CHOOSE_AT)
                    .checkAtForResult(REQUEST_CODE_CHECK_AT)
                    .build();
            inputDialog.onCreate();
            inputDialog.setOnItemDeleteListener((adapter, view, position) -> {
                adapter.remove(position);
                //更新底部按钮
                updateChooseImage();
                updateChooseVideo();
                //如果删除最后没有数据就隐藏
                if (selectedList.isEmpty()) inputDialog.getRecyclerView().setVisibility(View.GONE);
            });
            inputDialog.setOnSendListener(v -> clickChooseSend());
            inputDialog.setOnDismissListener(dialog -> {

            });
        }
        inputDialog.showDialog();
    }

    private void clickChooseSend() {
        KeyboardUtils.hideSoftInput(getActivity());
        if (isExitResource()) {
            if (isFromImage()) {
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
        //重置UI和数据
        if (selectedList != null) selectedList.clear();
        videoThumbPath = null;
        inputDialog.resetData();
        inputDialog.dismissDialog();
    }

    private void processPublishText() {
        //===========纯文字===========
        String commentKey = KeyUtils.getCommentKey();
        //1、添加到列表
        FeedListBean listBean = new FeedListBean();
        jointResultByCustom(listBean);
        jointResultByText(listBean);
        listBean.setCommentKey(commentKey);
        listBean.setContentType(CONTENT_TYPE_TEXT);
        getPortalCommentAdapter().addData(0, listBean);
        //2、上传到服务器
        CommentPostBean bean = new CommentPostBean();
        jointResultByCustom(bean);
        jointResultByText(bean, null);
        bean.setCommentKey(commentKey);
        bean.setContentType(CONTENT_TYPE_TEXT);
        getDetailActivity().replyComment(bean);
    }

    private void processPublishImage() {
        //===========文字+图片===========
        String commentKey = KeyUtils.getCommentKey();
        //1、添加到列表
        FeedListBean listBean = new FeedListBean();
        jointResultByCustom(listBean);
        jointResultByText(listBean);
        jointResultByImage(listBean);
        listBean.setCommentKey(commentKey);
        listBean.setContentType(CONTENT_TYPE_IMAGE);
        getPortalCommentAdapter().addData(0, listBean);
        //2、上传到服务器
        //2-1.先获取七牛token
        //2-2.上传图片到七牛云
        //2-3.再通过发帖接口post到服务器
        cacheResultByText(commentKey);
        List<MediaBean> tempSelectedList = copySelectedList();
        getDetailActivity().getQiniuToken(commentKey, true, tempSelectedList, null);
    }

    private void processPublishVideo() {
        //===========文字+视频===========
        String commentKey = KeyUtils.getCommentKey();
        //1、添加到列表
        FeedListBean listBean = new FeedListBean();
        jointResultByCustom(listBean);
        jointResultByText(listBean);
        jointResultByVideo(listBean);
        listBean.setCommentKey(commentKey);
        listBean.setContentType(CONTENT_TYPE_VIDEO);
        getPortalCommentAdapter().addData(0, listBean);
        //2、上传到服务器
        //2-1.先获取七牛token
        //2-2.上传视频和缩略图到七牛云
        //2-3.再通过发帖接口post到服务器
        cacheResultByText(commentKey);
        List<MediaBean> tempSelectedList = copySelectedList();
        String tempVideoThumbPath = copyVideoThumbPath();
        getDetailActivity().getQiniuToken(commentKey, false, tempSelectedList, tempVideoThumbPath);
    }

    private List<MediaBean> copySelectedList() {
        List<MediaBean> tempSelectedList = new ArrayList<>();
        for (MediaBean bean : selectedList) {
            tempSelectedList.add(bean);
        }
        return tempSelectedList;
    }

    private String copyVideoThumbPath() {
        String tempVideoThumbPath = videoThumbPath;
        return tempVideoThumbPath;
    }

    private UserBean getUserBeanByCache() {
        me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean tempUserBean = ApplicationCache.getUserBean();
        if (tempUserBean == null) return new UserBean(0, null, null);
        return new UserBean(tempUserBean.getUserId(), tempUserBean.getUserNickname(), tempUserBean.getUserAvatar());
    }

    private String getJsonTextContent() {
        Editable editable = inputDialog.getInputComment().getText();
        return FormatHelper.editableToJson(editable, ContentBean.class);
    }

    private void clickUserFollow() {
        FeedListBean item = getDetailActivity().getItem();
        if (item == null) return;
        if (item.getIsFollow() == STATUS_SUCCESS) userCancelFollow();
        else userFollow();
    }

    private void clickInputLike() {
        FeedListBean item = getDetailActivity().getItem();
        if (item == null) return;
        if (item.getIsLike() == STATUS_SUCCESS) cancelLike();
        else like();
    }

    private void clickInputComment() {
        moveToContent();
    }

    private void clickInputFavorite() {
        FeedListBean item = getDetailActivity().getItem();
        if (item == null) return;
        if (item.getIsFavorite() == STATUS_SUCCESS) portalCancelCollect();
        else portalCollect();
    }

    private void userFollow() {
        int userId = getDetailActivity().getUserBean().getUserId();
        getDetailActivity().follow(userId);
    }

    private void userCancelFollow() {
        int userId = getDetailActivity().getUserBean().getUserId();
        getDetailActivity().cancelFollow(userId);
    }

    private void like() {
        int portalId = getDetailActivity().getPortalId();
        getDetailActivity().like(portalId);
    }

    private void cancelLike() {
        int portalId = getDetailActivity().getPortalId();
        getDetailActivity().cancelLike(portalId);
    }

    private void portalCollect() {
        int portalId = getDetailActivity().getPortalId();
        getDetailActivity().portalCollect(portalId);
    }

    private void portalCancelCollect() {
        int portalId = getDetailActivity().getPortalId();
        int favoriteId = getDetailActivity().getItem().getFavoriteId();
        getDetailActivity().portalCancelCollect(portalId, favoriteId);
    }

    private void clickInputShare() {
        FeedListBean item = getDetailActivity().getItem();
        if (item == null) return;
        SharePopup sharePopup = SharePopupUtils.getSharePopup(mContext, new ShareOptions.Builder()
                .favorite(item.getIsFavorite() == STATUS_SUCCESS)
                .downloadVideo(getDetailActivity().getContentType() == CONTENT_TYPE_VIDEO)
                .build());
        sharePopup.setOnItemClickListener((iconId, titleId, clickTag) -> {
            switch (clickTag) {
                case ShareType.SHARE_TYPE_WECHAT:
                    shareByWechat();
                    break;
                case ShareType.SHARE_TYPE_QQ:
                    shareByQQ();
                    break;
                case ShareType.SHARE_TYPE_WECHAT_MOMENTS:
                    shareByWechatMoments();
                    break;
                case ShareType.SHARE_TYPE_QZONE:
                    shareByQzone();
                    break;
                case ShareType.SHARE_TYPE_COMPLAIN:
                    break;
                case ShareType.SHARE_TYPE_FAVORITE:
                    portalCollect();
                    break;
                case ShareType.SHARE_TYPE_UNFAVORITE:
                    portalCancelCollect();
                    break;
                case ShareType.SHARE_TYPE_DOWNLOAD_IMAGE:
                    break;
                case ShareType.SHARE_TYPE_DOWNLOAD_VIDEO:
                    clickDownloadVideo();
                    break;
            }
        });
        sharePopup.showPopupWindow();
    }

    private String getUrl() {
        return "https://www.baidu.com/";
    }

    protected void shareByWechat() {
        int contentType = getDetailActivity().getContentType();
        List<ResourceBean> imageList = getDetailActivity().getImageList();
        ResourceBean videoBean = getDetailActivity().getVideoBean();
        ShareHelper.shareByWechat(
                new ShareParams.Builder()
                        .setShareType(Platform.SHARE_WEBPAGE)
                        .setTitle(ShareUtils.getShareTitle(getDetailActivity().getContentList()))
                        .setText(ShareUtils.getShareText())
                        .setImageUrl(ShareUtils.getImageUrl(contentType, imageList, videoBean))
                        .setImageData(ShareUtils.getImageData(contentType))
                        .setUrl(getUrl())
                        .build(),
                new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        //分享成功的回调
                        ToastUtils.normal("分享成功");
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        //失败的回调，platform:平台对象，i:表示当前的动作，throwable:异常信息
                        ToastUtils.normal("分享失败");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        //取消分享的回调
                        ToastUtils.normal("分享取消");
                    }
                });
    }

    protected void shareByWechatMoments() {
        int contentType = getDetailActivity().getContentType();
        List<ResourceBean> imageList = getDetailActivity().getImageList();
        ResourceBean videoBean = getDetailActivity().getVideoBean();
        ShareHelper.shareByWechatMoments(
                new ShareParams.Builder()
                        .setShareType(Platform.SHARE_WEBPAGE)
                        .setTitle(ShareUtils.getShareTitle(getDetailActivity().getContentList()))
                        .setText(ShareUtils.getShareText())
                        .setImageUrl(ShareUtils.getImageUrl(contentType, imageList, videoBean))
                        .setImageData(ShareUtils.getImageData(contentType))
                        .setUrl(getUrl())
                        .build(),
                new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        //分享成功的回调
                        ToastUtils.normal("分享成功");
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        //失败的回调，platform:平台对象，i:表示当前的动作，throwable:异常信息
                        ToastUtils.normal("分享失败");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        //取消分享的回调
                        ToastUtils.normal("分享取消");
                    }
                });
    }

    protected void shareByQQ() {
        int contentType = getDetailActivity().getContentType();
        List<ResourceBean> imageList = getDetailActivity().getImageList();
        ResourceBean videoBean = getDetailActivity().getVideoBean();
        ShareHelper.shareByQQ(
                new ShareParams.Builder()
                        .setTitle(ShareUtils.getShareTitle(getDetailActivity().getContentList()))
                        .setText(ShareUtils.getShareText())
                        .setImageUrl(ShareUtils.getImageUrl(contentType, imageList, videoBean))
                        .setTitleUrl(getUrl())
                        .build(),
                new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        //分享成功的回调
                        ToastUtils.normal("分享成功");
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        //失败的回调，platform:平台对象，i:表示当前的动作，throwable:异常信息
                        ToastUtils.normal("分享失败");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        //取消分享的回调
                        ToastUtils.normal("分享取消");
                    }
                });
    }

    protected void shareByQzone() {
        ShareHelper.shareByQzone(
                new ShareParams.Builder()
                        .setTitle(ShareUtils.getShareTitle(getDetailActivity().getContentList()))
                        .setText(ShareUtils.getShareText())
                        .setTitleUrl(getUrl())
                        .setSite(ShareUtils.getShareTitle(getDetailActivity().getContentList()))
                        .setSiteUrl(getUrl())
                        .build(),
                new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        //分享成功的回调
                        ToastUtils.normal("分享成功");
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        //失败的回调，platform:平台对象，i:表示当前的动作，throwable:异常信息
                        ToastUtils.normal("分享失败");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        //取消分享的回调
                        ToastUtils.normal("分享取消");
                    }
                });
    }

    private void requestFocusAndShowSoftInput() {
        // 获取焦点
        EditText inputComment = inputDialog.getInputComment();
        inputComment.setFocusable(true);
        inputComment.setFocusableInTouchMode(true);
        inputComment.requestFocus();
        inputComment.setSelection(inputComment.getText().length());
        KeyboardUtils.showSoftInput(inputComment);
    }

    private List<MediaBean> getSelectedList(ArrayList<Item> selectedItems, ArrayList<String> selectedPaths, ArrayList<Uri> selectedUris) {
        List<MediaBean> selectedList = null;
        if (selectedItems != null && selectedPaths != null && selectedUris != null) {
            selectedList = new ArrayList<>();
            for (int i = 0; i < selectedItems.size(); i++) {
                Item item = selectedItems.get(i);
                String path = selectedPaths.get(i);
                Uri contentUri = selectedUris.get(i);
                selectedList.add(MediaBean.valueOf(item, path, contentUri));
            }
        }
        return selectedList;
    }

    private void updateChooseImage() {
        if (isFromImage()) {
            inputDialog.getChooseImage().setEnabled(isCanChooseImage());
        } else {
            inputDialog.getChooseImage().setEnabled(selectedList == null || selectedList.isEmpty());
        }
    }

    private void updateChooseVideo() {
        if (isFromImage()) {
            inputDialog.getChooseVideo().setEnabled(selectedList == null || selectedList.isEmpty());
        } else {
            inputDialog.getChooseVideo().setEnabled(true);
        }
    }

    /**
     * 是否可以选择图片
     *
     * @return true表示可以选中，false表示不可选择
     */
    private boolean isCanChooseImage() {
        if (selectedList != null && selectedList.size() == 9) return false;
        else return true;
    }

    /**
     * 是否存在资源，图片或者视频
     */
    private boolean isExitResource() {
        if (selectedList != null && !selectedList.isEmpty()) return true;
        else return false;
    }

    private boolean isFromImage() {
        if (fromType == IntentHub.FROM_IMAGE) return true;
        else return false;
    }

    @SuppressLint("CheckResult")
    private void checkVideoInfo() {
        if (selectedList.get(0).getId() != -1) {
            //非拍照的
            processVideoInfo();
            return;
        }
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> processVideoInfo());
    }

    private void processVideoInfo() {
        MediaBean mediaBean = selectedList.get(0);
        if (mediaBean.getHeight() == 0 || mediaBean.getWidth() == 0)
            //获取视频信息
            MediaUtils.getVideoInfo(mediaBean, result -> {
                if (result == null) return;
                mediaBean.setId(result.getId());
                mediaBean.setSize(result.getSize());
                mediaBean.setDuration(result.getDuration());
                mediaBean.setWidth(result.getWidth());
                mediaBean.setHeight(result.getHeight());
            });
        //获取视频封面
        MediaUtils.getVideoThumb(mediaBean, result -> {
            videoThumbPath = result;
        });
    }

    private int getIndexByCommentKey(String commentKey) {
        int index = -1;
        List<FeedListBean> list = getPortalCommentAdapter().getData();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                FeedListBean bean = list.get(i);
                if (bean == null) continue;
                if (bean.getCommentKey().equals(commentKey)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private int getIndexByCommentId(int commentId) {
        int index = -1;
        List<FeedListBean> list = getPortalCommentAdapter().getData();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                FeedListBean bean = list.get(i);
                if (bean == null) continue;
                if (bean.getId() == commentId) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private void copyFeedListBean(FeedListBean dest, FeedListBean src) {
        src.setId(dest.getId());
        src.setObjectId(dest.getObjectId());
        src.setContentType(dest.getContentType());
        src.setCreateTime(dest.getCreateTime());
        src.setCommentCount(dest.getCommentCount());
        src.setShareCount(dest.getShareCount());
        src.setLikeCount(dest.getLikeCount());
        src.setDislikeCount(dest.getDislikeCount());
        src.setFavoriteId(dest.getFavoriteId());
        src.setIsFavorite(dest.getIsFavorite());
        src.setIsLike(dest.getIsLike());
        src.setIsFollow(dest.getIsFollow());
        src.setUserBean(dest.getUserBean());
        src.setImageList(dest.getImageList());
        src.setVideoBean(dest.getVideoBean());
    }

    //=========================================================拼接FeedListBean
    private void jointResultByCustom(FeedListBean bean) {
        bean.setId(EMPTY_COMMENT_ID);//当前发布的评论ID
        bean.setCreateTime("刚刚");
        bean.setUserBean(getUserBeanByCache());
        bean.setObjectId(getDetailActivity().getPortalId());//帖子ID
    }

    private void jointResultByText(FeedListBean bean) {
        String json = getJsonTextContent();
        bean.setContentList(TextUtils.isEmpty(json) ? null : new Gson().fromJson(json, new TypeToken<List<ContentBean>>() {
        }.getType()));//评论内容
    }

    private void jointResultByImage(FeedListBean bean) {
        List<ResourceBean> imageList = new ArrayList<>();
        for (MediaBean mediaBean : selectedList) {
            imageList.add(new ResourceBean(mediaBean.getPath(), (int) mediaBean.getWidth(), (int) mediaBean.getHeight(), null));
        }
        bean.setImageList(imageList);
    }

    private void jointResultByVideo(FeedListBean bean) {
        MediaBean mediaBean = selectedList.get(0);
        bean.setVideoBean(new ResourceBean(mediaBean.getPath(), (int) mediaBean.getWidth(), (int) mediaBean.getHeight(), videoThumbPath));
    }

    //=========================================================拼接CommentPostBean

    private Map<String, String> cacheMapByText = new HashMap<>();

    private void cacheResultByText(String commentKey) {
        String json = getJsonTextContent();
        cacheMapByText.put(commentKey, json);
    }

    private String getCacheByText(String commentKey) {
        return cacheMapByText.get(commentKey);
    }

    private boolean isExistCommentKey(String commentKey) {
        if (getCacheByText(commentKey) != null)
            return true;
        else
            return false;
    }

    private void jointResultByCustom(CommentPostBean bean) {
        bean.setPortalId(getDetailActivity().getPortalId());
    }

    private void jointResultByText(CommentPostBean bean, String commentKey) {
        String json = TextUtils.isEmpty(commentKey) ? getJsonTextContent() : getCacheByText(commentKey);
        Timber.tag("jason").d("jointResultByText：json = " + json + " ,commentKey = " + commentKey);
        bean.setContent(json);
    }

    private void jointResultByImage(CommentPostBean bean, List<ImagePostBean> successList) {
        String json = new Gson().toJson(successList);
        Timber.tag("jason").d("jointResultByImage：json = " + json);
        bean.setMore(json);
    }

    private void jointResultByVideo(CommentPostBean bean, VideoPostBean videoBean) {
        String json = new Gson().toJson(videoBean);
        Timber.tag("jason").d("jointResultByVideo：json = " + json);
        bean.setMore(json);
    }

    //=========================================================onItemClick
    private void itemClickCommentLike(FeedListBean item, int position) {
        if (item.getIsLike() == STATUS_SUCCESS) {
            getDetailActivity().commentCancelLike(item.getId(), item.getObjectId(), true);
        } else {
            getDetailActivity().commentLike(item.getId(), item.getObjectId(), true);
        }
    }

    private void itemClickCommentReply(FeedListBean item, int position) {
        ARouter.getInstance()
                .build(RouterHub.PORTAL_PORTALCOMMENTACTIVITY)
                .withInt(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_CONTENTTYPE, item.getContentType())
                .withInt(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_COMMENTID, item.getId())
                .withInt(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_PORTALID, item.getObjectId())
                .withInt(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_MASTERUSERID, item.getUserBean().getUserId())
                .withParcelable(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_ITEM, item)
                .withTransition(R.anim.popup_up_in, R.anim.popup_up_out)
                .navigation(mContext);
    }

    private void itemCLickCommentMore(FeedListBean item, int position) {
        MorePopup.Builder builder = new MorePopup.Builder(mContext);
        if (isShowCopy(item))
            builder.addClickItem(-1, R.string.portal_title_more_copy, MoreType.MORE_TYPE_COPY);
        if (isShowDelete(item))
            builder.addClickItem(-1, R.string.portal_title_more_delete, MoreType.MORE_TYPE_DELETE);
        if (isShowComplain(item))
            builder.addClickItem(-1, R.string.portal_title_more_complain, MoreType.MORE_TYPE_COMPLAIN);
        MorePopup morePopup = builder.build();
        morePopup.setOnItemClickListener((iconId, titleId, clickTag) -> {
            switch (clickTag) {
                case MoreType.MORE_TYPE_COPY:
                    TextView commentContent = (TextView) getPortalCommentAdapter().getViewByPosition(position + getPortalCommentAdapter().getHeaderLayoutCount(), R.id.commentContent);
                    ClipboardUtils.copyText(commentContent.getText());
                    ToastUtils.normal("已粘贴到剪贴板");
                    break;
                case MoreType.MORE_TYPE_DELETE:
                    new CustomDialog.Builder(mContext)
                            .title(R.string.portal_dialog_delete_comment_title)
                            .negativeText(R.string.portal_dialog_delete_comment_cancel)
                            .positiveText(R.string.portal_dialog_delete_comment_ok)
                            .onPositive((dialog, dialogAction) -> {
                                getDetailActivity().deleteComment(item.getId(), -1);
                            })
                            .show();
                    break;
                case MoreType.MORE_TYPE_COMPLAIN:
                    ToastUtils.normal("举报");
                    break;
            }
        });
        morePopup.showPopupWindow();
    }

    private int getMainUserId() {
        me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean userBean = ApplicationCache.getUserBean();
        return userBean == null ? 0 : userBean.getUserId();
    }

    private boolean isShowFollow() {
        //本人不显示关注
        UserBean userBean = getDetailActivity().getUserBean();
        if (userBean != null) {
            int mainUserId = getMainUserId();
            return userBean.getUserId() != mainUserId;
        }
        return false;
    }

    private boolean isShowCopy(FeedListBean item) {
        //有内容就显示复制
        if (item.getContentList() != null && !item.getContentList().isEmpty())
            return true;
        else
            return false;
    }

    private boolean isShowDelete(FeedListBean item) {
        //本人的评论就显示删除
        if (item.getUserBean() != null) {
            int mainUserId = getMainUserId();
            return item.getUserBean().getUserId() == mainUserId;
        }
        return false;
    }

    private boolean isShowComplain(FeedListBean item) {
        //不是本人的评论就显示举报
        if (item.getUserBean() != null) {
            int mainUserId = getMainUserId();
            return item.getUserBean().getUserId() != mainUserId;
        }
        return false;
    }

    //=========================================================download
    private void clickDownloadVideo() {
        //判断数据
        ResourceBean videoBean = getDetailActivity().getVideoBean();
        if (videoBean == null) {
            ToastUtils.normal("下载数据丢失，请重试");
            return;
        }
        String videoSavePath = DownloadUtils.getVideoSavePath(videoBean.getPath());
        if (new File(videoSavePath).exists()) {
            //======已存在
            ToastUtils.normal("已保存至DCIM/jiqu文件夹");
        } else {
            //======不存在
            //下载进度弹窗
            showDownloadDialog();
            //下载操作
            downloadVideo(videoBean.getPath());
        }
    }

    private DownloadPopup downloadPopup;

    private void showDownloadDialog() {
        downloadPopup = new DownloadPopup.Builder(mContext).build();
        downloadPopup.setOnCloseListener(v -> {
            FileDownloader.getImpl().pause(downloadTask.getId());
            downloadPopup.dismiss();
        });
        downloadPopup.showPopupWindow();
    }

    private BaseDownloadTask downloadTask;

    private void downloadVideo(String url) {
        downloadTask = DownloadUtils.downloadVideo(url, new FileDownloadSampleListener() {
            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (downloadPopup != null) {
                    int progress = (int) (soFarBytes * 1f / totalBytes * 100);
                    downloadPopup.setProgress(progress);
                }
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                if (downloadPopup != null) {
                    downloadPopup.setProgress(100);
                    downloadPopup.dismiss();
                }
                ToastUtils.normal("已保存至DCIM/jiqu文件夹");
                //通知媒体库更新文件
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(task.getTargetFilePath()))));
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if (downloadPopup != null) {
                    downloadPopup.dismiss();
                }
                ToastUtils.normal("保存失败");
            }
        });
    }

    //=========================================================eventbus
    @Subscriber(tag = EventBusHub.PORTAL_FAVORITESUCCESS, mode = ThreadMode.MAIN)
    private void favoriteSuccess(Bundle bundle) {
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int favoriteId = bundle.getInt(EventBusHub.PORTAL_KEY_FAVORITEID, -1);
        if (getDetailActivity().getPortalId() != portalId) return;
        getDetailActivity().getItem().setIsFavorite(STATUS_SUCCESS);
        getDetailActivity().getItem().setFavoriteId(favoriteId);
        configInputFavorite(getInputFavorite());
    }

    @Subscriber(tag = EventBusHub.PORTAL_CANCELFAVORITESUCCESS, mode = ThreadMode.MAIN)
    private void cancelFavoriteSuccess(Bundle bundle) {
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int favoriteId = bundle.getInt(EventBusHub.PORTAL_KEY_FAVORITEID, -1);
        if (getDetailActivity().getPortalId() != portalId) return;
        getDetailActivity().getItem().setIsFavorite(STATUS_NORMAL);
        getDetailActivity().getItem().setFavoriteId(favoriteId);
        configInputFavorite(getInputFavorite());
    }

    @Subscriber(tag = EventBusHub.PORTAL_LIKESUCCESS, mode = ThreadMode.MAIN)
    private void likeSuccess(Bundle bundle) {
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int likeCount = bundle.getInt(EventBusHub.PORTAL_KEY_LIKECOUNT, -1);
        if (getDetailActivity().getPortalId() != portalId) return;
        getDetailActivity().getItem().setIsLike(STATUS_SUCCESS);
        getDetailActivity().getItem().setLikeCount(likeCount);
        configInputLike(getInputLike(), getInputLikeCount());
    }

    @Subscriber(tag = EventBusHub.PORTAL_CANCELLIKESUCCESS, mode = ThreadMode.MAIN)
    private void cancelLikeSuccess(Bundle bundle) {
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int likeCount = bundle.getInt(EventBusHub.PORTAL_KEY_LIKECOUNT, -1);
        if (getDetailActivity().getPortalId() != portalId) return;
        getDetailActivity().getItem().setIsLike(STATUS_NORMAL);
        getDetailActivity().getItem().setLikeCount(likeCount);
        configInputLike(getInputLike(), getInputLikeCount());
    }

    @Subscriber(tag = EventBusHub.PORTAL_FOLLOWSUCCESS, mode = ThreadMode.MAIN)
    private void followSuccess(Bundle bundle) {
        int userId = bundle.getInt(EventBusHub.PORTAL_KEY_USERID, -1);
        UserBean userBean = getDetailActivity().getUserBean();
        if (userBean == null || userBean.getUserId() != userId) return;
        getDetailActivity().getItem().setIsFollow(STATUS_SUCCESS);
        configUserFollow(getUserFollow());
    }

    @Subscriber(tag = EventBusHub.PORTAL_CANCELFOLLOWSUCCESS, mode = ThreadMode.MAIN)
    private void cancelFollowSuccess(Bundle bundle) {
        int userId = bundle.getInt(EventBusHub.PORTAL_KEY_USERID, -1);
        UserBean userBean = getDetailActivity().getUserBean();
        if (userBean == null || userBean.getUserId() != userId) return;
        getDetailActivity().getItem().setIsFollow(STATUS_NORMAL);
        configUserFollow(getUserFollow());
    }

    @Subscriber(tag = EventBusHub.PORTAL_COMMENTLIKESUCCESS, mode = ThreadMode.MAIN)
    private void commentLikeSuccess(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int likeCount = bundle.getInt(EventBusHub.PORTAL_KEY_LIKECOUNT, -1);
        boolean isParentComment = bundle.getBoolean(EventBusHub.PORTAL_KEY_ISPARENTCOMMENT);
        if (getDetailActivity().getPortalId() != portalId) return;
        if (!isParentComment) return;//父级评论才处理
        int index = getIndexByCommentId(commentId);
        if (index == -1) return;
        FeedListBean bean = (FeedListBean) getPortalCommentAdapter().getItem(index);
        if (bean == null) return;
        if (bean.getId() != commentId) return;
        bean.setIsLike(STATUS_SUCCESS);
        bean.setLikeCount(likeCount);
        ImageView commentLike = (ImageView) getPortalCommentAdapter().getViewByPosition(index + getPortalCommentAdapter().getHeaderLayoutCount(), R.id.commentLike);
        TextView commentLikeCount = (TextView) getPortalCommentAdapter().getViewByPosition(index + getPortalCommentAdapter().getHeaderLayoutCount(), R.id.commentLikeCount);
        commentLike.setSelected(true);
        commentLikeCount.setSelected(true);
        commentLikeCount.setText(DescUtils.getLikeText(likeCount));
    }

    @Subscriber(tag = EventBusHub.PORTAL_COMMENTCANCELLIKESUCCESS, mode = ThreadMode.MAIN)
    private void commentCancelLikeSuccess(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int likeCount = bundle.getInt(EventBusHub.PORTAL_KEY_LIKECOUNT, -1);
        boolean isParentComment = bundle.getBoolean(EventBusHub.PORTAL_KEY_ISPARENTCOMMENT);
        if (getDetailActivity().getPortalId() != portalId) return;
        if (!isParentComment) return;//父级评论才处理
        int index = getIndexByCommentId(commentId);
        if (index == -1) return;
        FeedListBean bean = (FeedListBean) getPortalCommentAdapter().getItem(index);
        if (bean == null) return;
        if (bean.getId() != commentId) return;
        bean.setIsLike(STATUS_NORMAL);
        bean.setLikeCount(likeCount);
        ImageView commentLike = (ImageView) getPortalCommentAdapter().getViewByPosition(index + getPortalCommentAdapter().getHeaderLayoutCount(), R.id.commentLike);
        TextView commentLikeCount = (TextView) getPortalCommentAdapter().getViewByPosition(index + getPortalCommentAdapter().getHeaderLayoutCount(), R.id.commentLikeCount);
        commentLike.setSelected(false);
        commentLikeCount.setSelected(false);
        commentLikeCount.setText(DescUtils.getLikeText(likeCount));
    }

    @Subscriber(tag = EventBusHub.PORTAL_REPLYPORTALSUCCESS, mode = ThreadMode.MAIN)
    private void replyPortalSuccess(Bundle bundle) {
        //评论成功
        FeedListBean bean = bundle.getParcelable(EventBusHub.PORTAL_KEY_FEEDLISTBEAN);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        String commentKey = bundle.getString(EventBusHub.PORTAL_KEY_COMMENTKEY, null);
        if (getDetailActivity().getPortalId() != portalId) return;
        if (bean == null) return;
        //一级评论
        int index = getIndexByCommentKey(commentKey);
        if (index == -1) return;
        BaseQuickAdapter adapter = getPortalCommentAdapter();
        FeedListBean src = (FeedListBean) adapter.getData().get(index);
        copyFeedListBean(bean, src);
        adapter.notifyItemChanged(index + adapter.getHeaderLayoutCount());
    }

    @Subscriber(tag = EventBusHub.PORTAL_UPLOADIMAGESUCCESS, mode = ThreadMode.MAIN)
    private void uploadImageSuccess(Bundle bundle) {
        //上传图片到七牛成功
        String commentKey = bundle.getString(EventBusHub.PORTAL_KEY_COMMENTKEY);
        if (!isExistCommentKey(commentKey)) return;
        List<ImagePostBean> imageList = bundle.getParcelableArrayList(EventBusHub.PORTAL_KEY_IMAGELIST);
        CommentPostBean bean = new CommentPostBean();
        jointResultByCustom(bean);
        jointResultByText(bean, commentKey);//从缓存中获取文本
        jointResultByImage(bean, imageList);
        bean.setCommentKey(commentKey);
        bean.setContentType(CONTENT_TYPE_IMAGE);
        getDetailActivity().replyComment(bean);
    }

    @Subscriber(tag = EventBusHub.PORTAL_UPLOADVIDEOSUCCESS, mode = ThreadMode.MAIN)
    private void uploadVideoSuccess(Bundle bundle) {
        //上传视频到七牛成功
        String commentKey = bundle.getString(EventBusHub.PORTAL_KEY_COMMENTKEY);
        if (!isExistCommentKey(commentKey)) return;
        VideoPostBean videoBean = bundle.getParcelable(EventBusHub.PORTAL_KEY_VIDEOBEAN);
        CommentPostBean bean = new CommentPostBean();
        jointResultByCustom(bean);
        jointResultByText(bean, commentKey);//从缓存中获取文本
        jointResultByVideo(bean, videoBean);
        bean.setCommentKey(commentKey);
        bean.setContentType(CONTENT_TYPE_VIDEO);
        getDetailActivity().replyComment(bean);
    }

    @Subscriber(tag = EventBusHub.PORTAL_DELETECOMMENTSUCCESS, mode = ThreadMode.MAIN)
    private void deleteCommentSuccess(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int parentId = bundle.getInt(EventBusHub.PORTAL_KEY_PARENTID, -1);
        BaseQuickAdapter adapter = getPortalCommentAdapter();
        if (parentId == -1) {
            //删除父评论
            int index = getIndexByCommentId(commentId);
            if (index == -1) return;
            adapter.remove(index);
        } else {
            //删除子评论
            int index = getIndexByCommentId(parentId);
            if (index == -1) return;
            FeedListBean item = (FeedListBean) adapter.getData().get(index);
            item.setCommentCount(item.getCommentCount() - 1);
            adapter.notifyItemChanged(index + adapter.getHeaderLayoutCount());
        }
    }
}
