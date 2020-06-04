package com.sulikeji.pipixia.search.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.sulikeji.pipixia.search.mvp.contract.SearchContactsContract;
import com.sulikeji.pipixia.search.mvp.model.api.service.SearchService;
import com.sulikeji.pipixia.search.mvp.model.entity.ContactsListBean;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/26/2019 18:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SearchModel extends BaseModel implements SearchContactsContract.Model {
    @Inject
    public SearchModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<PublicBaseResponse<ContactsListBean>> getFollowList(int userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        return mRepositoryManager.obtainRetrofitService(SearchService.class).getFollowList(params);
    }
}