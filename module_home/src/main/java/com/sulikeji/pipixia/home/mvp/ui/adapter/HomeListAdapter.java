package com.sulikeji.pipixia.home.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.sulikeji.pipixia.home.R;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.armscomponent.commoncore.DescUtils;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ContentBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ImageListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commoncore.message.mvp.ui.adapter.FeedImageAdapter;
import me.jessyan.armscomponent.commonui.base.mvp.ui.adapter.BaseRecyclerAdapter;
import me.jessyan.armscomponent.commonui.easyat.SpanFactory;
import me.jessyan.armscomponent.commonui.util.ToastUtils;
import me.jessyan.armscomponent.commonui.video.FeedGSYVideoPlayer;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;
import timber.log.Timber;

import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_FAIL;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_SUCCESS;

public class HomeListAdapter extends BaseRecyclerAdapter<FeedListBean, BaseViewHolder> {
    private static final int ITEM_TYPE_TEXT = 0;
    private static final int ITEM_TYPE_IMAGE = 1;
    private static final int ITEM_TYPE_VIDEO = 2;

    private final int PADDING_START;
    private final int PADDING_END;
    private final int IMAGE_MAX_WIDTH;
    private final int IMAGE_MAX_HEGIHT;
    private final int IMAGE_ADAPT_WIDTH;
    private final int IMAGE_ADAPT_HEGIHT;
    private final int GOD_IMAGE_MAX_WIDTH;
    private final int GOD_IMAGE_MAX_HEGIHT;
    private final int GOD_IMAGE_ADAPT_WIDTH;
    private final int GOD_IMAGE_ADAPT_HEGIHT;

