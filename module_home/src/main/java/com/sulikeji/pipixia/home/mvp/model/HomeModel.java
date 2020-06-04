package com.sulikeji.pipixia.home.mvp.model;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.sulikeji.pipixia.home.mvp.contract.HomeContract;
import com.sulikeji.pipixia.home.mvp.contract.HomeDetailContract;
import com.sulikeji.pipixia.home.mvp.model.api.service.HomeService;
import com.sulikeji.pipixia.home.mvp.model.entity.CategoryListBean;
import com.sulikeji.pipixia.home.mvp.model.entity.FeedBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import me.jessyan.armscomponent.commoncore.ConvertUtils;
import me.jessyan.armscomponent.commoncore.message.mvp.model.api.service.MessageService;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 11:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class HomeModel extends BaseModel implements HomeContract.Model, HomeDetailContract.Model {
    @Inject
    Gson mGson;

    @Inject
    public HomeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
    }

    @Override
    public Observable<PublicBaseResponse<List<CategoryListBean>>> getCategoryList() {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getCategoryList();
    }

    @Override
    public Observable<PublicBaseResponse<FeedBean>> getFeedList(int pageSize, int contentType, int lastId) {
        Map<String, Object> params = new HashMap<>();
        params.put("page_size", pageSize);
        params.put("content_type", contentType);
        params.put("last_id", lastId);
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getFeedList(params).map(new ConvertMoreImpl1());
    }

    @Override
    public Observable<PublicBaseResponse<Integer>> postLike(int portalId, int type, int status) {
        //赞与踩是互斥的
        Map<String, Object> params = new HashMap<>();
        params.put("portal_id", portalId);//帖子id
        params.put("type", type);//type[1：赞，2：踩]
        params.put("status", status);//status[0：不取消，1：取消]
        return mRepositoryManager.obtainRetrofitService(MessageService.class).postLike(params);
    }

    @Override
    public Observable<PublicBaseResponse> postFeedback(int portalId, int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", portalId);//帖子ID
        params.put("type", type);//反馈类型[0: 不感兴趣, 1: 内容重复, 2: 内容引起不适，3：屏蔽作者，4：屏蔽话题]
        return mRepositoryManager.obtainRetrofitService(HomeService.class).postFeedback(params);
    }

    @Override
    public Observable<PublicBaseResponse<Integer>> postCollectPortal(int portalId) {
        Map<String, Object> params = new HashMap<>();
        params.put("object_id", portalId);//必填，帖子ID
        params.put("type", 0);//必填，收藏类型，0主贴，1回复
        return mRepositoryManager.obtainRetrofitService(MessageService.class).postCollect(params);
    }

    @Override
    public Observable<PublicBaseResponse> postCancelCollectPortal(int portalId, int favoriteId) {
        Map<String, Object> params = new HashMap<>();
        params.put("object_id", portalId);//必填，帖子ID
        params.put("id", favoriteId);//必填，收藏ID
        return mRepositoryManager.obtainRetrofitService(MessageService.class).postCancelCollect(params);
    }

    public class ConvertMoreImpl1 implements Function<PublicBaseResponse<FeedBean>, PublicBaseResponse<FeedBean>> {
        @Override
        public PublicBaseResponse<FeedBean> apply(PublicBaseResponse<FeedBean> data) throws Exception {
            if (data.isSuccess() && data.getData() != null) {
                //list
                List<FeedListBean> list = data.getData().getFeedList();
                if (list != null) {
                    for (FeedListBean bean : list) {
                        ConvertUtils.convertFieldMore(bean);
                        //神评列表
                        List<FeedListBean> godCommentList = bean.getGodCommentList();
                        if (godCommentList != null) {
                            for (FeedListBean godBean : godCommentList) {
                                ConvertUtils.convertFieldMore(godBean);
                            }
                        }
                    }
                }
            }
            return data;
        }
    }
}