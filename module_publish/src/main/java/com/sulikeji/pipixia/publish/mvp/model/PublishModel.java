package com.sulikeji.pipixia.publish.mvp.model;

import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.sulikeji.pipixia.publish.mvp.contract.PublishDetailContract;
import com.sulikeji.pipixia.publish.mvp.model.api.service.PublishService;
import com.sulikeji.pipixia.publish.mvp.model.entity.PublishPostBean;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commoncore.ApplicationCache;
import me.jessyan.armscomponent.commoncore.user.mvp.model.api.service.UserService;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.QiniuBean;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/24/2019 17:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class PublishModel extends BaseModel implements PublishDetailContract.Model {
    @Inject
    public PublishModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<PublicBaseResponse<QiniuBean>> getQiniuToken() {
        UserBean userBean = ApplicationCache.getUserBean();
        String deviceID = userBean == null ? "" : String.valueOf(userBean.getUserId());
        return mRepositoryManager.obtainRetrofitService(UserService.class).getQiniuToken(deviceID);
    }

    @Override
    public Observable<PublicBaseResponse> postPublishMessage(PublishPostBean detailBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", detailBean.getContent());
        params.put("content_type", detailBean.getContentType());
        if (!TextUtils.isEmpty(detailBean.getMore()))
            params.put("more", detailBean.getMore());
        return mRepositoryManager.obtainRetrofitService(PublishService.class).postPublishMessage(params);
    }
}