package com.sulikeji.pipixia.login.mvp.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.sulikeji.pipixia.login.R;
import com.sulikeji.pipixia.login.R2;
import com.sulikeji.pipixia.login.di.component.DaggerLoginHomeComponent;
import com.sulikeji.pipixia.login.mvp.contract.LoginHomeContract;
import com.sulikeji.pipixia.login.mvp.presenter.LoginHomePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.edittext.CodeEditText;
import me.jessyan.armscomponent.commonui.util.ToastUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 16:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.LOGIN_LOGINHOMEACTIVITY)
public class LoginHomeActivity extends BaseActivity<LoginHomePresenter> implements LoginHomeContract.View {
    @Inject
    Dialog mDialog;

    @BindView(R2.id.loginTitle)
    TextView loginTitle;
    @BindView(R2.id.loginSmallPhone)
    TextView loginSmallPhone;
    @BindView(R2.id.loginSwitch)
    ImageView loginSwitch;
    @BindView(R2.id.loginPhoneLayout)
    View loginPhoneLayout;
    @BindView(R2.id.loginPhone)
    EditText loginPhone;
    @BindView(R2.id.loginPhoneDelete)
    ImageView loginPhoneDelete;
    @BindView(R2.id.loginCode)
    CodeEditText loginCode;
    @BindView(R2.id.loginButton)
    Button loginButton;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginHomeComponent //如找不到该类,请编译一下项目
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
        return R.layout.login_activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initStatusBar();
        initView();
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

    private void initView() {
        loginSmallPhone.setVisibility(View.INVISIBLE);
        loginSmallPhone.setOnClickListener(v -> switchInputPhone());
        loginSwitch.setVisibility(View.INVISIBLE);
        loginSwitch.setOnClickListener(v -> switchInputPhone());
        //手机号
        loginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginPhoneDelete.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                loginButton.setEnabled(s.length() >= 11);
            }
        });
        loginPhone.postDelayed(() -> {
            // 获取焦点
            loginPhone.setFocusable(true);
            loginPhone.setFocusableInTouchMode(true);
            loginPhone.requestFocus();
            KeyboardUtils.showSoftInput(loginPhone);
        }, 200);
        loginPhoneDelete.setVisibility(View.GONE);
        loginPhoneDelete.setOnClickListener(v -> {
            //重置手机号
            loginPhone.setText("");
        });
        //验证码
        loginCode.setVisibility(View.GONE);
        loginCode.setOnInputListener(new CodeEditText.OnInputListener() {
            @Override
            public void onTextChanged(String code) {

            }

            @Override
            public void onCompleted(String code) {
                String phone = loginPhone.getText().toString();
                mPresenter.login(phone, code);
            }
        });
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(v -> {
            if (loginButton.getTag() == null) {
                //获取验证码
                mPresenter.getAuthCode(loginPhone.getText().toString());
                return;
            }
            int tag = (int) loginButton.getTag();
            if (tag == R.string.login_btn_get_code) {
                //获取验证码
                mPresenter.getAuthCode(loginPhone.getText().toString());
            } else if (tag == R.string.login_btn_send_code) {
                //重新发送
                mPresenter.restartGetAuthCode(loginPhone.getText().toString());
            } else if (tag == R.string.login_btn_count_down) {
                //倒计时中
                showMessage("验证码发送中，请稍后再试");
            }
        });
    }

    @Override
    public void onBackPressed() {
        KeyboardUtils.hideSoftInput(this);
        super.onBackPressed();
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
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
        KeyboardUtils.hideSoftInput(this);
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void getAuthCodeSuccess(boolean isRestart, String message) {
        if (isRestart) {
            // 重新获取验证码
        } else {
            // 第一次获取验证码
            showCodeStatusUI();
        }
        loginButton.setTag(R.string.login_btn_count_down);
        mPresenter.startCountDown(60);
    }

    @Override
    public void updateUIByBeforeCountDown() {
        loginButton.setEnabled(false);
    }

    @Override
    public void updateUIByCountDown(int progress) {
        loginButton.setText(getString(R.string.login_btn_count_down, progress));
    }

    @Override
    public void updateUIByCompleteCountDown() {
        loginButton.setEnabled(true);
        loginButton.setText(R.string.login_btn_send_code);
        loginButton.setTag(R.string.login_btn_send_code);
    }

    private void switchInputPhone() {
        // 停止倒计时
        mPresenter.cancelCountDown();
        // 显示输入手机号码UI
        showPhoneStatusUI();
        loginButton.setEnabled(true);
        loginButton.setText(R.string.login_btn_get_code);
        loginButton.setTag(R.string.login_btn_get_code);
    }

    private void showCodeStatusUI() {
        loginPhoneLayout.setVisibility(View.GONE);
        loginSmallPhone.setVisibility(View.VISIBLE);
        loginSmallPhone.setText(loginPhone.getText());
        loginSwitch.setVisibility(View.VISIBLE);
        loginCode.setVisibility(View.VISIBLE);
        loginCode.firstFocus();
    }

    private void showPhoneStatusUI() {
        loginPhoneLayout.setVisibility(View.VISIBLE);
        loginSmallPhone.setVisibility(View.INVISIBLE);
        loginSmallPhone.setText("");
        loginSwitch.setVisibility(View.INVISIBLE);
        loginPhone.requestFocus();
        loginCode.setVisibility(View.GONE);
        loginCode.resetUI();
    }
}
