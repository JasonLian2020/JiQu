package com.sulikeji.pipixia.home.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;

public class FeedBean {
    @SerializedName("list")
    private List<FeedListBean> feedList;
    private int size;
    /**
     * 这个字段非常牛逼！
     * <P>当前返回的帖子数量 + 剩余的帖子数量</P>
     */
    private int total;

    public List<FeedListBean> getFeedList() {
        return feedList;
    }

    public void setFeedList(List<FeedListBean> feedList) {
        this.feedList = feedList;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
