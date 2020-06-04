package com.sulikeji.pipixia.publish.mvp.model.api.service;

import java.util.Map;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PublishService {
    @FormUrlEncoded
    @POST("/portal")
    Observable<PublicBaseResponse> postPublishMessage(@FieldMap Map<String, Object> params);
}
