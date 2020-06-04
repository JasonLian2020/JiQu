package com.sulikeji.pipixia.home.mvp.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.sulikeji.pipixia.home.R;
import com.sulikeji.pipixia.home.R2;
import com.sulikeji.pipixia.home.di.component.DaggerHomeDetailComponent;
import com.sulikeji.pipixia.home.mvp.contract.HomeDetailContract;
import com.sulikeji.pipixia.home.mvp.model.entity.PopupListBean;
import com.sulikeji.pipixia.home.mvp.presenter.HomeDetailPresenter;
import com.sulikeji.pipixia.home.mvp.ui.adapter.HomeListAdapter;
import com.sulikeji.pipixia.home.mvp.ui.adapter.PopupListAdapter;
import com.sulikeji.pipixia.home.mvp.ui.decoration.FeedVerticalInset;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import me.jessyan.armscomponent.commoncore.ShareUtils;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;
import me.jessyan.armscomponent.commonsdk.base.BaseLazyFragment;
import me.jessyan.armscomponent.commonservice.EventBusHub;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.mob.ShareHelper;
import me.jessyan.armscomponent.commonui.mob.ShareParams;
import me.jessyan.armscomponent.commonui.popup.DownloadPopup;
import me.jessyan.armscomponent.commonui.popup.ListPopup;
import me.jessyan.armscomponent.commonui.share.ShareOptions;
import me.jessyan.armscomponent.commonui.share.SharePopup;
import me.jessyan.armscomponent.commonui.share.SharePopupUtils;
import me.jessyan.armscomponent.commonui.share.ShareType;
import me.jessyan.armscomponent.commonui.util.DownloadUtils;
import me.jessyan.armscomponent.commonui.util.ToastUtils;
import me.jessyan.armscomponent.commonui.video.FeedGSYVideoPlayer;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;
import razerdp.basepopup.BasePopupWindow;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_VIDEO;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_FAIL;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_NORMAL;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_SUCCESS;


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
@Route(path = RouterHub.HOME_HOMEDETAILFRAGMENT)
public class HomeDetailFragment extends BaseLazyFragment<HomeDetailPresenter> implements HomeDetailContract.View {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Inject
    Dialog mDialog;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    BaseQuickAdapter<FeedListBean, BaseViewHolder> mAdapter;

    private int position;
    private int id;

