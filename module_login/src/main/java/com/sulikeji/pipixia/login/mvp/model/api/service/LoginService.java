package com.sulikeji.pipixia.login.mvp.model.api.service;

import com.sulikeji.pipixia.login.mvp.model.entity.LoginInfoBean;

import java.util.Map;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    // 获取验证码（注册，登录使用）
    @FormUrlEncoded
    @POST("/getcode")
    Observable<PublicBaseResponse> getAuthCode(@FieldMap Map<String, Object> params);

    // 登录（注册）
    @FormUrlEncoded
    @POST("/login")
    Observable<PublicBaseResponse<LoginInfoBean>> login(@FieldMap Map<String, Object> params);
}