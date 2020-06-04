package com.sulikeji.pipixia.portal.mvp.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.sulikeji.pipixia.portal.R;
import com.sulikeji.pipixia.portal.di.component.DaggerPortalDetailComponent;
import com.sulikeji.pipixia.portal.mvp.contract.PortalDetailContract;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentPostBean;
import com.sulikeji.pipixia.portal.mvp.presenter.PortalDetailPresenter;
import com.sulikeji.pipixia.portal.mvp.ui.fragment.PortalBaseFragment;
import com.sulikeji.pipixia.portal.mvp.ui.fragment.PortalImageFragment;
import com.sulikeji.pipixia.portal.mvp.ui.fragment.PortalVideoFragment;

import java.util.List;

import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ContentBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.MediaBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.util.ToastUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_VIDEO;


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
@Route(path = RouterHub.PORTAL_PORTALDETAILACTIVITY)
public class PortalDetailActivity extends BaseActivity<PortalDetailPresenter> implements PortalDetailContract.View {
    public static final int REQUEST_CODE_CHOOSE_IMAGE = 1;
    public static final int REQUEST_CODE_CHOOSE_VIDEO = 2;
    public static final int REQUEST_CODE_CHOOSE_AT = 3;
    public static final int REQUEST_CODE_CHECK_AT = 4;

    /**
     * 是否携带数据
     */
    private boolean isCarryData;
    private int contentType;
    private int portalId;
    private FeedListBean feedBean;
    private PortalBaseFragment baseFragment;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPortalDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        if (baseFragment != null && baseFragment.onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_IMAGE:
                if (data == null) return;
                processResultForChooseImage(data);
                break;
            case REQUEST_CODE_CHOOSE_VIDEO:
                if (data == null) return;
                processResultForChooseVideo(data);
                break;
            case REQUEST_CODE_CHOOSE_AT:
                if (data == null) return;
                processResultForChooseAt(data);
                break;
            case REQUEST_CODE_CHECK_AT:
                if (data == null) return;
                processResultForCheckAt(data);
                break;
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.portal_activity_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        parseIntent();
        if (getContentType() == CONTENT_TYPE_VIDEO) {
            //视频布局
            setFullScreen();
            initVideoFragment();
        } else {
            //图片布局
            initStatusBar();
            initImageFragment();
        }
        mPresenter.getPortalDetail(getPortalId());
        getCommentList(true);
    }

    private void parseIntent() {
        if (getIntent() == null) return;
        contentType = getIntent().getIntExtra(RouterHub.PORTAL_PORTALDETAILACTIVITY_KEY_CONTENTTYPE, -1);
        portalId = getIntent().getIntExtra(RouterHub.PORTAL_PORTALDETAILACTIVITY_KEY_PORTALID, -1);
        feedBean = getIntent().getParcelableExtra(RouterHub.PORTAL_PORTALDETAILACTIVITY_KEY_ITEM);
        isCarryData = feedBean != null;
    }

    private void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    private void initImageFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        baseFragment = PortalImageFragment.newInstance();
        ft.replace(R.id.container, baseFragment);
        ft.commitAllowingStateLoss();
    }

    private void initVideoFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        baseFragment = PortalVideoFragment.newInstance();
        ft.replace(R.id.container, baseFragment);
        ft.commitAllowingStateLoss();
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
    public void detailResultSuccess(FeedListBean bean) {
        //帖子详情接口请求成功
        feedBean = bean;
        if (isCarryData()) return;
        Message message = Message.obtain();
        message.what = 0;
        baseFragment.setData(message);
    }

    @Override
    public void detailResultError(String message) {
        //帖子详情接口请求失败
        showMessage(message);
    }

    @Override
    public void finishRefresh(boolean success, boolean emptyResponse, boolean noMoreData) {
        baseFragment.finishRefresh(success, emptyResponse, noMoreData);
    }

    @Override
    public void finishLoadMore(boolean success, boolean emptyResponse, boolean noMoreData) {
        baseFragment.finishLoadMore(success, emptyResponse, noMoreData);
    }

    @Override
    public BaseQuickAdapter getPortalCommentAdapter() {
        return baseFragment.getPortalCommentAdapter();
    }

    //=========================================================Fragment共同处理
    private void processResultForChooseImage(Intent data) {
        baseFragment.processResultForChooseImage(data);
    }

    private void processResultForChooseVideo(Intent data) {
        baseFragment.processResultForChooseVideo(data);
    }

    private void processResultForChooseAt(Intent data) {
        baseFragment.processResultForChooseAt(data);
    }

    private void processResultForCheckAt(Intent data) {
        baseFragment.processResultForCheckAt(data);
    }

    //=========================================================提供给Fragment调用变量
    public boolean isCarryData() {
        return isCarryData;
    }

    public int getContentType() {
        return contentType;
    }

    public int getPortalId() {
        return portalId;
    }

    public FeedListBean getItem() {
        return feedBean;
    }

    public List<ContentBean> getContentList() {
        return feedBean.getContentList();
    }

    public UserBean getUserBean() {
        if (feedBean == null) return null;
        return feedBean.getUserBean();
    }

    public List<ResourceBean> getImageList() {
        if (feedBean == null) return null;
        return feedBean.getImageList();
    }

    public ResourceBean getVideoBean() {
        if (feedBean == null) return null;
        return feedBean.getVideoBean();
    }

    //=========================================================提供给Fragment调用功能
    public void portalCollect(int portalId) {
        mPresenter.portalCollect(portalId);
    }

    public void portalCancelCollect(int portalId, int favoriteId) {
        mPresenter.portalCancelCollect(portalId, favoriteId);
    }

    public void like(int portalId) {
        mPresenter.like(portalId);
    }

    public void cancelLike(int portalId) {
        mPresenter.cancelLike(portalId);
    }

    public void follow(int userId) {
        mPresenter.follow(userId);
    }

    public void cancelFollow(int userId) {
        mPresenter.cancelFollow(userId);
    }

    public void commentLike(int commentId, int portalId, boolean isParent) {
        mPresenter.commentLike(commentId, portalId, isParent);
    }

    public void commentCancelLike(int commentId, int portalId, boolean isParent) {
        mPresenter.commentCancelLike(commentId, portalId, isParent);
    }

    public void getCommentList(boolean pullToRefresh) {
        mPresenter.getCommentList(getPortalId(), pullToRefresh);
    }

    public void replyComment(CommentPostBean bean) {
        mPresenter.replyComment(bean);
    }

    public void getQiniuToken(String commentKey, boolean isFromImage, List<MediaBean> selectedList, String videoThumbPath) {
        mPresenter.getQiniuToken(commentKey, isFromImage, selectedList, videoThumbPath);
    }

    public void deleteComment(int commentId, int parentId) {
        mPresenter.deleteComment(commentId, parentId);
    }
}
