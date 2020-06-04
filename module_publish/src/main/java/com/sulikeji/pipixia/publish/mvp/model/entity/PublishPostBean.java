package com.sulikeji.pipixia.publish.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

public class PublishPostBean {
    private String content;
    @SerializedName("more")
    private String more;
    @SerializedName("content_type")
    private int contentType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}
