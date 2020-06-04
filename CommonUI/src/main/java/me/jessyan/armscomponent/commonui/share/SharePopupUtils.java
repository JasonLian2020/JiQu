package me.jessyan.armscomponent.commonui.share;

import android.content.Context;

import me.jessyan.armscomponent.commonui.R;

public class SharePopupUtils {

    public static SharePopup getSharePopup(Context context, ShareOptions options) {
        SharePopup.Builder builder = new SharePopup.Builder(context)
                .addShareItem(R.mipmap.public_icon_share_wechat, R.string.public_title_share_wechat, ShareType.SHARE_TYPE_WECHAT)
                .addShareItem(R.mipmap.public_icon_share_qq, R.string.public_title_share_qq, ShareType.SHARE_TYPE_QQ)
                .addShareItem(R.mipmap.public_icon_share_wechat_moments, R.string.public_title_share_wechat_moments, ShareType.SHARE_TYPE_WECHAT_MOMENTS)
                .addShareItem(R.mipmap.public_icon_share_qzone, R.string.public_title_share_qzone, ShareType.SHARE_TYPE_QZONE);
        //是否支持点击自动消失
        builder.autoDismiss(options.autoDismiss);
        //是否支持定制
        if (options.isSupportCustom) {
            builder.addCustomItem(R.mipmap.public_icon_share_complain, R.string.public_title_share_complain, ShareType.SHARE_TYPE_COMPLAIN);
            // 收藏/取消收藏
            if (options.isFavorite)
                builder.addCustomItem(R.mipmap.public_icon_share_unfavorite, R.string.public_title_share_unfavorite, ShareType.SHARE_TYPE_UNFAVORITE);
            else
                builder.addCustomItem(R.mipmap.public_icon_share_favorite, R.string.public_title_share_favorite, ShareType.SHARE_TYPE_FAVORITE);
            // 是否支持保存视频
            if (options.isDownloadVideo)
                builder.addCustomItem(R.mipmap.public_icon_share_download, R.string.public_title_share_download_video, ShareType.SHARE_TYPE_DOWNLOAD_VIDEO);
            // 是否支持保存图片
            if (options.isDownloadImage)
                builder.addCustomItem(R.mipmap.public_icon_share_download, R.string.public_title_share_download_image, ShareType.SHARE_TYPE_DOWNLOAD_IMAGE);
        }
        return builder.build();
    }
}
