package com.sulikeji.pipixia.login.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.sulikeji.pipixia.login.mvp.contract.LoginHomeContract;
import com.sulikeji.pipixia.login.mvp.model.api.service.LoginService;
import com.sulikeji.pipixia.login.mvp.model.entity.LoginInfoBean;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 16:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class LoginHomeModel extends BaseModel implements LoginHomeContract.Model {
    @Inject
    public LoginHomeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<PublicBaseResponse> getAuthCode(String mobile) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        return mRepositoryManager.obtainRetrofitService(LoginService.class).getAuthCode(params);
    }

    @Override
    public Observable<PublicBaseResponse<LoginInfoBean>> login(String userLogin, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", userLogin);
        params.put("code", code);
        return mRepositoryManager.obtainRetrofitService(LoginService.class).login(params);
    }
}