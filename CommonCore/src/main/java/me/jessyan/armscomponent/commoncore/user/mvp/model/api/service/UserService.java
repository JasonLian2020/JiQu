package me.jessyan.armscomponent.commoncore.user.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.QiniuBean;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {
    @GET("/token")
    Observable<PublicBaseResponse<QiniuBean>> getQiniuToken(@Header("DeviceID") String deviceID);
}
