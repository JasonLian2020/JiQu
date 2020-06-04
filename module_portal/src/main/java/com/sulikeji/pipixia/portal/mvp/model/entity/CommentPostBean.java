package com.sulikeji.pipixia.portal.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

public class CommentPostBean {
    @SerializedName("id")
    private int portalId;
    @SerializedName("content")
    private String content;
    /**
     * 回复所属的一级评论id
     */
    @SerializedName("parent_id")
    private int parentId = -1;
    /**
     * 回复的评论id
     */
    @SerializedName("to_parent_id")
    private int toParentId = -1;
    /**
     * 回复的用户id
     */
    @SerializedName("to_user_id")
    private int toUserId = -1;
    @SerializedName("more")
    private String more;
    @SerializedName("content_type")
    private int contentType;

    public int getPortalId() {
        return portalId;
    }

    public void setPortalId(int portalId) {
        this.portalId = portalId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getToParentId() {
        return toParentId;
    }

    public void setToParentId(int toParentId) {
        this.toParentId = toParentId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
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

    //====================标记====================
    private String commentKey;//临时评论key

    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }
    //====================标记====================
}
