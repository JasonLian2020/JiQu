package me.jessyan.armscomponent.commonui.share;

public class ShareOptions {
    public boolean isSupportCustom;
    public boolean isDownloadVideo;
    public boolean isDownloadImage;
    public boolean autoDismiss;
    public boolean isFavorite;

    public static class Builder {
        private boolean isSupportCustom = true;
        private boolean isDownloadVideo = false;
        private boolean isDownloadImage = false;
        private boolean autoDismiss = true;
        private boolean isFavorite = false;

        public Builder supportCustom(boolean isSupportCustom) {
            this.isSupportCustom = isSupportCustom;
            return this;
        }

        public Builder downloadVideo(boolean isDownloadVideo) {
            this.isDownloadVideo = isDownloadVideo;
            return this;
        }

        public Builder downloadImage(boolean isDownloadImage) {
            this.isDownloadImage = isDownloadImage;
            return this;
        }

        public Builder favorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }

        public ShareOptions build() {
            ShareOptions shareOptions = new ShareOptions();
            shareOptions.isSupportCustom = isSupportCustom;
            shareOptions.isDownloadImage = isDownloadImage;
            shareOptions.isDownloadVideo = isDownloadVideo;
            shareOptions.autoDismiss = autoDismiss;
            shareOptions.isFavorite = isFavorite;
            return shareOptions;
        }
    }
}
