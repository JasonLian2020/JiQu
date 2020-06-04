package com.sulikeji.pipixia.preview.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.mvp.IView;
import com.sulikeji.pipixia.preview.R;
import com.sulikeji.pipixia.preview.R2;

import butterknife.BindView;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class PreviewImageFragment extends BaseFragment {
    @BindView(R2.id.imageView)
    ImageViewTouch imageView;

    private static final String ARGS_URL = "args_url";
    private static final String ARGS_POSITION = "args_position";

    private String url;
    private int position;

    public static PreviewImageFragment newInstance(String url, int position) {
        Bundle args = new Bundle();
        args.putString(ARGS_URL, url);
        args.putInt(ARGS_POSITION, position);
        PreviewImageFragment fragment = new PreviewImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) return;
        url = getArguments().getString(ARGS_URL, null);
        position = getArguments().getInt(ARGS_POSITION, -1);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preview_fragment_image, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        imageView.setSingleTapListener(() -> clickImage());
        Glide.with(this)
                .load(url)
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private void clickImage() {
        if (mContext instanceof IView) {
            ((IView) mContext).killMyself();
        }
    }

    public void resetView() {
        if (imageView != null) imageView.resetMatrix();
    }
}
