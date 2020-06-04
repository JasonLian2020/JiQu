package me.jessyan.armscomponent.commoncore.message.mvp.model.entity;

import android.net.Uri;

import me.jason.imagepicker.internal.entity.Item;

public class MediaBean {
    /**
     * 说明这个bean只是用来站位
     */
    public static final String NONE_PATH = "NONE_PATH";

    private long id;
    private String path;
    private Uri contentUri;
    private String mimeType;
    private long size;
    private long duration; // only for video, in ms
    private long width;
    private long height;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Uri getContentUri() {
        return contentUri;
    }

    public void setContentUri(Uri contentUri) {
        this.contentUri = contentUri;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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

    private MediaBean(long id, String path, Uri contentUri, String mimeType, long size, long duration, long width, long height) {
        this.id = id;
        this.path = path;
        this.contentUri = contentUri;
        this.mimeType = mimeType;
        this.size = size;
        this.duration = duration;
        this.width = width;
        this.height = height;
    }

    public static MediaBean valueOf(long id, String path, Uri contentUri, String mimeType, long size, long duration, long width, long height) {
        return new MediaBean(id, path, contentUri, mimeType, size, duration, width, height);
    }

    public static MediaBean valueOf(Item item, String path, Uri contentUri) {
        return new MediaBean(item.id, path, contentUri, item.mimeType, item.size, item.duration, item.width, item.height);
    }

    public static MediaBean emptyBean() {
        return new MediaBean(0, NONE_PATH, null, null, 0, 0, 0, 0);
    }
}
