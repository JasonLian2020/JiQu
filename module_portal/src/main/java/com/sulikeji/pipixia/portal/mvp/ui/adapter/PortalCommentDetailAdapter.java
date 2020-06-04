package com.sulikeji.pipixia.portal.mvp.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
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
import me.jessyan.autosize.utils.ScreenUtils;

import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_IMAGE;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_TEXT;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_VIDEO;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.STATUS_SUCCESS;

public class PortalCommentDetailAdapter extends BaseRecyclerAdapter<FeedListBean, BaseViewHolder> {
    private static final int ITEM_TYPE_TEXT = 1;
    private static final int ITEM_TYPE_IMAGE = 2;
    private static final int ITEM_TYPE_VIDEO = 3;

    private final int PADDING_START;
    private final int PADDING_END;
    private final int IMAGE_MAX_WIDTH;
    private final int IMAGE_MAX_HEGIHT;
    private final int IMAGE_ADAPT_WIDTH;
    private final int IMAGE_ADAPT_HEGIHT;

    public PortalCommentDetailAdapter(@Nullable List<FeedListBean> data) {
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
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_TEXT, R.layout.portal_item_comment_detail_text);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_IMAGE, R.layout.portal_item_comment_detail_image);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_VIDEO, R.layout.portal_item_comment_detail_video);
        //init config
        PADDING_START = getDimensionPixelSize(R.dimen.portal_comment_dialog_padding_left);
        PADDING_END = getDimensionPixelOffset(R.dimen.portal_comment_dialog_padding_right);
        IMAGE_MAX_WIDTH = (ScreenUtils.getScreenSize(Utils.getApp())[0] - PADDING_START - PADDING_END);
        IMAGE_MAX_HEGIHT = IMAGE_MAX_WIDTH;
        IMAGE_ADAPT_WIDTH = IMAGE_MAX_WIDTH / 3;
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
        //处理引用
        processQuoteLayout(helper, item);
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
        View inputLayout = helper.getView(R.id.inputLayout);
        if (inputLayout.getPaddingStart() != PADDING_START)
            inputLayout.setPadding(PADDING_START, inputLayout.getPaddingTop(), inputLayout.getPaddingEnd(), inputLayout.getPaddingBottom());
        //回复
        TextView commentReply = helper.getView(R.id.commentReply);
        String commentReplyStr = item.getCommentCount() == 0 ? getString(R.string.portal_comment_empty_reply_text) : getString(R.string.portal_comment_reply_text, item.getCommentCount());
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
        if (commentContent.getPaddingStart() != PADDING_START)
            commentContent.setPadding(PADDING_START, commentContent.getPaddingTop(), commentContent.getPaddingEnd(), commentContent.getPaddingBottom());
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

    private void processQuoteLayout(BaseViewHolder helper, FeedListBean item) {
        View quoteParentLayout = helper.getView(R.id.quoteParentLayout);
        TextView quoteContent = helper.getView(R.id.quoteContent);
        RecyclerView quoteRecyclerView = helper.getView(R.id.quoteRecyclerView);
        if (item.getToParentId() != 0 && item.getToUserId() != 0 && item.getToParentInfo() != null) {
            quoteParentLayout.setVisibility(View.VISIBLE);
            FeedListBean parentItem = item.getToParentInfo();
            //引用-文字
            quoteContent.setText("", TextView.BufferType.EDITABLE);
            quoteContent.setMovementMethod(LinkMovementMethod.getInstance());
            List<ContentBean> contentList = parentItem.getContentList();
            //被回复人的名字
            ContentBean quoteContentBean = new ContentBean();
            quoteContentBean.setUserName(parentItem.getUserBean().getUserNickname());
            quoteContentBean.setUserId(parentItem.getUserBean().getUserId());
            quoteContentBean.setOnClickListener((widget, userId, userName) -> {
                ToastUtils.normal("user = " + userId + " ,userName = " + userName);
            });
            quoteContent.append(SpanFactory.newSpannable(quoteContentBean.getCommentName(), quoteContentBean));
            if (contentList != null && !contentList.isEmpty()) {
                for (ContentBean contentBean : contentList) {
                    if (ContentBean.TAG_AT.equals(contentBean.getTag())) {
                        ContentBean bean = new ContentBean();
                        bean.setTag(contentBean.getTag());
                        bean.setUserId(contentBean.getUserId());
                        bean.setUserName(contentBean.getUserName());
                        quoteContent.append(SpanFactory.newSpannable(bean.getSpannedName(), bean));
                    } else {
                        quoteContent.append(contentBean.getContent());
                    }
                }
            }
            //引用-图片
            List<ResourceBean> imageList = parentItem.getImageList();
            if (parentItem.getContentType() == CONTENT_TYPE_IMAGE && imageList != null && !imageList.isEmpty()) {
                List<ImageListBean> list = new ArrayList<>();
                list.add(new ImageListBean(imageList));
                FeedImageAdapter adapter = new FeedImageAdapter(list);
                adapter.setMaxWidth(IMAGE_MAX_WIDTH);
                adapter.setMaxHeight(IMAGE_MAX_HEGIHT);
                adapter.setAdaptWidth(IMAGE_ADAPT_WIDTH);
                adapter.setAdaptHeight(IMAGE_ADAPT_HEGIHT);
                quoteRecyclerView.setAdapter(adapter);
            } else {
                FeedImageAdapter adapter = new FeedImageAdapter(null);
                quoteRecyclerView.setAdapter(adapter);
            }
        } else {
            quoteParentLayout.setVisibility(View.GONE);
        }
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
        helper.setGone(R.id.commentVideoLayout, false);
    }
}
