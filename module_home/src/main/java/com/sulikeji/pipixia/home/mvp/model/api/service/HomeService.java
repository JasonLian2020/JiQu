package com.sulikeji.pipixia.home.mvp.model.api.service;


import com.sulikeji.pipixia.home.mvp.model.entity.CategoryListBean;
import com.sulikeji.pipixia.home.mvp.model.entity.FeedBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface HomeService {
    @GET("/home/category")
    Observable<PublicBaseResponse<List<CategoryListBean>>> getCategoryList();

    @GET("/list")
    Observable<PublicBaseResponse<FeedBean>> getFeedList(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("/feedback")
    Observable<PublicBaseResponse> postFeedback(@FieldMap Map<String, Object> params);
}