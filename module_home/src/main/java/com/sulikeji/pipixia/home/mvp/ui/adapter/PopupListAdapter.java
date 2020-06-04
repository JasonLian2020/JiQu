package com.sulikeji.pipixia.home.mvp.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sulikeji.pipixia.home.R;
import com.sulikeji.pipixia.home.mvp.model.entity.PopupListBean;

import java.util.List;

public class PopupListAdapter extends BaseQuickAdapter<PopupListBean, BaseViewHolder> {

    public PopupListAdapter(@Nullable List<PopupListBean> data) {
        super(R.layout.home_item_popup, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PopupListBean item) {
        helper.setImageResource(R.id.icon, item.getIconId());
        helper.setText(R.id.title, item.getTitleId());
    }
}
