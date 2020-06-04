package com.sulikeji.pipixia.search.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.sulikeji.pipixia.search.R;
import com.sulikeji.pipixia.search.R2;
import com.sulikeji.pipixia.search.di.component.DaggerSearchContactsComponent;
import com.sulikeji.pipixia.search.mvp.contract.SearchContactsContract;
import com.sulikeji.pipixia.search.mvp.model.entity.ContactsBean;
import com.sulikeji.pipixia.search.mvp.presenter.SearchContactsPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.util.ToastUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/26/2019 18:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.SEARCH_SEARCHCONTACTSACTIVITY)
public class SearchContactsActivity extends BaseActivity<SearchContactsPresenter> implements SearchContactsContract.View {
    @BindView(R2.id.contactsLayout)
    LinearLayout contactsLayout;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    BaseQuickAdapter mAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSearchContactsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.popup_down_in, R.anim.popup_down_out);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.search_activity_contacts; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initStatusBar();
        initRecyclerView();
        mPresenter.getContactsList();
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

    private void initRecyclerView() {
        //设置
        ArmsUtils.configRecyclerView(recyclerView, mLayoutManager);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ContactsBean item = (ContactsBean) adapter.getItem(position);
            if (item == null) return;
            sendResult(item);
        });
        mAdapter.bindToRecyclerView(recyclerView);
    }

    private void sendResult(ContactsBean item) {
        Intent result = new Intent();
        result.putExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERNAME, item.getUserNickname());
        result.putExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERID, item.getFollowId());
        setResult(RESULT_OK, result);
        finish();
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

    @Override
    public Activity getActivity() {
        return this;
    }
}
