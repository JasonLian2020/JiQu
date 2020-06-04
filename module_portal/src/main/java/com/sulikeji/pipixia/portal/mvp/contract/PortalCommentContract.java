package com.sulikeji.pipixia.portal.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentDetailBean;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentPostBean;

import java.util.List;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.QiniuBean;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/14/2019 16:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface PortalCommentContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        /**
         * 获取内容方式
         */
        int getContentType();

        /**
         * 获取帖子ID
         */
        int getPortalId();

        /**
         * 获取评论ID
         */
        int getCommentId();

        /**
         * 获取作者用户ID
         */
        int getMasterUserId();

        /**
         * 对象
         */
        FeedListBean getItem();

        /**
         * 获取回复列表
         */
        List<FeedListBean> getCommentList();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<PublicBaseResponse<Integer>> postCommentCollect(int portalId, int commentId);

        Observable<PublicBaseResponse> postCommentCancelCollect(int portalId, int commentId, int favoriteId);

        Observable<PublicBaseResponse> postFollow(int userId);

        Observable<PublicBaseResponse<Integer>> postCommentLike(int commentId, int portalId);

        Observable<PublicBaseResponse<CommentDetailBean>> getCommentDetail(int commentId, int portalId, int page, int pageSize);

        Observable<PublicBaseResponse<FeedListBean>> postReplyComment(CommentPostBean bean);

        Observable<PublicBaseResponse<QiniuBean>> getQiniuToken();

        Observable<PublicBaseResponse> postDeleteComment(int commentId);
    }
}
