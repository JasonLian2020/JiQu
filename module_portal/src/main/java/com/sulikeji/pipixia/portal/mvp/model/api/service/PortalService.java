package com.sulikeji.pipixia.portal.mvp.model.api.service;

import com.sulikeji.pipixia.portal.mvp.model.entity.CommentDetailBean;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentListBean;

import java.util.Map;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface PortalService {
    @GET("/commentlist")
    Observable<PublicBaseResponse<CommentListBean>> getCommentlist(@QueryMap Map<String, Object> params);

    @GET("/portal/detail")
    Observable<PublicBaseResponse<FeedListBean>> getPortalDetail(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("/favorite")
    Observable<PublicBaseResponse<Integer>> postCommentLike(@FieldMap Map<String, Object> params);

    @GET("/commentDetail")
    Observable<PublicBaseResponse<CommentDetailBean>> getCommentDetail(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("/replycomment")
    Observable<PublicBaseResponse<FeedListBean>> postReplyComment(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("/deletecomment")
    Observable<PublicBaseResponse> postDeleteComment(@FieldMap Map<String, Object> params);
}
