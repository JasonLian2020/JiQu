package com.sulikeji.pipixia.preview.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.sulikeji.pipixia.preview.R;
import com.sulikeji.pipixia.preview.R2;
import com.sulikeji.pipixia.preview.di.component.DaggerPreviewImageComponent;
import com.sulikeji.pipixia.preview.mvp.contract.PreviewImageContract;
import com.sulikeji.pipixia.preview.mvp.presenter.PreviewImagePresenter;
import com.sulikeji.pipixia.preview.mvp.ui.adapter.PreviewImageAdapter;
import com.sulikeji.pipixia.preview.mvp.ui.fragment.PreviewImageFragment;

import java.util.List;

import butterknife.BindView;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.util.ToastUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/11/2019 10:03
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY)
public class PreviewImageActivity extends BaseActivity<PreviewImagePresenter> implements PreviewImageContract.View, ViewPager.OnPageChangeListener {
    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    @BindView(R2.id.imageCount)
    TextView imageCount;

    private int position;
    private List<ResourceBean> imageList;

    private PreviewImageAdapter pagerAdapter;
    /**
     * 记录上一次的位置
     */
    private int prePosition = -1;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPreviewImageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.public_preview_out_enter, R.anim.public_preview_out_exit);
    }

    @Override
    protected void onDestroy() {
        viewPager.removeOnPageChangeListener(this);
        super.onDestroy();
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.preview_activity_image; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        parseIntent();
        initView();
        prePosition = position;
        updateUIBySelected(position);
    }

    private void parseIntent() {
        if (getIntent() == null) return;
        position = getIntent().getIntExtra(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_POSITION, -1);
        imageList = getIntent().getParcelableArrayListExtra(RouterHub.PREVIEW_PREVIEWIMAGEACTIVITY_KEY_IMAGELIST);
    }

    private void initView() {
        pagerAdapter = new PreviewImageAdapter(getSupportFragmentManager());
        pagerAdapter.addAll(imageList);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ToastUtils.normal(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    private void updateUIBySelected(int position) {
        int total = imageList.size();
        int seleted = position + 1;
        imageCount.setText(getString(R.string.preview_image_count, seleted, total));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (prePosition != -1 && prePosition != position) {
            // 重置上一个页面
            PreviewImageFragment fragment = (PreviewImageFragment) pagerAdapter.instantiateItem(viewPager, prePosition);
            fragment.resetView();
            // 更新UI
            updateUIBySelected(position);
        }
        prePosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
