package me.jessyan.armscomponent.commoncore.message.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedListBean implements Parcelable {
    public static final int CONTENT_TYPE_TEXT = 0;//纯文字
    public static final int CONTENT_TYPE_IMAGE = 1;//文字+图片
    public static final int CONTENT_TYPE_VIDEO = 2;//文字+视频

    public static final int STATUS_FAIL = -1;
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_SUCCESS = 1;

    /**
     * 上传中/未上传成功的评论
     */
    public static final int EMPTY_COMMENT_ID = -1;

    /**
     * 评论数
     */
    @SerializedName("comment_count")
    private int commentCount;
    /**
     * 评论状态
     */
    @SerializedName("comment_status")
    private int commentStatus;
    /**
     * 内容类型[0: 纯文字; 1: 文字+图片; 2: 文字+视频]
     */
    @SerializedName("content_type")
    private int contentType;
    /**
     * 发帖时间
     */
    @SerializedName("create_time")
    private String createTime;
    /**
     * 删帖时间
     */
    @SerializedName("delete_time")
    private long deleteTime;
    /**
     * 发布时间
     */
    @SerializedName("published_time")
    private long publishedTime;
    /**
     * 帖子模型，该值是帖子ID；评论模型，该值是评论ID
     */
    @SerializedName("id")
    private int id;
    @SerializedName("favorite_id")
    private int favoriteId;
    /**
     * 是否收藏[0：未收藏，1：已收藏]
     */
    @SerializedName("is_favorite")
    private int isFavorite;
    @SerializedName("is_import")
    private int isImport;
    /**
     * 是否关注[0：未关注，1：已关注]
     */
    @SerializedName("is_follow")
    private int isFollow;
    /**
     * 是否点赞[-1：踩，0：不赞不踩，1：赞]
     */
    @SerializedName("is_like")
    private int isLike;
    /**
     * 是否神评[0:不是，1：是]
     */
    @SerializedName("is_god")
    private int isGod;
    /**
     * 图片、视频地址[图片：json格式，string list；视频：json格式 对象]
     */
    @SerializedName("more")
    private Object more;
    /**
     * 文本内容[json格式：对象 list]
     */
    @SerializedName("content")
    private List<ContentBean> contentList;
    @SerializedName("like_count")
    private int likeCount;
    @SerializedName("dislike_count")
    private int dislikeCount;
    @SerializedName("share_count")
    private int shareCount;
    /**
     * 作为评论模型的时候，该值就是帖子ID
     */
    @SerializedName("object_id")
    private int objectId;
    /**
     * 回复所属的一级评论id（父级评论ID）
     */
    @SerializedName("parent_id")
    private int parentId = -1;
    /**
     * 回复的评论id（被回复人的评论ID）
     */
    @SerializedName("to_parent_id")
    private int toParentId = -1;
    /**
     * 回复的用户id（被回复人的用户ID）
     */
    @SerializedName("to_user_id")
    private int toUserId = -1;
    /**
     * 被回复的人
     */
    @SerializedName("to_parent")
    private FeedListBean toParentInfo;
    /**
     * 神评列表
     */
    @SerializedName("god_comment")
    private List<FeedListBean> godCommentList;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public long getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(long publishedTime) {
        this.publishedTime = publishedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getIsImport() {
        return isImport;
    }

    public void setIsImport(int isImport) {
        this.isImport = isImport;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getIsGod() {
        return isGod;
    }

    public void setIsGod(int isGod) {
        this.isGod = isGod;
    }

    public Object getMore() {
        return more;
    }

    public void setMore(Object more) {
        this.more = more;
    }

    public List<ContentBean> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentBean> contentList) {
        this.contentList = contentList;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getToParentId() {
        return toParentId;
    }

    public void setToParentId(int toParentId) {
        this.toParentId = toParentId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public FeedListBean getToParentInfo() {
        return toParentInfo;
    }

    public void setToParentInfo(FeedListBean toParentInfo) {
        this.toParentInfo = toParentInfo;
    }

    public List<FeedListBean> getGodCommentList() {
        return godCommentList;
    }

    public void setGodCommentList(List<FeedListBean> godCommentList) {
        this.godCommentList = godCommentList;
    }

    //====================首页获取帖子接口====================
    @SerializedName("user_info")
    private UserBean userBean;

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    //====================首页获取帖子接口====================

    //====================手动解析生成的数据====================
    private ResourceBean videoBean;//视频
    private List<ResourceBean> imageList;//图片集合

    public ResourceBean getVideoBean() {
        return videoBean;
    }

    public void setVideoBean(ResourceBean videoBean) {
        this.videoBean = videoBean;
    }

    public List<ResourceBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<ResourceBean> imageList) {
        this.imageList = imageList;
    }
    //====================手动解析生成的数据====================

    //====================标记====================
    private String commentKey;//临时评论key

    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }
    //====================标记====================

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.commentCount);
        dest.writeInt(this.commentStatus);
        dest.writeInt(this.contentType);
        dest.writeString(this.createTime);
        dest.writeLong(this.deleteTime);
        dest.writeLong(this.publishedTime);
        dest.writeInt(this.id);
        dest.writeInt(this.favoriteId);
        dest.writeInt(this.isFavorite);
        dest.writeInt(this.isImport);
        dest.writeInt(this.isFollow);
        dest.writeInt(this.isLike);
        dest.writeInt(this.isGod);
        dest.writeTypedList(this.contentList);
        dest.writeInt(this.likeCount);
        dest.writeInt(this.dislikeCount);
        dest.writeInt(this.shareCount);
        dest.writeInt(this.objectId);
        dest.writeInt(this.parentId);
        dest.writeInt(this.toParentId);
        dest.writeInt(this.toUserId);
        dest.writeParcelable(this.toParentInfo, flags);
        dest.writeTypedList(this.godCommentList);
        dest.writeParcelable(this.userBean, flags);
        dest.writeParcelable(this.videoBean, flags);
        dest.writeTypedList(this.imageList);
        dest.writeString(this.commentKey);
    }

    public FeedListBean() {
    }

    private FeedListBean(Parcel in) {
        this.commentCount = in.readInt();
        this.commentStatus = in.readInt();
        this.contentType = in.readInt();
        this.createTime = in.readString();
        this.deleteTime = in.readLong();
        this.publishedTime = in.readLong();
        this.id = in.readInt();
        this.favoriteId = in.readInt();
        this.isFavorite = in.readInt();
        this.isImport = in.readInt();
        this.isFollow = in.readInt();
        this.isLike = in.readInt();
        this.isGod = in.readInt();
        this.contentList = in.createTypedArrayList(ContentBean.CREATOR);
        this.likeCount = in.readInt();
        this.dislikeCount = in.readInt();
        this.shareCount = in.readInt();
        this.objectId = in.readInt();
        this.parentId = in.readInt();
        this.toParentId = in.readInt();
        this.toUserId = in.readInt();
        this.toParentInfo = in.readParcelable(FeedListBean.class.getClassLoader());
        this.godCommentList = in.createTypedArrayList(FeedListBean.CREATOR);
        this.userBean = in.readParcelable(UserBean.class.getClassLoader());
        this.videoBean = in.readParcelable(ResourceBean.class.getClassLoader());
        this.imageList = in.createTypedArrayList(ResourceBean.CREATOR);
        this.commentKey = in.readString();
    }

    public static final Parcelable.Creator<FeedListBean> CREATOR = new Parcelable.Creator<FeedListBean>() {
        @Override
        public FeedListBean createFromParcel(Parcel source) {
            return new FeedListBean(source);
        }

        @Override
        public FeedListBean[] newArray(int size) {
            return new FeedListBean[size];
        }
    };
}
