package com.sulikeji.pipixia.portal.mvp.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.sulikeji.pipixia.portal.R;

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
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_IMAGE;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_TEXT;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_VIDEO;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_SUCCESS;

public class PortalCommentAdapter extends BaseRecyclerAdapter<FeedListBean, BaseViewHolder> {
    private static final int ITEM_TYPE_TEXT = 1;
    private static final int ITEM_TYPE_IMAGE = 2;
    private static final int ITEM_TYPE_VIDEO = 3;

    private final int IMAGE_MAX_WIDTH;
    private final int IMAGE_MAX_HEGIHT;
    private final int IMAGE_ADAPT_WIDTH;
    private final int IMAGE_ADAPT_HEGIHT;

    public PortalCommentAdapter(@Nullable List<FeedListBean> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<FeedListBean>() {
            @Override
            protected int getItemType(FeedListBean bean) {
                switch (bean.getContentType()) {
                    case CONTENT_TYPE_TEXT:
                        return ITEM_TYPE_TEXT;
                    case CONTENT_TYPE_IMAGE:
                        return ITEM_TYPE_IMAGE;
                    case CONTENT_TYPE_VIDEO:
                        return ITEM_TYPE_VIDEO;
                }
                return ITEM_TYPE_TEXT;
            }
        });
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_TEXT, R.layout.portal_item_comment_text);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_IMAGE, R.layout.portal_item_comment_image);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_VIDEO, R.layout.portal_item_comment_video);
        //init config
        int paddingStart = AutoSizeUtils.dp2px(Utils.getApp(), 40);
        int paddingEnd = AutoSizeUtils.dp2px(Utils.getApp(), 40);
        IMAGE_MAX_WIDTH = ScreenUtils.getScreenSize(Utils.getApp())[0] - paddingStart - paddingEnd;
        IMAGE_MAX_HEGIHT = IMAGE_MAX_WIDTH;
        IMAGE_ADAPT_WIDTH = IMAGE_MAX_WIDTH / 2;
        IMAGE_ADAPT_HEGIHT = IMAGE_ADAPT_WIDTH;
    }

    private FeedImageAdapter.OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(FeedImageAdapter.OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    /**
     * 作者用户ID
     */
    private int mainUserId;

    public void setMainUserId(int mainUserId) {
        this.mainUserId = mainUserId;
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
        //处理头部(用户)
        processUserLayout(helper, item);
        //处理底部(输入)
        processInputLayout(helper, item);
        //处理文本
        processTextLayout(helper, item);
        //处理神评
        processGodLayout(helper, item);
    }

    private void processUserLayout(BaseViewHolder helper, FeedListBean item) {
        UserBean userBean = item.getUserBean();
        ImageView userAvatar = helper.getView(R.id.userAvatar);
        Glide.with(mContext)
                .load(userBean == null ? null : userBean.getUserAvatar())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.public_placeholder)
                        .error(R.color.public_error))
                .into(userAvatar);
        helper.setText(R.id.userNickname, userBean == null ? "" : userBean.getUserNickname());
        helper.setGone(R.id.userTag, userBean != null && userBean.getUserId() == mainUserId);
        helper.setText(R.id.commentTime, item.getCreateTime());
        helper.addOnClickListener(R.id.userAvatar);
    }

    private void processInputLayout(BaseViewHolder helper, FeedListBean item) {
        //回复
        TextView commentReply = helper.getView(R.id.commentReply);
        String commentReplyStr = item.getCommentCount() == 0 ? getString(R.string.portal_comment_empty_reply_text) : getString(R.string.portal_comment_reply_text, DescUtils.getReplyText(item.getCommentCount()));
        Drawable commentReplyDrawable = item.getCommentCount() == 0 ? null : getDrawable(R.mipmap.portal_icon_comment_reply);
        if (commentReplyDrawable != null)
            commentReplyDrawable.setBounds(0, 0, commentReplyDrawable.getMinimumWidth(), commentReplyDrawable.getMinimumHeight());
        commentReply.setText(commentReplyStr);
        commentReply.setCompoundDrawables(null, null, commentReplyDrawable, null);
        //赞
        ImageView commentLike = helper.getView(R.id.commentLike);
        TextView commentLikeCount = helper.getView(R.id.commentLikeCount);
        commentLike.setSelected(item.getIsLike() == STATUS_SUCCESS);
        commentLikeCount.setSelected(item.getIsLike() == STATUS_SUCCESS);
        commentLikeCount.setText(DescUtils.getLikeText(item.getLikeCount()));
        //监听
        helper.addOnClickListener(R.id.commentReply);
        helper.addOnClickListener(R.id.commentLike);
        helper.addOnClickListener(R.id.commentMore);
    }

    private void processTextLayout(BaseViewHolder helper, FeedListBean item) {
        TextView commentContent = helper.getView(R.id.commentContent);
        commentContent.setText("", TextView.BufferType.EDITABLE);
        commentContent.setMovementMethod(LinkMovementMethod.getInstance());
        List<ContentBean> contentList = item.getContentList();
        if (contentList != null && !contentList.isEmpty()) {
            commentContent.setVisibility(View.VISIBLE);
            for (ContentBean contentBean : contentList) {
                if (ContentBean.TAG_AT.equals(contentBean.getTag())) {
                    ContentBean bean = new ContentBean();
                    bean.setTag(contentBean.getTag());
                    bean.setUserId(contentBean.getUserId());
                    bean.setUserName(contentBean.getUserName());
                    bean.setOnClickListener((widget, userId, userName) -> {
                        ToastUtils.normal("user = " + userId + " ,userName = " + userName);
                    });
                    commentContent.append(SpanFactory.newSpannable(bean.getSpannedName(), bean));
                } else {
                    commentContent.append(contentBean.getContent());
                }
            }
        } else {
            commentContent.setVisibility(View.GONE);
        }
    }

    private void processGodLayout(BaseViewHolder helper, FeedListBean item) {
        helper.setGone(R.id.godTag, item.getIsGod() == STATUS_SUCCESS);
    }

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

    private void handleVideo(BaseViewHolder helper, FeedListBean item) {
        updateCustomUI(helper, item);
        View commentVideoLayout = helper.getView(R.id.commentVideoLayout);
        ImageView commentVideoCover = helper.getView(R.id.commentVideoCover);
        ResourceBean videoBean = item.getVideoBean();
        Glide.with(mContext)
                .load(videoBean == null ? null : videoBean.getThumbnail())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.public_placeholder)
                        .error(R.color.public_error))
                .into(commentVideoCover);
        processVideoSize(videoBean, commentVideoLayout);
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
}
