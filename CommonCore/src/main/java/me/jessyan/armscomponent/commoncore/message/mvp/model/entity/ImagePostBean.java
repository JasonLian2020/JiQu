package me.jessyan.armscomponent.commoncore.message.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagePostBean implements Parcelable {
    private String path;
    private long width;
    private long height;

    public ImagePostBean(String path, long width, long height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeLong(this.width);
        dest.writeLong(this.height);
    }

    private ImagePostBean(Parcel in) {
        this.path = in.readString();
        this.width = in.readLong();
        this.height = in.readLong();
    }

    public static final Parcelable.Creator<ImagePostBean> CREATOR = new Parcelable.Creator<ImagePostBean>() {
        @Override
        public ImagePostBean createFromParcel(Parcel source) {
            return new ImagePostBean(source);
        }

        @Override
        public ImagePostBean[] newArray(int size) {
            return new ImagePostBean[size];
        }
    };
}
