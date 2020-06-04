package com.sulikeji.pipixia.portal.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.sulikeji.pipixia.portal.R;
import com.sulikeji.pipixia.portal.R2;
import com.sulikeji.pipixia.portal.di.component.DaggerPortalImageComponent;
import com.sulikeji.pipixia.portal.mvp.contract.PortalImageContract;
import com.sulikeji.pipixia.portal.mvp.presenter.PortalImagePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ImageListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;
import me.jessyan.armscomponent.commoncore.message.mvp.ui.adapter.FeedImageAdapter;
import me.jessyan.armscomponent.commonsdk.utils.FragmentUtil;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.util.ToastUtils;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/17/2019 15:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class PortalImageFragment extends PortalBaseFragment<PortalImagePresenter> implements PortalImageContract.View {
    @BindView(R2.id.public_titlebar_left)
    ImageView titlebarLeft;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
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

    @Inject
    BaseQuickAdapter mAdapter;

    private ImageView userAvatar;
    private TextView userNickname;
    private TextView userTime;
    private Button userFollow;
    private TextView portalContent;
    private RecyclerView imageRecyclerView;
    private FeedImageAdapter imageAdapter;

    public static PortalImageFragment newInstance() {
        PortalImageFragment fragment = new PortalImageFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerPortalImageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.portal_fragment_image, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        initRecyclerView();
        initInputLayout();
    }

    private void initTitleBar() {
        //标题
        FragmentUtil.setTitle(this, R.string.portal_detail_name);
        //左边按钮
        titlebarLeft.setVisibility(View.VISIBLE);
        titlebarLeft.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private View initHeaderView() {
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.portal_header_image, recyclerView, false);
        //用户
        userAvatar = headerView.findViewById(R.id.userAvatar);
        userNickname = headerView.findViewById(R.id.userNickname);
        userTime = headerView.findViewById(R.id.userTime);
        userFollow = headerView.findViewById(R.id.userFollow);
        configUserAvatar(userAvatar);
        configUserNickname(userNickname);
        configUserTime(userTime);
        configUserFollow(userFollow);
        //帖子内容
        portalContent = headerView.findViewById(R.id.portalContent);
        configPortalContent(portalContent);
        //图片
        initImageLayout(headerView);
        return headerView;
    }

    private void initImageLayout(View headerView) {
        imageRecyclerView = headerView.findViewById(R.id.imageRecyclerView);
        int paddingStart = AutoSizeUtils.dp2px(Utils.getApp(), 30);
        int paddingEnd = AutoSizeUtils.dp2px(Utils.getApp(), 30);
        int imageMaxWidth = (ScreenUtils.getScreenSize(Utils.getApp())[0] - paddingStart - paddingEnd);
        int imageMaxHegiht = imageMaxWidth;
        int imageAdaptWidth = imageMaxWidth / 2;
        int imageAdaptHegiht = imageAdaptWidth;
        imageAdapter = new FeedImageAdapter(null);
        imageAdapter.setMargin(paddingStart);
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
                    .navigation(mContext);
        });
        imageRecyclerView.setAdapter(imageAdapter);
        updateImageList(imageRecyclerView);
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

    private void updateImageList(RecyclerView imageRecyclerView) {
        FeedListBean item = getDetailActivity().getItem();
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
        //image
        updateImageList(imageRecyclerView);
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

    /**
     * 是否是（文字+图片）
     */
    private boolean isFromImageByContentType() {
        return getDetailActivity().getContentType() == FeedListBean.CONTENT_TYPE_IMAGE;
    }
}
