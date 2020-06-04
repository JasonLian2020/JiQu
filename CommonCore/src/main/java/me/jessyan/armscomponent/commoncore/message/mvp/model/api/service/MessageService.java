package me.jessyan.armscomponent.commoncore.message.mvp.model.api.service;

import java.util.Map;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MessageService {
    @FormUrlEncoded
    @POST("/like")
    Observable<PublicBaseResponse<Integer>> postLike(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("/collect")
    Observable<PublicBaseResponse<Integer>> postCollect(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("/cancelcollect")
    Observable<PublicBaseResponse> postCancelCollect(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("/follow")
    Observable<PublicBaseResponse> postFollow(@FieldMap Map<String, Object> params);
}
