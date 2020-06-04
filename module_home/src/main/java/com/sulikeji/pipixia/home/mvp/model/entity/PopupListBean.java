package com.sulikeji.pipixia.home.mvp.model.entity;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class PopupListBean {
    @DrawableRes
    private int iconId;
    @StringRes
    private int titleId;

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public PopupListBean(int iconId, int titleId) {
        this.iconId = iconId;
        this.titleId = titleId;
    }
}
