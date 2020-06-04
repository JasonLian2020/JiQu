package com.sulikeji.pipixia.portal.mvp.ui.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.sulikeji.pipixia.portal.R;
import com.sulikeji.pipixia.portal.R2;
import com.sulikeji.pipixia.portal.di.component.DaggerPortalVideoComponent;
import com.sulikeji.pipixia.portal.mvp.contract.PortalVideoContract;
import com.sulikeji.pipixia.portal.mvp.presenter.PortalVideoPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;
import me.jessyan.armscomponent.commonui.util.ToastUtils;
import me.jessyan.armscomponent.commonui.video.FeedGSYVideoPlayer;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.ScreenUtils;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/17/2019 15:17
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class PortalVideoFragment extends PortalBaseFragment<PortalVideoPresenter> implements PortalVideoContract.View {
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.userAvatar)
    RoundedImageView userAvatar;
    @BindView(R2.id.userFollow)
    Button userFollow;
    @BindView(R2.id.userNickname)
    TextView userNickname;
    @BindView(R2.id.userTime)
    TextView userTime;
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
    @BindView(R2.id.videoLayout)
    FrameLayout videoLayout;
    @BindView(R2.id.videoPlayer)
    FeedGSYVideoPlayer videoPlayer;
    @BindView(R2.id.shareLayout)
    FrameLayout shareLayout;
    @BindView(R2.id.shareWechat)
    ImageView shareWechat;
    @BindView(R2.id.shareQQ)
    ImageView shareQQ;
    @BindView(R2.id.shareWechatMoments)
    ImageView shareWechatMoments;
    @BindView(R2.id.shareQzone)
    ImageView shareQzone;
    @BindView(R2.id.shareReplay)
    Button shareReplay;

    @Inject
    BaseQuickAdapter mAdapter;

    private OrientationUtils orientationUtils;

    private TextView portalContent;

    public static PortalVideoFragment newInstance() {
        PortalVideoFragment fragment = new PortalVideoFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerPortalVideoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public boolean onBackPressed() {
        Timber.tag("jason").d("onBackPressed");
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(mContext)) return true;
        else return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.tag("jason").d("onDestroy");
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Timber.tag("jason").d("onConfigurationChanged： orientation = " + newConfig.orientation);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.portal_fragment_video, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initVideoLayout();
        initUserLayout();
        initRecyclerView();
        initInputLayout();
    }

    private View initHeaderView() {
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.portal_header_video, recyclerView, false);
        //帖子内容
        portalContent = headerView.findViewById(R.id.portalContent);
        configPortalContent(portalContent);
        return headerView;
    }

    private void initVideoLayout() {
        FeedListBean item = getDetailActivity().getItem();
        ResourceBean videoBean = item == null ? null : item.getVideoBean();

        processVideoSize(videoBean, videoLayout);

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(getActivity(), videoPlayer);
        orientationUtils.setEnable(false);//初始化不打开外部的旋转

        //封面
        ImageView thumbImageView = new ImageView(mContext);
        thumbImageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        Glide.with(mContext)
                .asBitmap()
                .load(videoBean == null ? "" : videoBean.getThumbnail())
                .into(thumbImageView);

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
            videoPlayer.startWindowFullscreen(mContext, true, false);
        });
        //分享
        shareLayout.setVisibility(View.GONE);
        shareWechat.setOnClickListener(v -> shareByWechat());
        shareQQ.setOnClickListener(v -> shareByQQ());
        shareWechatMoments.setOnClickListener(v -> shareByWechatMoments());
        shareQzone.setOnClickListener(v -> shareByQzone());
        shareReplay.setOnClickListener(v -> {
            shareLayout.setVisibility(View.GONE);
            videoPlayer.startPlayLogic();
        });
    }

    private void processVideoSize(ResourceBean bean, View viewParent) {
        int originalHeight = bean == null ? 0 : bean.getHeight();
        int originalWidth = bean == null ? 0 : bean.getWidth();
        int realWidth;
        int realHeight;
        if (originalHeight == originalWidth) {
            //高等于宽
            realWidth = ScreenUtils.getScreenSize(mContext)[0];
            realHeight = (int) (realWidth * 0.5625f + 0.5f);
        } else if (originalHeight > originalWidth) {
            //高大于宽
            realWidth = ScreenUtils.getScreenSize(mContext)[0];
            realHeight = (int) (originalHeight * 1f / originalWidth * (realWidth / 3) + 0.5f);
        } else {
            //宽大于高
            realWidth = ScreenUtils.getScreenSize(mContext)[0];
            realHeight = (int) (originalHeight * 1f / originalWidth * realWidth + 0.5f);
        }
        ViewGroup.LayoutParams layoutParams = viewParent.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = realWidth;
            layoutParams.height = realHeight;
            viewParent.setLayoutParams(layoutParams);
        }
    }

    private void setFullScreen() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initUserLayout() {
        configUserAvatar(userAvatar);
        configUserNickname(userNickname);
        configUserTime(userTime);
        configUserFollow(userFollow);
    }

    private void initRecyclerView() {
        mAdapter.addHeaderView(initHeaderView());
        configRefreshLayout(refreshLayout);
        configRecyclerView(recyclerView, mAdapter);
    }

    private void initInputLayout() {
        configInputContent(inputContent);
        configInputLike(inputLike, inputLikeCount);
        configInputComment(inputComment, inputCommentCount);
        configInputFavorite(inputFavorite);
        configInputShare(inputShare);
    }

    private void updateVideoPlayer() {

    }

    @Override
    protected ImageView getInputFavorite() {
        return inputFavorite;
    }

    @Override
    protected ImageView getInputLike() {
        return inputLike;
    }

    @Override
    protected TextView getInputLikeCount() {
        return inputLikeCount;
    }

    @Override
    protected Button getUserFollow() {
        return userFollow;
    }

    @Override
    protected void updateDetailUI() {
        //user
        configUserAvatar(userAvatar);
        configUserNickname(userNickname);
        configUserTime(userTime);
        configUserFollow(userFollow);
        //content
        configPortalContent(portalContent);
        //input
        configInputContent(inputContent);
        configInputLike(inputLike, inputLikeCount);
        configInputComment(inputComment, inputCommentCount);
        configInputFavorite(inputFavorite);
        configInputShare(inputShare);
    }

    @Override
    public BaseQuickAdapter getPortalCommentAdapter() {
        return mAdapter;
    }

    @Override
    public void finishRefresh(boolean success, boolean emptyResponse, boolean noMoreData) {

    }

    @Override
    public void finishLoadMore(boolean success, boolean emptyResponse, boolean noMoreData) {
        if (emptyResponse) refreshLayout.finishLoadMoreWithNoMoreData();
        else refreshLayout.finishLoadMore(success);
        refreshLayout.setEnableLoadMore(!noMoreData);
    }

    @Override
    public void moveToContent() {
        moveToPosition(recyclerView, 1);
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

    }
}
