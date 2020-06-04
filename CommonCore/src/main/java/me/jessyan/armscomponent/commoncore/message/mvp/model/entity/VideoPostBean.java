package me.jessyan.armscomponent.commoncore.message.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoPostBean implements Parcelable {
    /**
     * path : video/a.mp4
     * width : 100
     * height : 100
     */
    private String path;
    private long width;
    private long height;
    private String thumbnail;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeLong(this.width);
        dest.writeLong(this.height);
        dest.writeString(this.thumbnail);
    }

    public VideoPostBean() {
    }

    private VideoPostBean(Parcel in) {
        this.path = in.readString();
        this.width = in.readLong();
        this.height = in.readLong();
        this.thumbnail = in.readString();
    }

    public static final Parcelable.Creator<VideoPostBean> CREATOR = new Parcelable.Creator<VideoPostBean>() {
        @Override
        public VideoPostBean createFromParcel(Parcel source) {
            return new VideoPostBean(source);
        }

        @Override
        public VideoPostBean[] newArray(int size) {
            return new VideoPostBean[size];
        }
    };
}
