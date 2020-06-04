package me.jessyan.armscomponent.commoncore.message.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ResourceBean implements Parcelable {
    /**
     * path : video/a.mp4
     * width : 100
     * height : 100
     */
    private String path;
    private Integer width;
    private Integer height;
    private String thumbnail;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
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
        dest.writeValue(this.width);
        dest.writeValue(this.height);
        dest.writeString(this.thumbnail);
    }

    public ResourceBean(String path, Integer width, Integer height, String thumbnail) {
        this.path = path;
        this.width = width;
        this.height = height;
        this.thumbnail = thumbnail;
    }

    private ResourceBean(Parcel in) {
        this.path = in.readString();
        this.width = (Integer) in.readValue(Integer.class.getClassLoader());
        this.height = (Integer) in.readValue(Integer.class.getClassLoader());
        this.thumbnail = in.readString();
    }

    public static final Parcelable.Creator<ResourceBean> CREATOR = new Parcelable.Creator<ResourceBean>() {
        @Override
        public ResourceBean createFromParcel(Parcel source) {
            return new ResourceBean(source);
        }

        @Override
        public ResourceBean[] newArray(int size) {
            return new ResourceBean[size];
        }
    };
}
