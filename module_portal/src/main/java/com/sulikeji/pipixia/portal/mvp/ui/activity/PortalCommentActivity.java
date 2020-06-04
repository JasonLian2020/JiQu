package com.sulikeji.pipixia.portal.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeWrapper;
import com.billy.android.swipe.SwipeConsumer;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.sulikeji.pipixia.portal.R;
import com.sulikeji.pipixia.portal.R2;
import com.sulikeji.pipixia.portal.app.MoreType;
import com.sulikeji.pipixia.portal.di.component.DaggerPortalCommentComponent;
import com.sulikeji.pipixia.portal.mvp.contract.PortalCommentContract;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentPostBean;
import com.sulikeji.pipixia.portal.mvp.presenter.PortalCommentPresenter;
import com.sulikeji.pipixia.portal.mvp.ui.adapter.PortalCommentDetailAdapter;
import com.sulikeji.pipixia.portal.mvp.ui.dialog.PortalInputDialog;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
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
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ContentBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ImageListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ImagePostBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.MediaBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.VideoPostBean;
import me.jessyan.armscomponent.commoncore.message.mvp.ui.adapter.FeedImageAdapter;
import me.jessyan.armscomponent.commonservice.EventBusHub;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.BRVAH.loadmoreview.CustomLoadMoreView;
import me.jessyan.armscomponent.commonui.easyat.FormatHelper;
import me.jessyan.armscomponent.commonui.easyat.SpanFactory;
import me.jessyan.armscomponent.commonui.popup.MorePopup;
import me.jessyan.armscomponent.commonui.share.ShareOptions;
import me.jessyan.armscomponent.commonui.share.SharePopup;
import me.jessyan.armscomponent.commonui.share.SharePopupUtils;
import me.jessyan.armscomponent.commonui.share.ShareType;
import me.jessyan.armscomponent.commonui.util.ClipboardUtils;
import me.jessyan.armscomponent.commonui.util.ToastUtils;
import me.jessyan.armscomponent.commonui.video.FeedGSYVideoPlayer;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_IMAGE;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_TEXT;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_VIDEO;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.EMPTY_COMMENT_ID;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_NORMAL;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_SUCCESS;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/14/2019 16:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.PORTAL_PORTALCOMMENTACTIVITY)
public class PortalCommentActivity extends BaseActivity<PortalCommentPresenter> implements PortalCommentContract.View {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.inputContent)
    TextView inputContent;
    @BindView(R2.id.inputLike)
    ImageView inputLike;
    @BindView(R2.id.inputLikeCount)
    TextView inputLikeCount;
    @BindView(R2.id.inputComment)
    ImageView inputComment;
    @BindView(R2.id.inputCommentCount)
    TextView inputCommentCount;
    @BindView(R2.id.inputFavorite)
    ImageView inputFavorite;
    @BindView(R2.id.inputShare)
    ImageView inputShare;

    private ImageView userAvatar;
    private TextView userNickname;
    private TextView userTime;
    private Button userFollow;
    private TextView portalContent;
    private TextView commentCount;
    private RecyclerView imageRecyclerView;
    private FeedImageAdapter imageAdapter;
    private FrameLayout videoLayout;
    private FeedGSYVideoPlayer videoPlayer;
    private View shareLayout;

    private static final int REQUEST_CODE_CHOOSE_IMAGE = 1;
    private static final int REQUEST_CODE_CHOOSE_VIDEO = 2;
    private static final int REQUEST_CODE_CHOOSE_AT = 3;
    private static final int REQUEST_CODE_CHECK_AT = 4;

    private static final int IMAGE_MAX_WIDTH = ScreenUtils.getScreenSize(Utils.getApp())[0] - AutoSizeUtils.dp2px(Utils.getApp(), 40) * 2;
    private static final int IMAGE_MAX_HEGIHT = IMAGE_MAX_WIDTH;
    private static final int IMAGE_ADAPT_WIDTH = IMAGE_MAX_WIDTH / 2;
    private static final int IMAGE_ADAPT_HEGIHT = IMAGE_MAX_WIDTH;

    private int contentType;
    private int portalId;
    private int commentId;
    private int masterUserId;//作者用户ID
    private FeedListBean item;
    private List<FeedListBean> commentList;
    /**
     * 是否携带数据
     */
    private boolean isCarryData;

    private PortalCommentDetailAdapter adapter;

    private ActivitySlidingBackConsumer consumer;

    private PortalInputDialog inputDialog;

    private int fromType;
    private List<MediaBean> selectedList = new ArrayList<>();
    private String videoThumbPath;
    private OrientationUtils orientationUtils;

    /**
     * 被回复人的评论ID(子级评论ID)
     */
    private int toParentId = -1;
    /**
     * 被回复人的用户ID(子级用户ID)
     */
    private int toUserId = -1;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPortalCommentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        //退出全屏
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) return;
        //滑动退出
        SmartSwipeWrapper wrapper = SmartSwipe.peekWrapperFor(this);
        if (wrapper != null) {
            List<SwipeConsumer> consumers = wrapper.getAllConsumers();
            if (!consumers.isEmpty()) {
                for (SwipeConsumer consumer : consumers) {
                    if (consumer != null) {
                        if (consumer.isLeftEnable()) {
                            consumer.smoothLeftOpen();
                            return;
                        } else if (consumer.isTopEnable()) {
                            consumer.smoothTopOpen();
                            return;
                        }
                    }
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (inputDialog != null) inputDialog.onDestory();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
        if (videoPlayer != null) {
            videoPlayer.release();
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.portal_activity_comment; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initStatusBar();
        initSmartSwipe();
        parseIntent();
        initRecyclerView();
        initInputLayout();
        updateUI();
        mPresenter.getCommentDetail(commentId, portalId, true);
    }

    private void parseIntent() {
        if (getIntent() == null) return;
        contentType = getIntent().getIntExtra(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_CONTENTTYPE, -1);
        portalId = getIntent().getIntExtra(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_PORTALID, -1);
        commentId = getIntent().getIntExtra(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_COMMENTID, -1);
        masterUserId = getIntent().getIntExtra(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_MASTERUSERID, -1);
        item = getIntent().getParcelableExtra(RouterHub.PORTAL_PORTALCOMMENTACTIVITY_KEY_ITEM);
        isCarryData = item != null;
    }

    private void initStatusBar() {
        int statusBarColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarColor = getResources().getColor(R.color.public_colorPrimary);
            BarUtils.setStatusBarLightMode(this, true);
        } else {
            statusBarColor = getResources().getColor(R.color.public_colorPrimary_compatibility);
        }
        //状态栏颜色
        BarUtils.setStatusBarColor(this, statusBarColor);
    }

    private void initSmartSwipe() {
        consumer = SmartSwipe.wrap(this)
                .removeAllConsumers()
                .addConsumer(new ActivitySlidingBackConsumer(this))
                .setRelativeMoveFactor(0.0F)
                .enableTop()
                .as(ActivitySlidingBackConsumer.class);
    }

    private void processVideoSize(ResourceBean bean, View viewParent) {
        int originalHeight = bean == null ? 0 : bean.getHeight();
        int originalWidth = bean == null ? 0 : bean.getWidth();
        int realWidth;
        int realHeight;
        if (originalHeight == originalWidth) {
            //高等于宽
            realWidth = IMAGE_ADAPT_WIDTH;
            realHeight = IMAGE_ADAPT_HEGIHT;
        } else if (originalHeight > originalWidth) {
            //高大于宽
            realWidth = IMAGE_ADAPT_WIDTH;
            realHeight = (int) (originalHeight * 1f / originalWidth * realWidth + 0.5f);
            if (realHeight > IMAGE_MAX_HEGIHT) {
                realHeight = IMAGE_MAX_HEGIHT;
                realWidth = (int) (originalWidth * 1f / originalHeight * realHeight + 0.5f);
            }
        } else {
            //宽大于高
            realHeight = IMAGE_MAX_HEGIHT;
            realWidth = (int) (originalWidth * 1f / originalHeight * realHeight + 0.5f);
            if (realWidth > IMAGE_MAX_WIDTH) {
                realWidth = IMAGE_MAX_WIDTH;
                realHeight = (int) (originalHeight * 1f / originalWidth * realWidth + 0.5f);
            }
        }
        ViewGroup.LayoutParams layoutParams = viewParent.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = realWidth;
            layoutParams.height = realHeight;
            viewParent.setLayoutParams(layoutParams);
        }
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PortalCommentDetailAdapter(null);
        adapter.addHeaderView(initHeaderView());
        adapter.setMainUserId(masterUserId);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(() -> mPresenter.getCommentDetail(commentId, portalId, false));
        adapter.setOnItemClickListener((adapter, view, position) -> {
            //TODO:
        });
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
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
        adapter.setOnImageClickListener((imageView, position, imageList) -> {
            ARouter.getInstance()
                    .build(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY)
                    .withInt(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_POSITION, position)
                    .withParcelableArrayList(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_IMAGELIST, (ArrayList<ResourceBean>) imageList)
                    .withTransition(R.anim.public_preview_in_enter, R.anim.public_preview_in_exit)
                    .navigation(this);
        });
        adapter.bindToRecyclerView(recyclerView);
    }

    private View initHeaderView() {
        View headerView;
        switch (contentType) {
            default:
                headerView = LayoutInflater.from(this).inflate(R.layout.portal_header_comment_text, recyclerView, false);
                break;
            case CONTENT_TYPE_IMAGE:
                headerView = LayoutInflater.from(this).inflate(R.layout.portal_header_comment_image, recyclerView, false);
                initHeaderViewByImage(headerView);
                break;
            case CONTENT_TYPE_VIDEO:
                headerView = LayoutInflater.from(this).inflate(R.layout.portal_header_comment_video, recyclerView, false);
                initHeaderViewByVideo(headerView);
                break;
        }
        initHeaderViewByUser(headerView);//用户
        initHeaderViewByText(headerView);//帖子文本
        commentCount = headerView.findViewById(R.id.commentCount);//回复数
        return headerView;
    }

    private void initHeaderViewByUser(View headerView) {
        userAvatar = headerView.findViewById(R.id.userAvatar);
        userNickname = headerView.findViewById(R.id.userNickname);
        userTime = headerView.findViewById(R.id.userTime);
        userFollow = headerView.findViewById(R.id.userFollow);
        userFollow.setOnClickListener(v -> clickUserFollow());
    }

    private void initHeaderViewByText(View headerView) {
        portalContent = headerView.findViewById(R.id.portalContent);
    }

    private void initHeaderViewByImage(View headerView) {
        int paddingStart = AutoSizeUtils.dp2px(Utils.getApp(), 40);
        int paddingEnd = AutoSizeUtils.dp2px(Utils.getApp(), 40);
        int imageMaxWidth = (ScreenUtils.getScreenSize(Utils.getApp())[0] - paddingStart - paddingEnd);
        int imageMaxHegiht = imageMaxWidth;
        int imageAdaptWidth = imageMaxWidth / 2;
        int imageAdaptHegiht = imageAdaptWidth;
        imageRecyclerView = headerView.findViewById(R.id.imageRecyclerView);
        imageAdapter = new FeedImageAdapter(null);
        imageAdapter.setMaxWidth(imageMaxWidth);
        imageAdapter.setMaxHeight(imageMaxHegiht);
        imageAdapter.setAdaptHeight(imageAdaptWidth);
        imageAdapter.setAdaptWidth(imageAdaptHegiht);
        imageAdapter.setOnImageClickListener((imageView, position, imageList) -> {
            ARouter.getInstance()
                    .build(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY)
                    .withInt(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_POSITION, position)
                    .withParcelableArrayList(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_IMAGELIST, (ArrayList<ResourceBean>) imageList)
                    .withTransition(R.anim.public_preview_in_enter, R.anim.public_preview_in_exit)
                    .navigation(this);
        });
        imageRecyclerView.setAdapter(imageAdapter);
    }

    private void initHeaderViewByVideo(View headerView) {
        videoLayout = headerView.findViewById(R.id.videoLayout);
        videoPlayer = headerView.findViewById(R.id.videoPlayer);
        //视频信息
        ResourceBean videoBean = item == null ? null : item.getVideoBean();
        //适配大小
        processVideoSize(videoBean, videoLayout);
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(getActivity(), videoPlayer);
        orientationUtils.setEnable(false);//初始化不打开外部的旋转
        //封面
        ImageView thumbImageView = new ImageView(this);
        thumbImageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        Glide.with(this)
                .asBitmap()
                .load(videoBean == null ? "" : videoBean.getThumbnail())
                .into(thumbImageView);

        //配置
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        gsyVideoOptionBuilder.setThumbImageView(thumbImageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(videoBean == null ? "" : videoBean.getPath())
                .setCacheWithPlay(false)
                .setVideoTitle("")
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        Timber.tag("jason").d("onPrepared: url = " + url);
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        Timber.tag("jason").d("onAutoComplete: url = " + url);
                        if (videoPlayer.isIfCurrentIsFullscreen()) {
                            //全屏：需要先退出全屏
                            videoPlayer.onBackFullscreen();
                            shareLayout.setVisibility(View.VISIBLE);
                        } else {
                            shareLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        Timber.tag("jason").d("onQuitFullscreen: url = " + url);
                        if (orientationUtils != null) orientationUtils.backToProtVideo();
                        AutoSize.autoConvertDensityOfGlobal(getActivity());
                    }
                })
                .build(videoPlayer);

        videoPlayer.getFullscreenButton().setOnClickListener(v -> {
            //直接横屏
            orientationUtils.resolveByClick();
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            videoPlayer.startWindowFullscreen(this, true, false);
        });
        //分享
        shareLayout = headerView.findViewById(R.id.shareLayout);
        shareLayout.setVisibility(View.GONE);
        headerView.findViewById(R.id.videoShare).setOnClickListener(v -> clickInputShare());
        headerView.findViewById(R.id.videoReplay).setOnClickListener(v -> {
            shareLayout.setVisibility(View.GONE);
            videoPlayer.startPlayLogic();
        });
    }

    private void initInputLayout() {
        inputContent.setOnClickListener(v -> clickInputContent());
        inputLike.setOnClickListener(v -> clickInputLike());
        inputFavorite.setOnClickListener(v -> clickInputFavorite());
        inputShare.setOnClickListener(v -> clickInputShare());
    }

    public void updateUI() {
        updateHeaderUI();
        updateListUI();
        updateInputUI();
    }

    private void updateHeaderUI() {
        //用户
        updateUserAvatar();
        updateUserNickname();
        updateUserTime();
        updateUserFollow();
        //帖子文本
        updatePortalContent();
        //帖子图片
        updateImageList();
        //帖子视频
        updateVideoUI();
    }

    private void updateListUI() {
        if (commentList != null && !commentList.isEmpty()) {
            adapter.setNewData(commentList);
        } else {
            adapter.setNewData(null);
        }
    }

    private void updateInputUI() {
        //点赞
        updateInputLike();
        //收藏
        updateInputFavorite();
    }

    private void updateCommentCount() {
        int size = commentList == null ? 0 : commentList.size();
        commentCount.setVisibility(View.VISIBLE);
        commentCount.setText(getString(R.string.portal_dialog_comment_count, DescUtils.getReplyText(size)));
    }

    private void updateUserAvatar() {
        if (item == null) return;
        UserBean userBean = item.getUserBean();
        Glide.with(this)
                .load(userBean == null ? "" : userBean.getUserAvatar())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.public_placeholder)
                        .error(R.color.public_error))
                .into(userAvatar);
    }

    private void updateUserNickname() {
        if (item == null) return;
        UserBean userBean = item.getUserBean();
        userNickname.setText(userBean == null ? "" : userBean.getUserNickname());
    }

    private void updateUserTime() {
        if (item == null) return;
        String createTime = item.getCreateTime();
        userTime.setText(createTime);
    }

    private void updateUserFollow() {
        if (item == null) return;
        int isFollow = item.getIsFollow();
        userFollow.setVisibility(isShowFollow() ? View.VISIBLE : View.GONE);
        userFollow.setSelected(isFollow == STATUS_SUCCESS);
        userFollow.setText(isFollow == STATUS_SUCCESS ? "已关注" : "关注");
    }

    private void updatePortalContent() {
        if (item == null) return;
        portalContent.setMovementMethod(LinkMovementMethod.getInstance());
        portalContent.setText("", TextView.BufferType.EDITABLE);
        List<ContentBean> contentList = item.getContentList();
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

    private void updateImageList() {
        if (contentType != CONTENT_TYPE_IMAGE) return;
        if (item == null) return;
        List<ResourceBean> imageList = item.getImageList();
        if (isFromImageByContentType() && imageList != null && !imageList.isEmpty()) {
            List<ImageListBean> list = new ArrayList<>();
            list.add(new ImageListBean(imageList));
            imageRecyclerView.setVisibility(View.VISIBLE);
            imageAdapter.setNewData(list);
        } else {
            imageRecyclerView.setVisibility(View.GONE);
            imageAdapter.setNewData(null);
        }
    }

    private void updateVideoUI() {
        if (contentType != CONTENT_TYPE_VIDEO) return;
        if (item == null) return;
        //视频信息
        ResourceBean videoBean = item.getVideoBean();
        //更新封面
        ImageView thumbImageView = (ImageView) videoPlayer.getThumbImageView();
        Glide.with(this)
                .asBitmap()
                .load(videoBean == null ? "" : videoBean.getThumbnail())
                .into(thumbImageView);
        //设置Url
        videoPlayer.setUp(videoBean == null ? "" : videoBean.getPath(), false, "");
    }

    private void updateInputLike() {
        if (item == null) return;
        inputLike.setSelected(item.getIsLike() == STATUS_SUCCESS);
        inputLikeCount.setSelected(item.getIsLike() == STATUS_SUCCESS);
        inputLikeCount.setText(DescUtils.getLikeText(item.getLikeCount()));
    }

    private void updateInputFavorite() {
        if (item == null) return;
        inputFavorite.setSelected(item.getIsFavorite() == STATUS_SUCCESS);
    }

    /**
     * 是否是（文字+图片）
     */
    private boolean isFromImageByContentType() {
        return contentType == FeedListBean.CONTENT_TYPE_IMAGE;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ToastUtils.normal(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getContentType() {
        return contentType;
    }

    @Override
    public int getPortalId() {
        return portalId;
    }

    @Override
    public int getCommentId() {
        return commentId;
    }

    @Override
    public int getMasterUserId() {
        return masterUserId;
    }

    @Override
    public FeedListBean getItem() {
        return item;
    }

    @Override
    public List<FeedListBean> getCommentList() {
        return commentList;
    }

    private void showInputDialog(String nickname, int toParentId, int toUserId) {
        this.toParentId = toParentId;
        this.toUserId = toUserId;
        if (inputDialog == null) {
            inputDialog = new PortalInputDialog.Builder(this)
                    .chooseImageForResult(REQUEST_CODE_CHOOSE_IMAGE)
                    .chooseVideoForResult(REQUEST_CODE_CHOOSE_VIDEO)
                    .chooseAtForResult(REQUEST_CODE_CHOOSE_AT)
                    .checkAtForResult(REQUEST_CODE_CHECK_AT)
                    .setShowVideo(false)
                    .setShowImage(true)
                    .toCommentUser(nickname)
                    .toParentId(toParentId)
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
        } else {
            inputDialog.resetData(toParentId);//必须先调用
            inputDialog.toCommentUser(nickname);
            inputDialog.toParentId(toParentId);
        }
        inputDialog.showDialog();
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
        List<FeedListBean> list = adapter.getData();
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
        List<FeedListBean> list = adapter.getData();
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

    //=============================================================onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_IMAGE:
                processResultForChooseImage(data);
                break;
            case REQUEST_CODE_CHOOSE_VIDEO:
                processResultForChooseVideo(data);
                break;
            case REQUEST_CODE_CHOOSE_AT:
                processResultForChooseAt(data);
                break;
            case REQUEST_CODE_CHECK_AT:
                processResultForCheckAt(data);
                break;
        }
    }

    private void processResultForChooseImage(Intent data) {
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

    private void processResultForChooseVideo(Intent data) {
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

    private void processResultForChooseAt(Intent data) {
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

    private void processResultForCheckAt(Intent data) {
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

    //=========================================================Publish
    private void clickChooseSend() {
        KeyboardUtils.hideSoftInput(this);
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
        adapter.addData(0, listBean);
        //2、上传到服务器
        CommentPostBean bean = new CommentPostBean();
        jointResultByCustom(bean, null);
        jointResultByText(bean, null);
        bean.setCommentKey(commentKey);
        bean.setContentType(CONTENT_TYPE_TEXT);
        mPresenter.replyComment(bean);
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
        adapter.addData(0, listBean);
        //2、上传到服务器
        //2-1.先获取七牛token
        //2-2.上传图片到七牛云
        //2-3.再通过发帖接口post到服务器
        cacheResultByText(commentKey);
        cacheResultByToParentId(commentKey);
        cacheResultByToUserId(commentKey);
        List<MediaBean> tempSelectedList = copySelectedList();
        mPresenter.getQiniuToken(commentKey, true, tempSelectedList, null);
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
        adapter.addData(0, listBean);
        //2、上传到服务器
        //2-1.先获取七牛token
        //2-2.上传视频和缩略图到七牛云
        //2-3.再通过发帖接口post到服务器
        cacheResultByText(commentKey);
        cacheResultByToParentId(commentKey);
        cacheResultByToUserId(commentKey);
        List<MediaBean> tempSelectedList = copySelectedList();
        String tempVideoThumbPath = copyVideoThumbPath();
        mPresenter.getQiniuToken(commentKey, false, tempSelectedList, tempVideoThumbPath);
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

    //=========================================================拼接FeedListBean
    private void jointResultByCustom(FeedListBean bean) {
        bean.setId(EMPTY_COMMENT_ID);//当前发布的评论ID
        bean.setCreateTime("刚刚");
        bean.setUserBean(getUserBeanByCache());
        bean.setObjectId(portalId);//帖子ID
        bean.setParentId(commentId);//回复所属的一级评论ID(父级评论ID)
        bean.setToParentId(toParentId);//被回复人的评论ID(子级评论ID)
        bean.setToUserId(toUserId);//被回复人的用户ID(子级用户ID)
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
    private Map<String, Integer> cacheMapByToParentId = new HashMap<>();
    private Map<String, Integer> cacheMapByToUserId = new HashMap<>();

    private void cacheResultByText(String commentKey) {
        String json = getJsonTextContent();
        cacheMapByText.put(commentKey, json);
    }

    private void cacheResultByToParentId(String commentKey) {
        cacheMapByToParentId.put(commentKey, toParentId);
    }

    private void cacheResultByToUserId(String commentKey) {
        cacheMapByToUserId.put(commentKey, toUserId);
    }

    private String getCacheByText(String commentKey) {
        return cacheMapByText.get(commentKey);
    }

    private Integer getCacheByToParentId(String commentKey) {
        return cacheMapByToParentId.get(commentKey);
    }

    private Integer getCacheByToUserId(String commentKey) {
        return cacheMapByToUserId.get(commentKey);
    }

    private boolean isExistCommentKey(String commentKey) {
        if (getCacheByText(commentKey) != null)
            return true;
        else
            return false;
    }

    private void jointResultByCustom(CommentPostBean bean, String commentKey) {
        bean.setPortalId(portalId);
        bean.setParentId(commentId);
        //这里因为请求是耗时的，所以需要先缓存起来，请求成功之后再去取
        int toParentId = TextUtils.isEmpty(commentKey) ? this.toParentId : getCacheByToParentId(commentKey);
        bean.setToParentId(toParentId);
        int toUserId = TextUtils.isEmpty(commentKey) ? this.toUserId : getCacheByToUserId(commentKey);
        bean.setToUserId(toUserId);
        Timber.tag("jason").d("jointResultByText：toParentId = " + toParentId + " ,toUserId =" + toUserId + " ,commentKey = " + commentKey);
    }

    private void jointResultByText(CommentPostBean bean, String commentKey) {
        String json = TextUtils.isEmpty(commentKey) ? getJsonTextContent() : getCacheByText(commentKey);
        Timber.tag("jason").d("jointResultByText：json = " + json + " ,commentKey = " + commentKey);
        bean.setContent(json);
    }

    private void jointResultByImage(CommentPostBean bean, List<ImagePostBean> successList) {
        String json = new Gson().toJson(successList);
        Timber.tag("jason").d("jointResultByImage = " + json);
        bean.setMore(json);
    }

    private void jointResultByVideo(CommentPostBean bean, VideoPostBean videoBean) {
        String json = new Gson().toJson(videoBean);
        Timber.tag("jason").d("jointResultByVideo = " + json);
        bean.setMore(json);
    }

    //=========================================================Click
    private void clickUserFollow() {
        if (item == null) return;
        if (item.getIsFollow() == STATUS_SUCCESS) userCancelFollow();
        else userFollow();
    }

    private void clickInputContent() {
        showInputDialog(null, -1, -1);
    }

    private void clickInputLike() {
        if (item == null) return;
        if (item.getIsLike() == STATUS_SUCCESS) {
            mPresenter.commentCancelLike(item.getId(), item.getObjectId(), true);
        } else {
            mPresenter.commentLike(item.getId(), item.getObjectId(), true);
        }
    }

    private void clickInputFavorite() {
        if (item == null) return;
        if (item.getIsFavorite() == STATUS_SUCCESS) {
            mPresenter.commentCancelCollect(item.getObjectId(), item.getId(), item.getFavoriteId());
        } else {
            mPresenter.commentCollect(item.getObjectId(), item.getId());
        }
    }

    private void clickInputShare() {
        if (item == null) return;
        SharePopup sharePopup = SharePopupUtils.getSharePopup(this, new ShareOptions.Builder()
                .favorite(item.getIsFavorite() == STATUS_SUCCESS)
                .downloadVideo(item.getContentType() == CONTENT_TYPE_VIDEO)
                .build());
        sharePopup.setOnItemClickListener((iconId, titleId, clickTag) -> {
            switch (clickTag) {
                case ShareType.SHARE_TYPE_WECHAT:
                    mPresenter.shareByWechat();
                    break;
                case ShareType.SHARE_TYPE_QQ:
                    mPresenter.shareByQQ();
                    break;
                case ShareType.SHARE_TYPE_WECHAT_MOMENTS:
                    mPresenter.shareByWechatMoments();
                    break;
                case ShareType.SHARE_TYPE_QZONE:
                    mPresenter.shareByQzone();
                    break;
                case ShareType.SHARE_TYPE_COMPLAIN:
                    break;
                case ShareType.SHARE_TYPE_FAVORITE:
                    mPresenter.commentCollect(item.getObjectId(), item.getId());
                    break;
                case ShareType.SHARE_TYPE_UNFAVORITE:
                    mPresenter.commentCancelCollect(item.getObjectId(), item.getId(), item.getFavoriteId());
                    break;
                case ShareType.SHARE_TYPE_DOWNLOAD_IMAGE:
                    break;
                case ShareType.SHARE_TYPE_DOWNLOAD_VIDEO:
                    mPresenter.clickDownloadVideo();
                    break;
            }
        });
        sharePopup.showPopupWindow();
    }

    private void userFollow() {
        UserBean userBean = item.getUserBean();
        if (userBean == null) return;
        mPresenter.follow(userBean.getUserId());
    }

    private void userCancelFollow() {
        UserBean userBean = item.getUserBean();
        if (userBean == null) return;
        mPresenter.cancelFollow(userBean.getUserId());
    }

    //=========================================================onItemClick
    private void itemClickCommentLike(FeedListBean item, int position) {
        if (item.getIsLike() == STATUS_SUCCESS) {
            mPresenter.commentCancelLike(item.getId(), item.getObjectId(), false);
        } else {
            mPresenter.commentLike(item.getId(), item.getObjectId(), false);
        }
    }

    private void itemClickCommentReply(FeedListBean item, int position) {
        UserBean userBean = item.getUserBean();
        showInputDialog(userBean.getUserNickname(), item.getId(), userBean.getUserId());
    }

    private void itemCLickCommentMore(FeedListBean item, int position) {
        MorePopup.Builder builder = new MorePopup.Builder(this);
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
                    TextView commentContent = (TextView) adapter.getViewByPosition(position + adapter.getHeaderLayoutCount(), R.id.commentContent);
                    ClipboardUtils.copyText(commentContent.getText());
                    ToastUtils.normal("已粘贴到剪贴板");
                    break;
                case MoreType.MORE_TYPE_DELETE:
                    new CustomDialog.Builder(this)
                            .title(R.string.portal_dialog_delete_comment_title)
                            .negativeText(R.string.portal_dialog_delete_comment_cancel)
                            .positiveText(R.string.portal_dialog_delete_comment_ok)
                            .onPositive((dialog, dialogAction) -> {
                                mPresenter.deleteComment(item.getId(), item.getParentId());
                            })
                            .show();
                    break;
                case MoreType.MORE_TYPE_COMPLAIN:
                    ToastUtils.normal("举报");
                    break;
            }
        });
        morePopup.setOnRootViewProvider(() -> findViewById(R.id.contentLayout));
        morePopup.showPopupWindow();
    }

    //=========================================================boolean method
    private int getMainUserId() {
        me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean userBean = ApplicationCache.getUserBean();
        return userBean == null ? 0 : userBean.getUserId();
    }

    private boolean isShowFollow() {
        //本人不显示关注
        UserBean userBean = item.getUserBean();
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

    //=========================================================eventbus
    @Subscriber(tag = EventBusHub.PORTAL_COMMENTLISTREFRESHSUCCESS, mode = ThreadMode.MAIN)
    private void commentListRefreshSuccess(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        if (this.portalId != portalId) return;
        if (this.commentId != commentId) return;
        FeedListBean bean = bundle.getParcelable(EventBusHub.PORTAL_KEY_FEEDLISTBEAN);
        List<FeedListBean> commentList = bundle.getParcelableArrayList(EventBusHub.PORTAL_KEY_COMMENTLIST);
        int total = bundle.getInt(EventBusHub.PORTAL_KEY_TOTAL, -1);
        //下拉刷新
        this.item = bean;
        this.commentList = commentList;
        if (!isCarryData) {
            updateHeaderUI();//刷新头部UI
        }
        adapter.setNewData(commentList);
        updateCommentCount();
        //是否还有更多数据
        if (commentList == null || commentList.isEmpty() || commentList.size() < 10)
            adapter.setEnableLoadMore(false);
        else
            adapter.setEnableLoadMore(true);

    }

    @Subscriber(tag = EventBusHub.PORTAL_COMMENTLISTREFRESHERROR, mode = ThreadMode.MAIN)
    private void commentListRefreshError(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        if (this.portalId != portalId) return;
        if (this.commentId != commentId) return;
        //下拉刷新

    }

    @Subscriber(tag = EventBusHub.PORTAL_COMMENTLISTLOADMORESUCCESS, mode = ThreadMode.MAIN)
    private void commentListLoadMoreSuccess(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        if (this.portalId != portalId) return;
        if (this.commentId != commentId) return;
        List<FeedListBean> commentList = bundle.getParcelableArrayList(EventBusHub.PORTAL_KEY_COMMENTLIST);
        int total = bundle.getInt(EventBusHub.PORTAL_KEY_TOTAL, -1);
        //上拉加载更多
        if (commentList != null && !commentList.isEmpty()) adapter.addData(commentList);
        updateCommentCount();
        //是否还有更多数据
        if (commentList == null || commentList.isEmpty() || commentList.size() < 10)
            adapter.loadMoreEnd();
        else
            adapter.loadMoreComplete();
    }

    @Subscriber(tag = EventBusHub.PORTAL_COMMENTLISTLOADMOREERROR, mode = ThreadMode.MAIN)
    private void commentListLoadMoreError(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        if (this.portalId != portalId) return;
        if (this.commentId != commentId) return;
        //上拉加载更多
        adapter.loadMoreFail();
    }

    @Subscriber(tag = EventBusHub.PORTAL_FOLLOWSUCCESS, mode = ThreadMode.MAIN)
    private void followSuccess(Bundle bundle) {
        int userId = bundle.getInt(EventBusHub.PORTAL_KEY_USERID, -1);
        UserBean userBean = item.getUserBean();
        if (userBean == null) return;
        if (userBean.getUserId() != userId) return;
        item.setIsFollow(STATUS_SUCCESS);
        updateUserFollow();
    }

    @Subscriber(tag = EventBusHub.PORTAL_CANCELFOLLOWSUCCESS, mode = ThreadMode.MAIN)
    private void cancelFollowSuccess(Bundle bundle) {
        int userId = bundle.getInt(EventBusHub.PORTAL_KEY_USERID, -1);
        UserBean userBean = item.getUserBean();
        if (userBean == null) return;
        if (userBean.getUserId() != userId) return;
        item.setIsFollow(STATUS_NORMAL);
        updateUserFollow();
    }

    @Subscriber(tag = EventBusHub.PORTAL_COMMENTLIKESUCCESS, mode = ThreadMode.MAIN)
    private void commentLikeSuccess(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int likeCount = bundle.getInt(EventBusHub.PORTAL_KEY_LIKECOUNT, -1);
        boolean isParentComment = bundle.getBoolean(EventBusHub.PORTAL_KEY_ISPARENTCOMMENT);
        if (this.portalId != portalId) return;
        if (isParentComment) {
            //父级评论
            if (item == null) return;
            item.setIsLike(STATUS_SUCCESS);
            item.setLikeCount(likeCount);
            updateInputLike();
        } else {
            //子级评论
            int index = getIndexByCommentId(commentId);
            if (index == -1) return;
            FeedListBean bean = adapter.getItem(index);
            if (bean == null) return;
            if (bean.getId() != commentId) return;
            bean.setIsLike(STATUS_SUCCESS);
            bean.setLikeCount(likeCount);
            ImageView commentLike = (ImageView) adapter.getViewByPosition(index + adapter.getHeaderLayoutCount(), R.id.commentLike);
            TextView commentLikeCount = (TextView) adapter.getViewByPosition(index + adapter.getHeaderLayoutCount(), R.id.commentLikeCount);
            commentLike.setSelected(true);
            commentLikeCount.setSelected(true);
            commentLikeCount.setText(DescUtils.getLikeText(likeCount));
        }
    }

    @Subscriber(tag = EventBusHub.PORTAL_COMMENTCANCELLIKESUCCESS, mode = ThreadMode.MAIN)
    private void commentCancelLikeSuccess(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int likeCount = bundle.getInt(EventBusHub.PORTAL_KEY_LIKECOUNT, -1);
        boolean isParentComment = bundle.getBoolean(EventBusHub.PORTAL_KEY_ISPARENTCOMMENT);
        if (this.portalId != portalId) return;
        if (isParentComment) {
            //父级评论
            if (item == null) return;
            item.setIsLike(STATUS_NORMAL);
            item.setLikeCount(likeCount);
            updateInputLike();
        } else {
            //子级评论
            int index = getIndexByCommentId(commentId);
            if (index == -1) return;
            FeedListBean bean = adapter.getItem(index);
            if (bean == null) return;
            if (bean.getId() != commentId) return;
            bean.setIsLike(STATUS_NORMAL);
            bean.setLikeCount(likeCount);
            ImageView commentLike = (ImageView) adapter.getViewByPosition(index + adapter.getHeaderLayoutCount(), R.id.commentLike);
            TextView commentLikeCount = (TextView) adapter.getViewByPosition(index + adapter.getHeaderLayoutCount(), R.id.commentLikeCount);
            commentLike.setSelected(false);
            commentLikeCount.setSelected(false);
            commentLikeCount.setText(DescUtils.getLikeText(likeCount));
        }
    }


    @Subscriber(tag = EventBusHub.PORTAL_REPLYCOMMENTSUCCESS, mode = ThreadMode.MAIN)
    private void replyCommentSuccess(Bundle bundle) {
        //评论成功
        FeedListBean bean = bundle.getParcelable(EventBusHub.PORTAL_KEY_FEEDLISTBEAN);
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        String commentKey = bundle.getString(EventBusHub.PORTAL_KEY_COMMENTKEY, null);
        int parentId = bundle.getInt(EventBusHub.PORTAL_KEY_PARENTID, -1);
        int toParentId = bundle.getInt(EventBusHub.PORTAL_KEY_TOPARENTID, -1);
        int toUserId = bundle.getInt(EventBusHub.PORTAL_KEY_TOUSERID, -1);
        if (this.portalId != portalId) return;
        if (bean == null) return;
        //二级评论
        int index = getIndexByCommentKey(commentKey);
        if (index == -1) return;
        FeedListBean src = adapter.getData().get(index);
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
        jointResultByCustom(bean, commentKey);
        jointResultByText(bean, commentKey);//从缓存中获取文本
        jointResultByImage(bean, imageList);
        bean.setCommentKey(commentKey);
        bean.setContentType(CONTENT_TYPE_IMAGE);
        mPresenter.replyComment(bean);
    }

    @Subscriber(tag = EventBusHub.PORTAL_UPLOADVIDEOSUCCESS, mode = ThreadMode.MAIN)
    private void uploadVideoSuccess(Bundle bundle) {
        //上传图片到七牛成功
        String commentKey = bundle.getString(EventBusHub.PORTAL_KEY_COMMENTKEY);
        if (!isExistCommentKey(commentKey)) return;
        VideoPostBean videoBean = bundle.getParcelable(EventBusHub.PORTAL_KEY_VIDEOBEAN);
        CommentPostBean bean = new CommentPostBean();
        jointResultByCustom(bean, commentKey);
        jointResultByText(bean, commentKey);//从缓存中获取文本
        jointResultByVideo(bean, videoBean);
        bean.setCommentKey(commentKey);
        bean.setContentType(CONTENT_TYPE_VIDEO);
        mPresenter.replyComment(bean);
    }

    @Subscriber(tag = EventBusHub.PORTAL_DELETECOMMENTSUCCESS, mode = ThreadMode.MAIN)
    private void deleteCommentSuccess(Bundle bundle) {
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int parentId = bundle.getInt(EventBusHub.PORTAL_KEY_PARENTID, -1);
        if (this.commentId != parentId) return;
        //删除子评论
        int index = getIndexByCommentId(commentId);
        if (index == -1) return;
        adapter.remove(index);
    }

    @Subscriber(tag = EventBusHub.PORTAL_COMMENTFAVORITESUCCESS, mode = ThreadMode.MAIN)
    private void commentFavoriteSuccess(Bundle bundle) {
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int favoriteId = bundle.getInt(EventBusHub.PORTAL_KEY_FAVORITEID, -1);
        if (this.portalId != portalId) return;
        if (this.commentId != commentId) return;
        if (item == null) return;
        item.setIsFavorite(STATUS_SUCCESS);
        item.setFavoriteId(favoriteId);
        updateInputFavorite();
    }

    @Subscriber(tag = EventBusHub.PORTAL_COMMENTCANCELFAVORITESUCCESS, mode = ThreadMode.MAIN)
    private void commentCancelFavoriteSuccess(Bundle bundle) {
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int commentId = bundle.getInt(EventBusHub.PORTAL_KEY_COMMONID, -1);
        int favoriteId = bundle.getInt(EventBusHub.PORTAL_KEY_FAVORITEID, -1);
        if (this.portalId != portalId) return;
        if (this.commentId != commentId) return;
        if (item == null) return;
        item.setIsFavorite(STATUS_NORMAL);
        item.setFavoriteId(favoriteId);
        updateInputFavorite();
    }
}
