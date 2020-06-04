package com.sulikeji.pipixia.search.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

public class ContactsBean {

    private String avatar;
    /**
     * 关注用户的id
     */
    @SerializedName("follow_id")
    private int followId;
    /**
     * 是否互相关注
     */
    @SerializedName("is_fans")
    private int isFans;
    @SerializedName("signature")
    private String userDesc;
    @SerializedName("user_id")
    private int userId;
    /**
     * 关注用户的昵称
     */
    @SerializedName("user_nickname")
    private String userNickname;


    public ContactsBean(String userNickname, String userDesc) {
        this.userNickname = userNickname;
        this.userDesc = userDesc;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFollowId() {
        return followId;
    }

    public void setFollowId(int followId) {
        this.followId = followId;
    }

    public int getIsFans() {
        return isFans;
    }

    public void setIsFans(int isFans) {
        this.isFans = isFans;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
