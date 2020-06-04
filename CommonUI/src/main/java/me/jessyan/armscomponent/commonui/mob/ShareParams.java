package me.jessyan.armscomponent.commonui.mob;

import android.graphics.Bitmap;

public class ShareParams {
    public String title;//分享内容的标题
    public String text;//待分享的文本
    public String imagePath;//待分享的本地图片。如果目标平台使用客户端分享，此路径不可以在/data/data下面
    public String imageUrl;//待分享的网络图片
    public String titleUrl;//分享内容标题的链接地址
    public String url;//分享内容的url、在微信和易信中也使用为视频文件地址
    public String filePath;//待分享的文件路径。这个用在Dropbox和Wechat中
    //=============================微信字段
    public int shareType;// 微信和易信的字段，分享内容的类型：
    public String musicUrl;//微信和易信的字段，分享音频时的音频文件网络地址
    public Bitmap imageData;//微信和易信的字段，各类分享内容中的图片bitmap对象，可以替代imagePatd或者imageUrl
    //=============================QQ空间的字段
    public String site;//QQ空间的字段，标记分享应用的名称
    public String siteUrl;//QQ空间的字段，标记分享应用的网页地址

    public static class Builder {
        private String title;//分享内容的标题
        private String text;//待分享的文本
        private String imagePath;//待分享的本地图片。如果目标平台使用客户端分享，此路径不可以在/data/data下面
        private String imageUrl;//待分享的网络图片
        private String titleUrl;//分享内容标题的链接地址
        private String url;//分享内容的url、在微信和易信中也使用为视频文件地址
        private String filePath;//待分享的文件路径。这个用在Dropbox和Wechat中
        //=============================微信的字段
        private int shareType;// 微信和易信的字段，分享内容的类型：
        private String musicUrl;//微信和易信的字段，分享音频时的音频文件网络地址
        public Bitmap imageData;//微信和易信的字段，各类分享内容中的图片bitmap对象，可以替代imagePatd或者imageUrl
        //=============================QQ空间的字段
        private String site;//QQ空间的字段，标记分享应用的名称
        private String siteUrl;//QQ空间的字段，标记分享应用的网页地址

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setTitleUrl(String titleUrl) {
            this.titleUrl = titleUrl;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder setShareType(int shareType) {
            this.shareType = shareType;
            return this;
        }

        public Builder setMusicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
            return this;
        }

        public Builder setImageData(Bitmap imageData) {
            this.imageData = imageData;
            return this;
        }

        public Builder setSite(String site) {
            this.site = site;
            return this;
        }

        public Builder setSiteUrl(String siteUrl) {
            this.siteUrl = siteUrl;
            return this;
        }

        public ShareParams build() {
            ShareParams shareParams = new ShareParams();
            shareParams.title = title;
            shareParams.text = text;
            shareParams.imagePath = imagePath;
            shareParams.imageUrl = imageUrl;
            shareParams.titleUrl = titleUrl;
            shareParams.url = url;
            shareParams.filePath = filePath;
            shareParams.shareType = shareType;
            shareParams.musicUrl = musicUrl;
            shareParams.imageData = imageData;
            shareParams.site = site;
            shareParams.siteUrl = siteUrl;
            return shareParams;
        }
    }
}
