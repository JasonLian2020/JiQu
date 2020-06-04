package com.sulikeji.pipixia.portal.mvp.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.sulikeji.pipixia.portal.mvp.contract.PortalCommentContract;
import com.sulikeji.pipixia.portal.mvp.contract.PortalDetailContract;
import com.sulikeji.pipixia.portal.mvp.model.api.service.PortalService;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentDetailBean;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentListBean;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentPostBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import me.jessyan.armscomponent.commoncore.ApplicationCache;
import me.jessyan.armscomponent.commoncore.ConvertUtils;
import me.jessyan.armscomponent.commoncore.message.mvp.model.api.service.MessageService;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.user.mvp.model.api.service.UserService;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.QiniuBean;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/17/2019 14:24
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class PortalActivityModel extends BaseModel implements PortalDetailContract.Model, PortalCommentContract.Model {
    @Inject
    Gson mGson;

    @Inject
    public PortalActivityModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
    }

    @Override
    public Observable<PublicBaseResponse<CommentListBean>> getCommentlist(int portalId, int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", portalId);//必填，帖子ID
        params.put("page", page);//页数，从1开始
        params.put("page_size", pageSize);
        return mRepositoryManager.obtainRetrofitService(PortalService.class).getCommentlist(params).map(new ConvertMoreImpl2());
    }

    @Override
    public Observable<PublicBaseResponse<FeedListBean>> getPortalDetail(int portalId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", portalId);//必填，帖子ID
        return mRepositoryManager.obtainRetrofitService(PortalService.class).getPortalDetail(params).map(new ConvertMoreImpl1());
    }

    @Override
    public Observable<PublicBaseResponse<Integer>> postPortalCollect(int portalId) {
        Map<String, Object> params = new HashMap<>();
        params.put("object_id", portalId);//必填，帖子ID
        params.put("type", 0);//必填，收藏类型，0主贴，1回复
        return mRepositoryManager.obtainRetrofitService(MessageService.class).postCollect(params);
    }

    @Override
    public Observable<PublicBaseResponse> postPortalCancelCollect(int portalId, int favoriteId) {
        Map<String, Object> params = new HashMap<>();
        params.put("object_id", portalId);//必填，帖子ID
        params.put("id", favoriteId);//必填，收藏ID
        return mRepositoryManager.obtainRetrofitService(MessageService.class).postCancelCollect(params);
    }

    @Override
    public Observable<PublicBaseResponse<Integer>> postCommentCollect(int portalId, int commentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("object_id", portalId);//必填，帖子ID
        params.put("comment_id", commentId);//当type为1的时候必填 回复的id
        params.put("type", 1);//必填，收藏类型，0主贴，1回复
        return mRepositoryManager.obtainRetrofitService(MessageService.class).postCollect(params);
    }

    @Override
    public Observable<PublicBaseResponse> postCommentCancelCollect(int portalId, int commentId, int favoriteId) {
        Map<String, Object> params = new HashMap<>();
        params.put("object_id", portalId);//必填，帖子ID
        params.put("id", favoriteId);//必填，收藏ID
        params.put("comment_id", commentId);//当type为1的时候必填 回复的id
        params.put("type", 1);//必填，收藏类型，0主贴，1回复
        return mRepositoryManager.obtainRetrofitService(MessageService.class).postCancelCollect(params);
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
    public Observable<PublicBaseResponse> postFollow(int userId) {
        //后台会自动判断，关注/取消关注都一样
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        return mRepositoryManager.obtainRetrofitService(MessageService.class).postFollow(params);
    }

    @Override
    public Observable<PublicBaseResponse<Integer>> postCommentLike(int commentId, int portalId) {
        //后台会自动判断，点赞评论/取消点赞评论都一样
        Map<String, Object> params = new HashMap<>();
        params.put("id", commentId);//必填，评论ID
        params.put("object_id", portalId);//必填，帖子ID
        return mRepositoryManager.obtainRetrofitService(PortalService.class).postCommentLike(params);
    }

    @Override
    public Observable<PublicBaseResponse<CommentDetailBean>> getCommentDetail(int commentId, int portalId, int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", commentId);//必填，评论ID
        params.put("object_id", portalId);//必填，帖子ID
        params.put("page", page);//页数，从1开始
        params.put("page_size", pageSize);
        return mRepositoryManager.obtainRetrofitService(PortalService.class).getCommentDetail(params).map(new ConvertMoreImpl3());
    }

    @Override
    public Observable<PublicBaseResponse<FeedListBean>> postReplyComment(CommentPostBean bean) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", bean.getPortalId());
        params.put("content", bean.getContent());
        params.put("content_type", bean.getContentType());
        if (!TextUtils.isEmpty(bean.getMore()))
            params.put("more", bean.getMore());
        //回复所属的一级评论ID
        if (bean.getParentId() != -1) {
            params.put("parent_id", bean.getParentId());
        }
        //被回复人的评论ID和用户ID
        if (bean.getToParentId() != -1 && bean.getToUserId() != -1) {
            params.put("to_parent_id", bean.getToParentId());
            params.put("to_user_id", bean.getToUserId());
        }
        return mRepositoryManager.obtainRetrofitService(PortalService.class).postReplyComment(params).map(new ConvertMoreImpl1());
    }

    @Override
    public Observable<PublicBaseResponse<QiniuBean>> getQiniuToken() {
        UserBean userBean = ApplicationCache.getUserBean();
        String deviceID = userBean == null ? "" : String.valueOf(userBean.getUserId());
        return mRepositoryManager.obtainRetrofitService(UserService.class).getQiniuToken(deviceID);
    }

    @Override
    public Observable<PublicBaseResponse> postDeleteComment(int commentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", commentId);//评论ID
        return mRepositoryManager.obtainRetrofitService(PortalService.class).postDeleteComment(params);
    }

    public class ConvertMoreImpl1 implements Function<PublicBaseResponse<FeedListBean>, PublicBaseResponse<FeedListBean>> {
        @Override
        public PublicBaseResponse<FeedListBean> apply(PublicBaseResponse<FeedListBean> data) throws Exception {
            if (data.isSuccess()) {
                ConvertUtils.convertFieldMore(data.getData());
                ConvertUtils.convertFieldToParent(data.getData());
            }
            return data;
        }
    }

    public class ConvertMoreImpl2 implements Function<PublicBaseResponse<CommentListBean>, PublicBaseResponse<CommentListBean>> {
        @Override
        public PublicBaseResponse<CommentListBean> apply(PublicBaseResponse<CommentListBean> data) throws Exception {
            if (data.isSuccess() && data.getData() != null) {
                //list
                List<FeedListBean> list = data.getData().getList();
                if (list != null) {
                    for (FeedListBean bean : list) {
                        ConvertUtils.convertFieldMore(bean);
                        ConvertUtils.convertFieldToParent(bean);
                    }
                }
            }
            return data;
        }
    }

    public class ConvertMoreImpl3 implements Function<PublicBaseResponse<CommentDetailBean>, PublicBaseResponse<CommentDetailBean>> {
        @Override
        public PublicBaseResponse<CommentDetailBean> apply(PublicBaseResponse<CommentDetailBean> data) throws Exception {
            if (data.isSuccess() && data.getData() != null) {
                //info
                ConvertUtils.convertFieldMore(data.getData().getInfo());
                ConvertUtils.convertFieldToParent(data.getData().getInfo());
                //list
                List<FeedListBean> list = data.getData().getList();
                if (list != null) {
                    for (FeedListBean bean : list) {
                        ConvertUtils.convertFieldMore(bean);
                        ConvertUtils.convertFieldToParent(bean);
                    }
                }
            }
            return data;
        }
    }
}