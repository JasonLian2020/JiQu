package me.jessyan.armscomponent.commoncore.user.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

public class UserBean {
    //用户ID
    @SerializedName("id")
    private int userId;
    //登录账号
    @SerializedName("user_login")
    private String userLogin;
    //昵称
    @SerializedName("user_nickname")
    private String userNickname;
    //头像
    @SerializedName("avatar")
    private String userAvatar;
    //登录token有效期
    private long exp;
    private String iss;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }
}