    private ListPopup listPopup;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) return;
        position = getArguments().getInt(RouterHub.HOME_HOMEDETAILFRAGMENT_KEY_POSITION, -1);
        id = getArguments().getInt(RouterHub.HOME_HOMEDETAILFRAGMENT_KEY_ID, -1);
        Timber.tag("jason").d("position = " + position);
        Timber.tag("jason").d("id = " + id);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_detail, container, false);
    }

    @Override
    protected void initViewConfig() {
        initRecyclerView();
    }

    @Override
    protected void initData() {
        //自动刷新，会回调下拉刷新请求数据
        refreshLayout.autoRefresh();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
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

    @Override
    public void finishRefresh(boolean success, boolean emptyResponse, boolean noMoreData) {
        if (emptyResponse) refreshLayout.finishRefreshWithNoMoreData();
        else refreshLayout.finishRefresh(success);
        refreshLayout.setEnableLoadMore(true);

    }

    @Override
    public void finishLoadMore(boolean success, boolean emptyResponse, boolean noMoreData) {
        if (emptyResponse) refreshLayout.finishLoadMoreWithNoMoreData();
        else refreshLayout.finishLoadMore(success);
        refreshLayout.setEnableLoadMore(!noMoreData);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public int getCategoryId() {
        return id;
    }

    @Override
    public void showFeedbackPopup(FeedListBean item, int position, View view) {
        view.setSelected(true);
        //显示弹窗
        int marginleft = 40;//距离左侧屏幕距离
        //计算弹窗宽度
        int width = AutoSizeConfig.getInstance().getScreenWidth() - AutoSizeUtils.dp2px(mContext, marginleft) * 2;
        //获取在整个屏幕内的绝对坐标,包括通知栏
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        //点击距离屏幕顶部的距离
        int clickDistance = location[1];
        //屏幕高度(除去虚拟导航栏)
        int screenHeight = ScreenUtils.getScreenSize(mContext)[1];
        //是否从上至下
        boolean isUpPopup = screenHeight / 2 > clickDistance;
        //x坐标
        int x = AutoSizeUtils.dp2px(mContext, marginleft);
        //y坐标
        int y = isUpPopup ? clickDistance + view.getHeight() : clickDistance - AutoSizeUtils.dp2px(mContext, 107) * 5;
        listPopup = new ListPopup.Builder(mContext)
                .setWidth(width)
                .setUpPopup(isUpPopup)
                .setLayoutManager(new LinearLayoutManager(mContext))
                .setAdapter(getPopupListAdapter(item, position))
                .build();
        listPopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                view.setSelected(false);
            }
        });
        listPopup.showPopupWindow(x, y);
    }

    private void initRecyclerView() {
        refreshLayout.setOnRefreshListener(refreshLayout -> mPresenter.requestDatas(true));
        refreshLayout.setOnLoadMoreListener(refreshLayout -> mPresenter.requestDatas(false));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new FeedVerticalInset(AutoSizeUtils.dp2px(mContext, 10), R.color.home_feed_divider));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FeedListBean item = (FeedListBean) adapter.getItem(position);
            if (item == null) return;
            if (view.getId() == R.id.bottomLikeLayout) {
                childClickLike(item, position);
            } else if (view.getId() == R.id.bottomDislikeLayout) {
                childClickDisLike(item, position);
            } else if (view.getId() == R.id.bottomCommentLayout) {
                childClickComment(item, position);
            } else if (view.getId() == R.id.bottomShareLayout) {
                childClickShare(item, position);
            } else if (view.getId() == R.id.userFeedback) {
                clickFeedback(item, position, view);
            } else if (view.getId() == R.id.shareWechat) {
                shareByWechat(item);
            } else if (view.getId() == R.id.shareQQ) {
                shareByQQ(item);
            } else if (view.getId() == R.id.shareWechatMoments) {
                shareByWechatMoments(item);
            } else if (view.getId() == R.id.shareQzone) {
                shareByQzone(item);
            } else if (view.getId() == R.id.shareReplay) {
                childClickReplay(item, position);
            }
        });
        ((HomeListAdapter) mAdapter).setOnImageClickListener((imageView, position, imageList) -> {
            ARouter.getInstance()
                    .build(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY)
                    .withInt(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_POSITION, position)
                    .withParcelableArrayList(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_IMAGELIST, (ArrayList<ResourceBean>) imageList)
                    .withTransition(R.anim.public_preview_in_enter, R.anim.public_preview_in_exit)
                    .navigation(mContext);
        });
        mAdapter.bindToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(HomeListAdapter.PLAY_TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if (!GSYVideoManager.isFullState(getActivity())) {
                            GSYVideoManager.releaseAllVideos();
                            mAdapter.notifyItemChanged(position);
                        }
                    }
                }
            }
        });
    }

    private BaseQuickAdapter popupListAdapter;

    private BaseQuickAdapter getPopupListAdapter(FeedListBean item, int position) {
        if (popupListAdapter == null) {
            List<PopupListBean> data = new ArrayList<>();
            data.add(new PopupListBean(R.mipmap.home_icon_no_interesting, R.string.home_title_no_interesting));
            data.add(new PopupListBean(R.mipmap.home_icon_repetition_content, R.string.home_title_repetition_content));
            data.add(new PopupListBean(R.mipmap.home_icon_bad_content, R.string.home_title_bad_content));
            data.add(new PopupListBean(R.mipmap.home_icon_ban_author, R.string.home_title_ban_author));
            data.add(new PopupListBean(R.mipmap.home_icon_ban_topic, R.string.home_title_ban_topic));
            popupListAdapter = new PopupListAdapter(data);
            popupListAdapter.setOnItemClickListener((adapter, view, itemPosition) -> {
                //弹窗消失
                listPopup.dismiss();
                //更新UI
                mAdapter.remove(position);
                //请求接口
                mPresenter.feedback(item.getId(), position);
            });
        }
        return popupListAdapter;
    }

    private void clickFeedback(FeedListBean item, int position, View view) {
        mPresenter.showFeedback(item, position, view);
    }

    private void childClickReplay(FeedListBean item, int position) {
        View shareLayout = mAdapter.getViewByPosition(position + mAdapter.getHeaderLayoutCount(), R.id.shareLayout);
        FeedGSYVideoPlayer videoPlayer = (FeedGSYVideoPlayer) mAdapter.getViewByPosition(position + mAdapter.getHeaderLayoutCount(), R.id.videoPlayer);
        shareLayout.setVisibility(View.GONE);
        videoPlayer.startPlayLogic();
    }

    private void childClickLike(FeedListBean item, int position) {
        if (item.getIsLike() == STATUS_SUCCESS) mPresenter.cancelLike(item, position);
        else mPresenter.confirmLike(item, position);
    }

    private void childClickDisLike(FeedListBean item, int position) {
        if (item.getIsLike() == STATUS_FAIL) mPresenter.cancelDislike(item, position);
        else mPresenter.confirmDislike(item, position);
    }

    private void childClickComment(FeedListBean item, int position) {
        ARouter.getInstance()
                .build(RouterHub.PORTAL_PORTALDETAILACTIVITY)
                .withInt(RouterHub.PORTAL_PORTALDETAILACTIVITY_KEY_CONTENTTYPE, item.getContentType())
                .withInt(RouterHub.PORTAL_PORTALDETAILACTIVITY_KEY_PORTALID, item.getId())
                .withParcelable(RouterHub.PORTAL_PORTALDETAILACTIVITY_KEY_ITEM, item)
                .navigation(mContext);
    }

    private void childClickShare(FeedListBean item, int position) {
        SharePopup sharePopup = SharePopupUtils.getSharePopup(mContext, new ShareOptions.Builder()
                .favorite(item.getIsFavorite() == STATUS_SUCCESS)
                .downloadVideo(item.getContentType() == CONTENT_TYPE_VIDEO)
                .build());
        sharePopup.setOnItemClickListener((iconId, titleId, clickTag) -> {
            switch (clickTag) {
                case ShareType.SHARE_TYPE_WECHAT:
                    shareByWechat(item);
                    break;
                case ShareType.SHARE_TYPE_QQ:
                    shareByQQ(item);
                    break;
                case ShareType.SHARE_TYPE_WECHAT_MOMENTS:
                    shareByWechatMoments(item);
                    break;
                case ShareType.SHARE_TYPE_QZONE:
                    shareByQzone(item);
                    break;
                case ShareType.SHARE_TYPE_COMPLAIN:
                    showMessage("举报");
                    break;
                case ShareType.SHARE_TYPE_FAVORITE:
                    mPresenter.collectPortal(item);
                    break;
                case ShareType.SHARE_TYPE_UNFAVORITE:
                    mPresenter.cancelCollectPortal(item);
                    break;
                case ShareType.SHARE_TYPE_DOWNLOAD_IMAGE:
                    showMessage("保存图片");
                    break;
                case ShareType.SHARE_TYPE_DOWNLOAD_VIDEO:
                    clickDownloadVideo(item);
                    break;
            }
        });
        sharePopup.showPopupWindow();
    }

    private String getUrl() {
        return "https://www.baidu.com/";
    }

    private void shareByWechat(FeedListBean item) {
        int contentType = item.getContentType();
        List<ResourceBean> imageList = item.getImageList();
        ResourceBean videoBean = item.getVideoBean();
        ShareHelper.shareByWechat(
                new ShareParams.Builder()
                        .setShareType(Platform.SHARE_WEBPAGE)
                        .setTitle(ShareUtils.getShareTitle(item.getContentList()))
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

    private void shareByWechatMoments(FeedListBean item) {
        int contentType = item.getContentType();
        List<ResourceBean> imageList = item.getImageList();
        ResourceBean videoBean = item.getVideoBean();
        ShareHelper.shareByWechatMoments(
                new ShareParams.Builder()
                        .setShareType(Platform.SHARE_WEBPAGE)
                        .setTitle(ShareUtils.getShareTitle(item.getContentList()))
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

    private void shareByQQ(FeedListBean item) {
        int contentType = item.getContentType();
        List<ResourceBean> imageList = item.getImageList();
        ResourceBean videoBean = item.getVideoBean();
        ShareHelper.shareByQQ(
                new ShareParams.Builder()
                        .setTitle(ShareUtils.getShareTitle(item.getContentList()))
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

    private void shareByQzone(FeedListBean item) {
        ShareHelper.shareByQzone(
                new ShareParams.Builder()
                        .setTitle(ShareUtils.getShareTitle(item.getContentList()))
                        .setText(ShareUtils.getShareText())
                        .setTitleUrl(getUrl())
                        .setSite(ShareUtils.getShareTitle(item.getContentList()))
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

    private int getIndexByPortalId(int portalId) {
        int index = -1;
        List<FeedListBean> list = mAdapter.getData();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                FeedListBean bean = list.get(i);
                if (bean == null) continue;
                if (bean.getId() == portalId) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private void updateFollwByUserId(int userId, boolean isFollow) {
        if (userId == -1) return;
        List<FeedListBean> list = mAdapter.getData();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                FeedListBean bean = list.get(i);
                if (bean == null || bean.getUserBean() == null) continue;
                if (bean.getUserBean().getUserId() == userId) {
                    bean.setIsFollow(isFollow ? STATUS_SUCCESS : STATUS_NORMAL);
                }
            }
        }
    }

    //=========================================================download
    private void clickDownloadVideo(FeedListBean item) {
        //判断数据
        ResourceBean videoBean = item.getVideoBean();
        if (videoBean == null) {
            ToastUtils.normal("下载数据丢失，请重试");
            return;
        }
        String videoSavePath = DownloadUtils.getVideoSavePath(videoBean.getPath());
        if (new File(videoSavePath).exists()) {
            //======已存在
            ToastUtils.normal("已保存至DCIM/JiQu文件夹");
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
                ToastUtils.normal("已保存至DCIM/JiQu文件夹");
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
        int index = getIndexByPortalId(portalId);
        if (index == -1) return;
        FeedListBean bean = mAdapter.getData().get(index);
        bean.setIsFavorite(STATUS_SUCCESS);
        bean.setFavoriteId(favoriteId);
    }

    @Subscriber(tag = EventBusHub.PORTAL_CANCELFAVORITESUCCESS, mode = ThreadMode.MAIN)
    private void cancelFavoriteSuccess(Bundle bundle) {
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int favoriteId = bundle.getInt(EventBusHub.PORTAL_KEY_FAVORITEID, -1);
        int index = getIndexByPortalId(portalId);
        if (index == -1) return;
        FeedListBean bean = mAdapter.getData().get(index);
        bean.setIsFavorite(STATUS_NORMAL);
        bean.setFavoriteId(favoriteId);
    }

    @Subscriber(tag = EventBusHub.PORTAL_LIKESUCCESS, mode = ThreadMode.MAIN)
    private void likeSuccess(Bundle bundle) {
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int likeCount = bundle.getInt(EventBusHub.PORTAL_KEY_LIKECOUNT, -1);
        int index = getIndexByPortalId(portalId);
        if (index == -1) return;
        FeedListBean bean = mAdapter.getData().get(index);
        bean.setIsLike(STATUS_SUCCESS);
        bean.setLikeCount(likeCount);
        mAdapter.notifyItemChanged(index);
    }

    @Subscriber(tag = EventBusHub.PORTAL_CANCELLIKESUCCESS, mode = ThreadMode.MAIN)
    private void cancelLikeSuccess(Bundle bundle) {
        int portalId = bundle.getInt(EventBusHub.PORTAL_KEY_PORTALID, -1);
        int likeCount = bundle.getInt(EventBusHub.PORTAL_KEY_LIKECOUNT, -1);
        int index = getIndexByPortalId(portalId);
        if (index == -1) return;
        FeedListBean bean = mAdapter.getData().get(index);
        bean.setIsLike(STATUS_NORMAL);
        bean.setLikeCount(likeCount);
        mAdapter.notifyItemChanged(index);
    }

    @Subscriber(tag = EventBusHub.PORTAL_FOLLOWSUCCESS, mode = ThreadMode.MAIN)
    private void followSuccess(Bundle bundle) {
        int userId = bundle.getInt(EventBusHub.PORTAL_KEY_USERID, -1);
        updateFollwByUserId(userId, true);
    }

    @Subscriber(tag = EventBusHub.PORTAL_CANCELFOLLOWSUCCESS, mode = ThreadMode.MAIN)
    private void cancelFollowSuccess(Bundle bundle) {
        int userId = bundle.getInt(EventBusHub.PORTAL_KEY_USERID, -1);
        updateFollwByUserId(userId, false);
    }
}
