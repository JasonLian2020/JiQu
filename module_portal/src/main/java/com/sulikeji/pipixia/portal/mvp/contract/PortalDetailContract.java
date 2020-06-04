package com.sulikeji.pipixia.portal.mvp.contract;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentListBean;
import com.sulikeji.pipixia.portal.mvp.model.entity.CommentPostBean;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.QiniuBean;
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
public interface PortalDetailContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void detailResultSuccess(FeedListBean bean);

        void detailResultError(String message);

        void finishRefresh(boolean success, boolean emptyResponse, boolean noMoreData);

        void finishLoadMore(boolean success, boolean emptyResponse, boolean noMoreData);

        BaseQuickAdapter getPortalCommentAdapter();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<PublicBaseResponse<CommentListBean>> getCommentlist(int portalId, int page, int pageSize);

        Observable<PublicBaseResponse<FeedListBean>> getPortalDetail(int portalId);

        Observable<PublicBaseResponse<Integer>> postPortalCollect(int portalId);

        Observable<PublicBaseResponse> postPortalCancelCollect(int portalId, int favoriteId);

        Observable<PublicBaseResponse<Integer>> postLike(int portalId, int type, int status);

        Observable<PublicBaseResponse> postFollow(int userId);

        Observable<PublicBaseResponse<Integer>> postCommentLike(int commentId, int portalId);

        Observable<PublicBaseResponse<FeedListBean>> postReplyComment(CommentPostBean bean);

        Observable<PublicBaseResponse<QiniuBean>> getQiniuToken();

        Observable<PublicBaseResponse> postDeleteComment(int commentId);
    }
}
