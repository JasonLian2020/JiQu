package com.sulikeji.pipixia.preview.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sulikeji.pipixia.preview.mvp.ui.fragment.PreviewImageFragment;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;

public class PreviewImageAdapter extends FragmentPagerAdapter {
    private List<ResourceBean> mItems = new ArrayList<>();

    public PreviewImageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mItems == null ? null : PreviewImageFragment.newInstance(mItems.get(position).getPath(), position);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public void addAll(List<ResourceBean> items) {
        mItems.addAll(items);
    }

}
