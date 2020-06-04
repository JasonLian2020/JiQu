package com.sulikeji.pipixia.login.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean;

public class LoginInfoBean {
    @SerializedName("info")
    private UserBean userBean;
    @SerializedName("token")
    private String userToken;

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}