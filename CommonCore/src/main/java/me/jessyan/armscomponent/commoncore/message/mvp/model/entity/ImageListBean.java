package me.jessyan.armscomponent.commoncore.message.mvp.model.entity;

import java.util.List;

public class ImageListBean {
    private List<ResourceBean> imageList;

    public ImageListBean(List<ResourceBean> imageList) {
        this.imageList = imageList;
    }

    public List<ResourceBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<ResourceBean> imageList) {
        this.imageList = imageList;
    }
}
