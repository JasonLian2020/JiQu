/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.armscomponent.commonservice;

/**
 * ================================================
 * AndroidEventBus 作为本方案提供的另一种跨组件通信方式 (第一种是接口下沉, 在 app 的 MainActivity 中已展示, 通过 ARouter 实现)
 * AndroidEventBus 比 greenrobot 的 EventBus 多了一个 Tag, 在组件化中更容定位和管理事件
 * <p>
 * EventBusHub 用来定义 AndroidEventBus 的 Tag 字符串, 以组件名作为 Tag 前缀, 对每个组件的事件进行分组
 * Tag 的命名规则为 组件名 + 页面名 + 动作
 * 比如需要使用 AndroidEventBus 通知订单组件的订单详情页进行刷新, 可以将这个刷新方法的 Tag 命名为 "order/OrderDetailActivity/refresh"
 * <p>
 * Tips: 基础库中的 EventBusHub 仅用来存放需要跨组件通信的事件的 Tag, 如果某个事件只想在组件内使用 AndroidEventBus 进行通信
 * 那就让组件自行管理这个事件的 Tag
 *
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki#3.5">EventBusHub wiki 官方文档</a>
 * Created by JessYan on 30/03/2018 17:46
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface EventBusHub {
    /**
     * 组名
     */
    String APP = "/App";//宿主 App 组件
    String LOGIN = "/Login";//登录 组件
    String PUBLISH = "/Publish";//发帖 组件
    String PORTAL = "/Portal";//帖子 组件

    String KEY = "/Key";

    /**
     * 登录 分组
     */
    String LOGIN_LOGINSUCCESS = LOGIN + "/LoginSuccess";//登录成功

    /**
     * 发帖 分组
     */
    String PUBLISH_PUBLISHSUCCESS = PUBLISH + "/PublishSuccess";

    /**
     * 帖子 分组
     */
    String PORTAL_FAVORITESUCCESS = PORTAL + "/FavoriteSuccess";//收藏帖子
    String PORTAL_CANCELFAVORITESUCCESS = PORTAL + "/CancelFavoriteSuccess";//取消收藏帖子
    String PORTAL_COMMENTFAVORITESUCCESS = PORTAL + "/CommentFavoriteSuccess";//收藏评论
    String PORTAL_COMMENTCANCELFAVORITESUCCESS = PORTAL + "/CommentCancelFavoriteSuccess";//取消收藏评论
    String PORTAL_LIKESUCCESS = PORTAL + "/LikeSuccess";//点赞
    String PORTAL_CANCELLIKESUCCESS = PORTAL + "/CancelLikeSuccess";//取消点赞
    String PORTAL_FOLLOWSUCCESS = PORTAL + "/FollowSuccess";//关注
    String PORTAL_CANCELFOLLOWSUCCESS = PORTAL + "/CancelFollowSuccess";//取消关注
    String PORTAL_COMMENTLIKESUCCESS = PORTAL + "/CommentLikeSuccess";//点赞
    String PORTAL_COMMENTCANCELLIKESUCCESS = PORTAL + "/CommentCancelLikeSuccess";//取消点赞
    String PORTAL_REPLYPORTALSUCCESS = PORTAL + "/ReplyPortalSuccess";//回复帖子
    String PORTAL_REPLYCOMMENTSUCCESS = PORTAL + "/ReplyCommentSuccess";//回复帖子下的评论
    String PORTAL_UPLOADIMAGESUCCESS = PORTAL + "/UploadImageSuccess";//上传图片到七牛成功
    String PORTAL_UPLOADVIDEOSUCCESS = PORTAL + "/UploadVideoSuccess";//上传视频到七牛成功
    String PORTAL_COMMENTLISTREFRESHSUCCESS = PORTAL + "/CommentListRefreshSuccess";//获取父级评论详情+子级评论列表
    String PORTAL_COMMENTLISTREFRESHERROR = PORTAL + "/CommentListRefreshError";//获取父级评论详情+子级评论列表
    String PORTAL_COMMENTLISTLOADMORESUCCESS = PORTAL + "/CommentListLoadMoreSuccess";//
    String PORTAL_COMMENTLISTLOADMOREERROR = PORTAL + "/CommentListLoadMoreError";//
    String PORTAL_DELETECOMMENTSUCCESS = PORTAL + "/DeleteCommentSuccess";//删除评论

    String PORTAL_KEY_PORTALID = PORTAL + KEY + "/PortalId";//帖子ID
    String PORTAL_KEY_FAVORITEID = PORTAL + KEY + "/FavoriteId";//收藏ID
    String PORTAL_KEY_LIKECOUNT = PORTAL + KEY + "/LikeCount";//点赞数量
    String PORTAL_KEY_USERID = PORTAL + KEY + "/UserId";//用户ID
    String PORTAL_KEY_COMMONID = PORTAL + KEY + "/CommonId";//评论ID
    String PORTAL_KEY_ISPARENTCOMMENT = PORTAL + KEY + "/IsParentComment";//是否父级评论
    String PORTAL_KEY_PARENTID = PORTAL + KEY + "/ParentId";//回复所属的一级评论ID
    String PORTAL_KEY_TOPARENTID = PORTAL + KEY + "/ToParentId";//被回复人的评论ID
    String PORTAL_KEY_TOUSERID = PORTAL + KEY + "/ToUserId";//被回复人的用户ID
    String PORTAL_KEY_COMMENTKEY = PORTAL + KEY + "/CommentKey";//评论Key，起标记作用
    String PORTAL_KEY_IMAGELIST = PORTAL + KEY + "/ImageList";//图片列表
    String PORTAL_KEY_VIDEOBEAN = PORTAL + KEY + "/VideoBean";//视频
    String PORTAL_KEY_FEEDLISTBEAN = PORTAL + KEY + "/FeedListBean";//帖子/评论对象
    String PORTAL_KEY_COMMENTLIST = PORTAL + KEY + "/CommentList";//评论列表
    String PORTAL_KEY_TOTAL = PORTAL + KEY + "/Total";//总数
}
