package com.sulikeji.pipixia.search.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sulikeji.pipixia.search.R;
import com.sulikeji.pipixia.search.mvp.model.entity.ContactsBean;

import java.util.List;

public class SearchContactsAdapter extends BaseQuickAdapter<ContactsBean, BaseViewHolder> {
    public SearchContactsAdapter(@Nullable List<ContactsBean> data) {
        super(R.layout.search_item_contacts, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactsBean item) {
        helper.setText(R.id.contactsName, item.getUserNickname());
        helper.setText(R.id.contactsDesc, item.getUserDesc());
        ImageView contactsAvatar = helper.getView(R.id.contactsAvatar);
        Glide.with(mContext)
                .load(item.getAvatar())
                .apply(new RequestOptions().centerCrop())
                .into(contactsAvatar);
    }
}