    public HomeListAdapter(@Nullable List<FeedListBean> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<FeedListBean>() {
            @Override
            protected int getItemType(FeedListBean bean) {
                switch (bean.getContentType()) {
                    case FeedListBean.CONTENT_TYPE_TEXT:
                    default:
                        return ITEM_TYPE_TEXT;
                    case FeedListBean.CONTENT_TYPE_IMAGE:
                        return ITEM_TYPE_IMAGE;
                    case FeedListBean.CONTENT_TYPE_VIDEO:
                        return ITEM_TYPE_VIDEO;
                }
            }
        });
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_TEXT, R.layout.home_item_feed_text);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_IMAGE, R.layout.home_item_feed_image);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_VIDEO, R.layout.home_item_feed_video);
        //init config
        PADDING_START = AutoSizeUtils.dp2px(Utils.getApp(), 40);
        PADDING_END = AutoSizeUtils.dp2px(Utils.getApp(), 40);
        IMAGE_MAX_WIDTH = ScreenUtils.getScreenSize(Utils.getApp())[0] - PADDING_START - PADDING_END;
        IMAGE_MAX_HEGIHT = IMAGE_MAX_WIDTH;
        IMAGE_ADAPT_WIDTH = IMAGE_MAX_WIDTH / 2;
        IMAGE_ADAPT_HEGIHT = IMAGE_ADAPT_WIDTH;
        GOD_IMAGE_MAX_WIDTH = ScreenUtils.getScreenSize(Utils.getApp())[0] - PADDING_START - PADDING_END - AutoSizeUtils.dp2px(Utils.getApp(), 40) * 2;
        GOD_IMAGE_MAX_HEGIHT = GOD_IMAGE_MAX_WIDTH;
        GOD_IMAGE_ADAPT_WIDTH = GOD_IMAGE_MAX_WIDTH / 3;
        GOD_IMAGE_ADAPT_HEGIHT = GOD_IMAGE_ADAPT_WIDTH;
    }

    private FeedImageAdapter.OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(FeedImageAdapter.OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedListBean item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_TEXT:
                handleText(helper, item);
                break;
            case ITEM_TYPE_IMAGE:
                handleImage(helper, item);
                break;
            case ITEM_TYPE_VIDEO:
                handleVideo(helper, item);
                break;
        }
    }

    private void updateCustomUI(BaseViewHolder helper, FeedListBean item) {
        //处理顶部
        processUserLayout(helper, item);
        //处理底部
        processInputLayout(helper, item);
        //处理文本
        processFeedText(helper, item);
        //处理神评
        processGodLayout(helper, item);
    }

    private void processUserLayout(BaseViewHolder helper, FeedListBean item) {
        UserBean userBean = item.getUserBean();
        helper.setText(R.id.userNickname, userBean.getUserNickname());
        helper.setText(R.id.userDesc, userBean.getSignature());
        ImageView userAvatar = helper.getView(R.id.userAvatar);
        Glide.with(mContext)
                .load(userBean.getUserAvatar())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.public_placeholder)
                        .error(R.color.public_error))
                .into(userAvatar);
        //反馈功能
        helper.addOnClickListener(R.id.userFeedback);
    }

    private void processInputLayout(BaseViewHolder helper, FeedListBean item) {
        //赞
        ImageView bottomLikeBtn = helper.getView(R.id.bottomLikeBtn);
        bottomLikeBtn.setSelected(item.getIsLike() == STATUS_SUCCESS);
        TextView bottomLikeText = helper.getView(R.id.bottomLikeText);
        bottomLikeText.setSelected(item.getIsLike() == STATUS_SUCCESS);
        bottomLikeText.setText(getLikeText(item.getLikeCount()));
        helper.addOnClickListener(R.id.bottomLikeLayout);
        //踩
        ImageView bottomDislikeBtn = helper.getView(R.id.bottomDislikeBtn);
        bottomDislikeBtn.setSelected(item.getIsLike() == STATUS_FAIL);
        TextView bottomDislikeText = helper.getView(R.id.bottomDislikeText);
        bottomDislikeText.setSelected(item.getIsLike() == STATUS_FAIL);
        bottomDislikeText.setText(getDislikeText(item.getDislikeCount()));
        helper.addOnClickListener(R.id.bottomDislikeLayout);
        //评论
        TextView bottomCommentText = helper.getView(R.id.bottomCommentText);
        bottomCommentText.setText(getCommentText(item.getCommentCount()));
        helper.addOnClickListener(R.id.bottomCommentLayout);
        //转发
        TextView bottomShareText = helper.getView(R.id.bottomShareText);
        bottomShareText.setText(getShareText(item.getShareCount()));
        helper.addOnClickListener(R.id.bottomShareLayout);
    }

    //处理点赞
    private String getLikeText(int likeCount) {
        String likeText;
        if (likeCount > 0) {
            likeText = DescUtils.getLikeText(likeCount);
        } else {
            likeText = "赞";
        }
        return likeText;
    }

    //处理踩
    private String getDislikeText(int dislikeCount) {
        return "踩";
    }

    //处理评论
    private String getCommentText(int commentCount) {
        String commentText;
        if (commentCount > 0) {
            commentText = DescUtils.getCommentText(commentCount);
        } else {
            commentText = "评论";
        }
        return commentText;
    }

    //处理分享
    private String getShareText(int shareCount) {
        String shareText;
        if (shareCount > 0) {
            shareText = DescUtils.getShareText(shareCount);
        } else {
            shareText = "分享";
        }
        return shareText;
    }

    private void processFeedText(BaseViewHolder helper, FeedListBean item) {
        View feedTextLayout = helper.getView(R.id.feedTextLayout);
        TextView feedText = helper.getView(R.id.feedText);
        feedText.setText("", TextView.BufferType.EDITABLE);
        feedText.setMovementMethod(LinkMovementMethod.getInstance());
        TextView feedTextUnfold = helper.getView(R.id.feedTextUnfold);
        List<ContentBean> contentList = item.getContentList();
        if (contentList != null && !contentList.isEmpty()) {
            feedTextLayout.setVisibility(View.VISIBLE);
            for (ContentBean contentBean : contentList) {
                if (ContentBean.TAG_AT.equals(contentBean.getTag())) {
                    ContentBean bean = new ContentBean();
                    bean.setTag(contentBean.getTag());
                    bean.setUserId(contentBean.getUserId());
                    bean.setUserName(contentBean.getUserName());
                    bean.setOnClickListener((widget, userId, userName) -> {
                        ToastUtils.normal("user = " + userId + " ,userName = " + userName);
                    });
                    feedText.append(SpanFactory.newSpannable(bean.getSpannedName(), bean));
                } else {
                    feedText.append(contentBean.getContent());
                }
            }
        } else {
            feedTextLayout.setVisibility(View.GONE);
        }
        //是否显示展开
        feedTextUnfold.post(() -> {
            int lineCount = feedText.getLineCount();
            if (lineCount > 7) {
                feedTextUnfold.setVisibility(View.VISIBLE);
                float lineWidth = feedText.getLayout().getLineWidth(6);
                int[] screenSize = ScreenUtils.getScreenSize(mContext);
                int screenWidth = screenSize[0];
                int paddingLeft = mContext.getResources().getDimensionPixelSize(R.dimen.home_feed_text_padding_left);
                int paddingRight = mContext.getResources().getDimensionPixelSize(R.dimen.home_feed_text_padding_right);
                int realWidth = screenWidth - paddingLeft - paddingRight;
                int textUnfoldWidth = feedTextUnfold.getWidth();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) feedTextUnfold.getLayoutParams();
                if (realWidth - lineWidth >= textUnfoldWidth) {
                    if (layoutParams != null) {
                        layoutParams.leftMargin = (int) lineWidth;
                        feedTextUnfold.setLayoutParams(layoutParams);
                    }
                } else {
                    if (layoutParams != null) {
                        layoutParams.leftMargin = realWidth - textUnfoldWidth;
                        feedTextUnfold.setLayoutParams(layoutParams);
                    }
                }
            } else {
                feedTextUnfold.setVisibility(View.GONE);
            }
        });
    }

    //================================================================God UI
    private void processGodLayout(BaseViewHolder helper, FeedListBean item) {
        List<FeedListBean> godCommentList = item.getGodCommentList();
        View godParentLayout = helper.getView(R.id.godParentLayout);
        if (godCommentList != null && !godCommentList.isEmpty()) {
            FeedListBean godItem = godCommentList.get(0);
            if (godItem == null) {
                godParentLayout.setVisibility(View.GONE);
                return;
            }
            godParentLayout.setVisibility(View.VISIBLE);
            processGodUser(helper, godItem);//用户
            processGodLike(helper, godItem);//赞
            processGodText(helper, godItem);//文本
            processGodImage(helper, godItem);//图片
        } else {
            godParentLayout.setVisibility(View.GONE);
        }
    }

    private void processGodUser(BaseViewHolder helper, FeedListBean godItem) {
        UserBean userBean = godItem.getUserBean();
        helper.setText(R.id.godUserNickname, userBean.getUserNickname());
        ImageView godUserAvatar = helper.getView(R.id.godUserAvatar);
        Glide.with(mContext)
                .load(userBean.getUserAvatar())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.public_placeholder)
                        .error(R.color.public_error))
                .into(godUserAvatar);
    }

    private void processGodLike(BaseViewHolder helper, FeedListBean godItem) {
        ImageView godLike = helper.getView(R.id.godLike);
        godLike.setSelected(godItem.getIsLike() == STATUS_SUCCESS);
        TextView godLikeCount = helper.getView(R.id.godLikeCount);
        godLikeCount.setSelected(godItem.getIsLike() == STATUS_SUCCESS);
        godLikeCount.setText(DescUtils.getLikeText(godItem.getLikeCount()));
        helper.addOnClickListener(R.id.godLike);
    }

    private void processGodText(BaseViewHolder helper, FeedListBean godItem) {
        TextView godContent = helper.getView(R.id.godContent);
        godContent.setText("", TextView.BufferType.EDITABLE);
        godContent.setMovementMethod(LinkMovementMethod.getInstance());
        List<ContentBean> contentList = godItem.getContentList();
        if (contentList != null && !contentList.isEmpty()) {
            godContent.setVisibility(View.VISIBLE);
            for (ContentBean contentBean : contentList) {
                if (ContentBean.TAG_AT.equals(contentBean.getTag())) {
                    ContentBean bean = new ContentBean();
                    bean.setTag(contentBean.getTag());
                    bean.setUserId(contentBean.getUserId());
                    bean.setUserName(contentBean.getUserName());
                    bean.setOnClickListener((widget, userId, userName) -> {
                        ToastUtils.normal("user = " + userId + " ,userName = " + userName);
                    });
                    godContent.append(SpanFactory.newSpannable(bean.getSpannedName(), bean));
                } else {
                    godContent.append(contentBean.getContent());
                }
            }
        } else {
            godContent.setVisibility(View.GONE);
        }
    }

    private void processGodImage(BaseViewHolder helper, FeedListBean godItem) {
        RecyclerView godRecyclerView = helper.getView(R.id.godRecyclerView);
        if (godItem.getImageList() != null && !godItem.getImageList().isEmpty()) {
            List<ImageListBean> list = new ArrayList<>();
            list.add(new ImageListBean(godItem.getImageList()));
            FeedImageAdapter adapter = new FeedImageAdapter(list);
            adapter.setMaxWidth(GOD_IMAGE_MAX_WIDTH);
            adapter.setMaxHeight(GOD_IMAGE_MAX_HEGIHT);
            adapter.setAdaptWidth(GOD_IMAGE_ADAPT_WIDTH);
            adapter.setAdaptHeight(GOD_IMAGE_ADAPT_HEGIHT);
            adapter.setOnImageClickListener((imageView, position, imageList) -> {
                if (onImageClickListener != null)
                    onImageClickListener.onItemClick(imageView, position, imageList);
            });
            godRecyclerView.setAdapter(adapter);
            godRecyclerView.setVisibility(View.VISIBLE);
        } else {
            FeedImageAdapter adapter = new FeedImageAdapter(null);
            godRecyclerView.setAdapter(adapter);
            godRecyclerView.setVisibility(View.GONE);
        }
    }

    //================================================================handle UI
    private void handleText(BaseViewHolder helper, FeedListBean item) {
        updateCustomUI(helper, item);
    }

    private void handleImage(BaseViewHolder helper, FeedListBean item) {
        updateCustomUI(helper, item);
        RecyclerView recyclerView = helper.getView(R.id.recyclerView);
        if (item.getImageList() != null && !item.getImageList().isEmpty()) {
            List<ImageListBean> list = new ArrayList<>();
            list.add(new ImageListBean(item.getImageList()));
            FeedImageAdapter adapter = new FeedImageAdapter(list);
            adapter.setMargin(PADDING_START);
            adapter.setMaxWidth(IMAGE_MAX_WIDTH);
            adapter.setMaxHeight(IMAGE_MAX_HEGIHT);
            adapter.setAdaptWidth(IMAGE_ADAPT_WIDTH);
            adapter.setAdaptHeight(IMAGE_ADAPT_HEGIHT);
            adapter.setOnImageClickListener((imageView, position, imageList) -> {
                if (onImageClickListener != null)
                    onImageClickListener.onItemClick(imageView, position, imageList);
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            FeedImageAdapter adapter = new FeedImageAdapter(null);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public static final String PLAY_TAG = "RecyclerView2List";

    private void handleVideo(BaseViewHolder helper, FeedListBean item) {
        updateCustomUI(helper, item);
        View videoLayout = helper.getView(R.id.videoLayout);
        FeedGSYVideoPlayer videoPlayer = helper.getView(R.id.videoPlayer);
        ResourceBean videoBean = item.getVideoBean();
        processVideoSize(videoBean, videoLayout);//适配大小
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        ImageView thumbImageView;
        if (videoPlayer.getThumbImageView() == null) {
            thumbImageView = new ImageView(mContext);
            thumbImageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            gsyVideoOptionBuilder.setThumbImageView(thumbImageView);
        } else {
            thumbImageView = (ImageView) videoPlayer.getThumbImageView();
        }
        Glide.with(mContext).asBitmap().load(videoBean == null ? "" : videoBean.getThumbnail()).into(thumbImageView);
        gsyVideoOptionBuilder.setIsTouchWiget(false)
                .setUrl(videoBean == null ? "" : videoBean.getPath())
                .setVideoTitle("")
                .setCacheWithPlay(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setPlayTag(PLAY_TAG)
                .setPlayPosition(helper.getLayoutPosition())
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
                        helper.setGone(R.id.shareLayout, true);

                    }
                })
                .build(videoPlayer);
        videoPlayer.getFullscreenButton().setOnClickListener(v -> {
            //TODO:
            ToastUtils.normal("getFullscreenButton onClick");
        });
        //分享布局
        helper.setGone(R.id.shareLayout, false);
        helper.addOnClickListener(R.id.shareWechat);
        helper.addOnClickListener(R.id.shareQQ);
        helper.addOnClickListener(R.id.shareWechatMoments);
        helper.addOnClickListener(R.id.shareQzone);
        helper.addOnClickListener(R.id.shareReplay);
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
}
