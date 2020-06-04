package com.sulikeji.pipixia.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.sulikeji.pipixia.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.jason.imagepicker.ImagePicker;
import me.jason.imagepicker.MimeType;
import me.jason.imagepicker.internal.entity.CaptureStrategy;
import me.jason.imagepicker.internal.entity.Item;
import me.jessyan.armscomponent.commonservice.IntentHub;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.engine.Glide4Engine;

@Route(path = RouterHub.APP_MAINACTIVITY)
public class MainActivity extends BaseActivity {

    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.ivMine)
    ImageView ivMine;
    @BindView(R.id.tvMine)
    TextView tvMine;

    private static final int REQUEST_CODE_PUBLISH = 1;

    private static final String TAB_TYPE_HOME = "TAB_TYPE_HOME";
    private static final String TAB_TYPE_MINE = "TAB_TYPE_MINE";

    private BaseFragment currentFragment;
    private BaseFragment homeFragment;
    private BaseFragment mineFragment;

    private String chooseTab = IntentHub.APP_KEY_CHOOSETAB_HOME;// 选中的tab

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initStatusBar();
        onParseIntent();
        chooseFragment(chooseTab);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //更新意图
        setIntent(intent);
        //重新处理意图
        onParseIntent();
        chooseFragment(chooseTab);
    }

    private void initStatusBar() {
        int statusBarColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarColor = getResources().getColor(R.color.public_colorPrimary);
            BarUtils.setStatusBarLightMode(this, true);
        } else {
            statusBarColor = getResources().getColor(R.color.public_colorPrimary_compatibility);
        }
        //状态栏颜色
        BarUtils.setStatusBarColor(this, statusBarColor);
    }

    private void onParseIntent() {
        Intent intent = getIntent();
        // 处理Tab选中逻辑
        if (intent.hasExtra(RouterHub.APP_MAINACTIVITY_KEY_CHOOSETAB)) {
            String newChooseTab = intent.getStringExtra(RouterHub.APP_MAINACTIVITY_KEY_CHOOSETAB);
            if (!TextUtils.isEmpty(newChooseTab)) chooseTab = newChooseTab;
        }
    }

    @OnClick({R.id.llHome, R.id.llPublish, R.id.llMine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llHome:
                showFragmentByHome();
                break;
            case R.id.llPublish:
                showFragmentByPublish();
                break;
            case R.id.llMine:
                showFragmentByMine();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_PUBLISH:
                if (data == null) return;
                int fromType = ImagePicker.obtainFromType(data);
                ArrayList<Item> selectedItems = ImagePicker.obtainItemResult(data);
                ArrayList<String> selectedPaths = ImagePicker.obtainPathResult(data);
                ArrayList<Uri> selectedUris = ImagePicker.obtainUriResult(data);
                ARouter.getInstance()
                        .build(RouterHub.PUBLISH_PUBLISHDETAILACTIVITY)
                        .withInt(RouterHub.PUBLISH_PUBLISHDETAILACTIVITY_KEY_FROMTYPE, fromType)
                        .withParcelableArrayList(RouterHub.PUBLISH_PUBLISHDETAILACTIVITY_KEY_SELECTEDITEM, selectedItems)
                        .withStringArrayList(RouterHub.PUBLISH_PUBLISHDETAILACTIVITY_KEY_SELECTEDPATH, selectedPaths)
                        .withParcelableArrayList(RouterHub.PUBLISH_PUBLISHDETAILACTIVITY_KEY_SELECTEDURI, selectedUris)
                        .withTransition(R.anim.popup_up_in, R.anim.popup_up_out)
                        .navigation(this);
                break;
        }
    }

    private void chooseFragment(String chooseTab) {
        switch (chooseTab) {
            case IntentHub.APP_KEY_CHOOSETAB_HOME:
                showFragmentByHome();
                break;
            case IntentHub.APP_KEY_CHOOSETAB_PUBLISH:
                showFragmentByPublish();
                break;
            case IntentHub.APP_KEY_CHOOSETAB_MINE:
                showFragmentByMine();
                break;
        }
    }

    private void showFragmentByHome() {
        showFragment(homeFragment, TAB_TYPE_HOME);
    }

    @SuppressLint("CheckResult")
    private void showFragmentByPublish() {
        new RxPermissions(this)
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        //授权成功
                        ImagePicker.from(this)
                                .choose(MimeType.ofAll())
                                .capture(true)
                                .captureStrategy(new CaptureStrategy(true, getPackageName() + ".imagepicker.provider", ""))
                                .imageEngine(new Glide4Engine())
                                .forResult(REQUEST_CODE_PUBLISH);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //授权失败
                    } else {
                        //授权失败，不能再次询问
                    }
                });
    }

    private void showFragmentByMine() {
        showFragment(mineFragment, TAB_TYPE_MINE);
    }

    private void showFragment(BaseFragment baseFragment, String tabType) {
        if (currentFragment == baseFragment && currentFragment != null) return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) ft.hide(currentFragment);
        if (baseFragment == null) {
            baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tabType);
        }
        if (baseFragment == null) {
            baseFragment = newInstanceByClass(tabType);
            ft.add(R.id.contentLayout, baseFragment, tabType);
        } else {
            ft.show(baseFragment);
        }
        ft.commitAllowingStateLoss();
        currentFragment = baseFragment;
        // Update UI
        refreshBottomUI(tabType);
    }

    private BaseFragment newInstanceByClass(String tabType) {
        BaseFragment baseFragment = null;
        switch (tabType) {
            case TAB_TYPE_HOME:
                baseFragment = (BaseFragment) ARouter.getInstance().build(RouterHub.HOME_HOMEFRAGMENT).navigation();
                break;
            case TAB_TYPE_MINE:
                baseFragment = (BaseFragment) ARouter.getInstance().build(RouterHub.MINE_MINEHOMEFRAGMENT).navigation();
                break;
        }
        return baseFragment;
    }

    private void refreshBottomUI(String tabType) {
        switch (tabType) {
            case TAB_TYPE_HOME:
                ivHome.setSelected(true);
                tvHome.setSelected(true);
                ivMine.setSelected(false);
                tvMine.setSelected(false);
                break;
            case TAB_TYPE_MINE:
                ivHome.setSelected(false);
                tvHome.setSelected(false);
                ivMine.setSelected(true);
                tvMine.setSelected(true);
                break;
        }
    }
}