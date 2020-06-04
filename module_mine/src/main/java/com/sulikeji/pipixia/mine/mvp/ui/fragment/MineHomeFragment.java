package com.sulikeji.pipixia.mine.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.sulikeji.pipixia.mine.R;
import com.sulikeji.pipixia.mine.R2;
import com.sulikeji.pipixia.mine.di.component.DaggerMineHomeComponent;
import com.sulikeji.pipixia.mine.mvp.contract.MineHomeContract;
import com.sulikeji.pipixia.mine.mvp.presenter.MineHomePresenter;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commoncore.ApplicationCache;
import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commonservice.EventBusHub;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.util.ToastUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/15/2019 17:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.MINE_MINEHOMEFRAGMENT)
public class MineHomeFragment extends BaseFragment<MineHomePresenter> implements MineHomeContract.View {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    BaseQuickAdapter mAdapter;

    private View mineAvaterLayout;
    private ImageView mineUserAvatar;
    private TextView mineUserName;
    private TextView mineUserDesc;

    /**
     * 标记是否跳转到登录页面
     * <P>true表示未登录，false表示已登录</P>
     */
    private boolean isLogin;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMineHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mine_fragment_home, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //view
        initRecyclerView();
        //ui
        UserBean userBean = ApplicationCache.getUserBean();
        if (userBean == null) showLoginUI();
        else showUserUI(userBean);
    }

    @Override
    public void setData(@Nullable Object data) {

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

    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(recyclerView, mLayoutManager);
        mAdapter.addHeaderView(initHeaderView());
        recyclerView.setAdapter(mAdapter);
    }

    private View initHeaderView() {
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.mine_header_home, recyclerView, false);
        initAvaterLayout(headerView);
        return headerView;
    }

    private void initAvaterLayout(View headerView) {
        mineAvaterLayout = headerView.findViewById(R.id.mineAvaterLayout);
        mineAvaterLayout.setOnClickListener(v -> {
            if (isLogin)
                ARouter.getInstance()
                        .build(RouterHub.LOGIN_LOGINHOMEACTIVITY)
                        .withTransition(R.anim.popup_up_in, R.anim.popup_up_out)
                        .navigation(mContext);
            else
                showMessage("个人信息页面开发中...");
        });
        mineUserAvatar = headerView.findViewById(R.id.mineUserAvatar);
        mineUserName = headerView.findViewById(R.id.mineUserName);
        mineUserDesc = headerView.findViewById(R.id.mineUserDesc);
    }

    private void showUserUI(UserBean userBean) {
        isLogin = false;
        mineUserName.setTypeface(Typeface.DEFAULT_BOLD);
        mineUserName.setText(userBean.getUserNickname());
        mineUserDesc.setText("没有该字段");
    }

    private void showLoginUI() {
        isLogin = true;
        mineUserName.setTypeface(Typeface.DEFAULT);
        mineUserName.setText(R.string.mine_user_def_name);
        mineUserDesc.setText(R.string.mine_user_def_desc);
    }

    @Subscriber(tag = EventBusHub.LOGIN_LOGINSUCCESS, mode = ThreadMode.MAIN)
    private void loginSuccess(UserBean userBean) {
        showUserUI(userBean);
    }
}
