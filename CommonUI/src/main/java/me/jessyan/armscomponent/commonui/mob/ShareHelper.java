package me.jessyan.armscomponent.commonui.mob;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareHelper {
    public static void shareByWechat(@NonNull ShareParams params, PlatformActionListener platformActionListener) {
        // 配置参数
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(params.shareType);
        sp.setTitle(params.title);
        if (!TextUtils.isEmpty(params.text))
            sp.setText(params.text);
        if (!TextUtils.isEmpty(params.imagePath))
            sp.setImagePath(params.imagePath);
        if (!TextUtils.isEmpty(params.imageUrl))
            sp.setImageUrl(params.imageUrl);
        if (!TextUtils.isEmpty(params.url))
            sp.setUrl(params.url);
        if (!TextUtils.isEmpty(params.musicUrl))
            sp.setMusicUrl(params.musicUrl);// 分享音乐才需要此字段
        if (params.imageData != null)
            sp.setImageData(params.imageData);
        // 调起分享
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(platformActionListener);
        // 执行图文分享
        platform.share(sp);
    }

    public static void shareByWechatMoments(@NonNull ShareParams params, PlatformActionListener platformActionListener) {
        // 配置参数
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(params.shareType);
        sp.setTitle(params.title);
        if (!TextUtils.isEmpty(params.text))
            sp.setText(params.text);
        if (!TextUtils.isEmpty(params.imagePath))
            sp.setImagePath(params.imagePath);
        if (!TextUtils.isEmpty(params.imageUrl))
            sp.setImageUrl(params.imageUrl);
        if (!TextUtils.isEmpty(params.url))
            sp.setUrl(params.url);
        if (!TextUtils.isEmpty(params.musicUrl))
            sp.setMusicUrl(params.musicUrl);// 分享音乐才需要此字段
        if (params.imageData != null)
            sp.setImageData(params.imageData);
        // 调起分享
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(platformActionListener);
        // 执行图文分享
        platform.share(sp);
    }

    public static void shareByQQ(@NonNull ShareParams params, PlatformActionListener platformActionListener) {
        // 配置参数
        Platform.ShareParams sp = new Platform.ShareParams();
        if (!TextUtils.isEmpty(params.title))
            sp.setTitle(params.title);
        if (!TextUtils.isEmpty(params.titleUrl))
            sp.setTitleUrl(params.titleUrl);// 分享内容标题的链接地址
        if (!TextUtils.isEmpty(params.text))
            sp.setText(params.text);
        if (!TextUtils.isEmpty(params.imagePath))
            sp.setImagePath(params.imagePath);
        if (!TextUtils.isEmpty(params.imageUrl))
            sp.setImageUrl(params.imageUrl);
        if (!TextUtils.isEmpty(params.musicUrl))
            sp.setMusicUrl(params.musicUrl);// 分享音乐才需要此字段
        // 调起分享
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(platformActionListener);
        // 执行图文分享
        platform.share(sp);
    }

    public static void shareByQzone(@NonNull ShareParams params, PlatformActionListener platformActionListener) {
        // 配置参数
        Platform.ShareParams sp = new Platform.ShareParams();
        if (!TextUtils.isEmpty(params.title))
            sp.setTitle(params.title);
        if (!TextUtils.isEmpty(params.titleUrl))
            sp.setTitleUrl(params.titleUrl);// 分享内容标题的链接地址
        if (!TextUtils.isEmpty(params.text))
            sp.setText(params.text);
        if (!TextUtils.isEmpty(params.imagePath))
            sp.setImagePath(params.imagePath);
        if (!TextUtils.isEmpty(params.imageUrl))
            sp.setImageUrl(params.imageUrl);
        if (!TextUtils.isEmpty(params.site))
            sp.setSite(params.site);
        if (!TextUtils.isEmpty(params.siteUrl))
            sp.setSiteUrl(params.siteUrl);
        if (!TextUtils.isEmpty(params.filePath))
            sp.setFilePath(params.filePath);// 分享视频才需要此字段
        if (params.shareType != 0)
            sp.setShareType(params.shareType);// 分享视频才需要此字段
        // 调起分享
        Platform platform = ShareSDK.getPlatform(QZone.NAME);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(platformActionListener);
        // 执行图文分享
        platform.share(sp);
    }
}
