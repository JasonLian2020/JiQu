package com.sulikeji.pipixia.portal.mvp.model.entity;

import java.util.List;

import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;

public class CommentListBean {
    private int page;
    private int size;
    private int total;
    private List<FeedListBean> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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

    public List<FeedListBean> getList() {
        return list;
    }

    public void setList(List<FeedListBean> list) {
        this.list = list;
    }
}
