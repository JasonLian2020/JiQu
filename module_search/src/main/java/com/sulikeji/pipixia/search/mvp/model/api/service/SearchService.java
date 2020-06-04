package com.sulikeji.pipixia.search.mvp.model.api.service;

import com.sulikeji.pipixia.search.mvp.model.entity.ContactsListBean;

import java.util.Map;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface SearchService {
    @GET("/user/follow")
    Observable<PublicBaseResponse<ContactsListBean>> getFollowList(@QueryMap Map<String, Object> params);
}
