package com.sulikeji.pipixia.home.mvp.contract;

import android.support.v4.app.Fragment;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.sulikeji.pipixia.home.mvp.model.entity.FeedBean;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commonui.base.mvp.model.entity.PublicBaseResponse;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 11:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface HomeDetailContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void finishRefresh(boolean success, boolean emptyResponse, boolean noMoreData);

        void finishLoadMore(boolean success, boolean emptyResponse, boolean noMoreData);

        Fragment getFragment();

        int getCategoryId();

        void showFeedbackPopup(FeedListBean item, int position, android.view.View view);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<PublicBaseResponse<FeedBean>> getFeedList(int pageSize, int contentType, int lastId);

        Observable<PublicBaseResponse<Integer>> postLike(int portalId, int type, int status);

        Observable<PublicBaseResponse> postFeedback(int portalId, int type);

        Observable<PublicBaseResponse<Integer>> postCollectPortal(int portalId);

        Observable<PublicBaseResponse> postCancelCollectPortal(int portalId, int favoriteId);
    }
}
