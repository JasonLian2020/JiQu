package com.sulikeji.pipixia.portal.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sulikeji.pipixia.portal.R;

import java.util.List;

import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.MediaBean;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class PortalInputDialogAdapter extends BaseQuickAdapter<MediaBean, BaseViewHolder> {
    public PortalInputDialogAdapter(@Nullable List<MediaBean> data) {
        super(R.layout.portal_item_material, data);
    }

    /**
     * 区别图片和视频，true表示图片，false表示视频
     */
    private boolean isFromImage;

    public void setFromImage(boolean isFromImage) {
        this.isFromImage = isFromImage;
    }


    @Override
    protected void convert(BaseViewHolder helper, MediaBean item) {
        ImageView icon = helper.getView(R.id.icon);
        Glide.with(mContext)
                .asBitmap()
                .load(item.getContentUri())
                .apply(new RequestOptions()
                        .override(getImageResize(), getImageResize())
                        .placeholder(null)
                        .centerCrop())
                .into(icon);
        helper.setGone(R.id.videoTag, !isFromImage);
        helper.addOnClickListener(R.id.delete);
    }

    private int mImageResize;

    private int getImageResize() {
        if (mImageResize == 0) {
            mImageResize = AutoSizeUtils.dp2px(mContext, 100);
        }
        return mImageResize;
    }
}
