package com.sulikeji.pipixia.publish.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.sulikeji.pipixia.publish.R;

import java.util.List;

import me.jason.imagepicker.internal.entity.SelectionSpec;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.MediaBean;

public class PublishDetailAdapter extends BaseQuickAdapter<MediaBean, BaseViewHolder> {
    private static final int ITEM_TYPE_IMAGE = 1;
    private static final int ITEM_TYPE_VIDEO = 2;
    private static final int ITEM_TYPE_ADD = 3;

    private int mImageResize;

    public PublishDetailAdapter(@Nullable List<MediaBean> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<MediaBean>() {
            @Override
            protected int getItemType(MediaBean bean) {
                if (isFromImage) {
                    if (MediaBean.NONE_PATH.equals(bean.getPath()))
                        return ITEM_TYPE_ADD;
                    else
                        return ITEM_TYPE_IMAGE;
                } else return ITEM_TYPE_VIDEO;
            }
        });
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_IMAGE, R.layout.publish_item_image);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_VIDEO, R.layout.publish_item_video);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_ADD, R.layout.publish_item_add);
    }

    /**
     * 区别图片和视频，true表示图片，false表示视频
     */
    private boolean isFromImage;

    public void setFromImage(boolean isFromImage) {
        this.isFromImage = isFromImage;
    }

    public void setList(List<MediaBean> data) {
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, MediaBean item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_IMAGE:
                handleImage(helper, item);
                break;
            case ITEM_TYPE_VIDEO:
                handleVideo(helper, item);
                break;
            case ITEM_TYPE_ADD:
                handleAdd(helper, item);
                break;
        }
    }

    private void handleImage(BaseViewHolder helper, MediaBean item) {
        ImageView detailIcon = helper.getView(R.id.imageIcon);
        Glide.with(mContext)
                .asBitmap()
                .load(item.getContentUri())
                .apply(new RequestOptions()
                        .override(getImageResize(), getImageResize())
                        .placeholder(null)
                        .centerCrop())
                .into(detailIcon);
        helper.addOnClickListener(R.id.imageDelete);
    }

    private void handleVideo(BaseViewHolder helper, MediaBean item) {
        ImageView detailIcon = helper.getView(R.id.videoIcon);
        Glide.with(mContext)
                .asBitmap()
                .load(item.getContentUri())
                .apply(new RequestOptions()
                        .override(getImageResize(), getImageResize())
                        .placeholder(null)
                        .centerCrop())
                .into(detailIcon);
        helper.addOnClickListener(R.id.videoDelete);
    }

    private void handleAdd(BaseViewHolder helper, MediaBean item) {

    }

    private int getImageResize() {
        if (mImageResize == 0) {
            RecyclerView.LayoutManager lm = getRecyclerView().getLayoutManager();
            int spanCount = ((GridLayoutManager) lm).getSpanCount();
            int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            int availableWidth = screenWidth - mContext.getResources().getDimensionPixelSize(R.dimen.publish_detail_spacing) * (spanCount - 1);
            mImageResize = availableWidth / spanCount;
            mImageResize = (int) (mImageResize * SelectionSpec.getInstance().thumbnailScale);
        }
        return mImageResize;
    }
}
