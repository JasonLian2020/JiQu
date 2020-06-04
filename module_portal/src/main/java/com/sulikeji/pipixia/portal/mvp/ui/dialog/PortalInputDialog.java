package com.sulikeji.pipixia.portal.mvp.ui.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.integration.EventBusManager;
import com.sulikeji.pipixia.portal.R;
import com.sulikeji.pipixia.portal.mvp.ui.adapter.PortalInputDialogAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import me.jason.imagepicker.ImagePicker;
import me.jason.imagepicker.MimeType;
import me.jason.imagepicker.internal.entity.CaptureStrategy;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.easyat.KeyCodeDeleteHelper;
import me.jessyan.armscomponent.commonui.easyat.NoCopySpanEditableFactory;
import me.jessyan.armscomponent.commonui.easyat.watcher.SelectionSpanWatcher;
import me.jessyan.armscomponent.commonui.engine.Glide4Engine;
import me.jessyan.autosize.utils.AutoSizeUtils;
import timber.log.Timber;


public class PortalInputDialog extends BottomSheetDialog implements TextWatcher {
    protected final Builder builder;

    private static final String DEFAULT_INPUT_HINT = Utils.getApp().getString(R.string.portal_dialog_input_hint_default);

    private RecyclerView recyclerView;
    private EditText inputComment;
    private View chooseVideo;
    private View chooseImage;
    private View chooseAt;
    private View chooseSend;
    private PortalInputDialogAdapter adapter;
    private BaseQuickAdapter.OnItemChildClickListener onItemDeleteListener;
    private View.OnClickListener onSendListener;

    private PortalInputDialog(@NonNull Builder builder) {
        super(builder.activity, R.style.public_BottomSheetDialog_Input);
        this.builder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portal_dialog_input);
        View contentLayout = findViewById(R.id.contentLayout);
        //左右滑动列表
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(builder.activity, LinearLayout.HORIZONTAL, false));
        adapter = new PortalInputDialogAdapter(null);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.delete) {
                if (onItemDeleteListener != null)
                    onItemDeleteListener.onItemChildClick(adapter, view, position);
            }
        });
        adapter.bindToRecyclerView(recyclerView);
        //自动获取焦点，弹出键盘事件
        inputComment = findViewById(R.id.inputComment);
        inputComment.setHint(TextUtils.isEmpty(builder.inputHint) ? DEFAULT_INPUT_HINT : builder.inputHint);
        inputComment.setEditableFactory(new NoCopySpanEditableFactory(new SelectionSpanWatcher()));
        inputComment.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                return KeyCodeDeleteHelper.onDelDown(((EditText) v).getText());
            }
            return false;
        });
        inputComment.setOnFocusChangeListener((v, hasFocus) -> {
            Timber.tag("jason").d("onFocusChange " + hasFocus);
        });
        inputComment.addTextChangedListener(this);
        inputComment.setMovementMethod(LinkMovementMethod.getInstance());
        inputComment.setFocusable(true);
        inputComment.setFocusableInTouchMode(true);
        inputComment.requestFocus();
        //操作按钮
        chooseVideo = findViewById(R.id.chooseVideo);
        chooseVideo.setOnClickListener(v -> clickChooseVideo());
        chooseVideo.setVisibility(builder.isShowVideo ? View.VISIBLE : View.GONE);
        chooseImage = findViewById(R.id.chooseImage);
        chooseImage.setOnClickListener(v -> clickChooseImage());
        chooseImage.setVisibility(builder.isShowImage ? View.VISIBLE : View.GONE);
        chooseAt = findViewById(R.id.chooseAt);
        chooseAt.setOnClickListener(v -> clickChooseAt());
        chooseSend = findViewById(R.id.chooseSend);
        chooseSend.setOnClickListener(v -> clickChooseSend());
        chooseSend.setEnabled(false);
        //fix bug：固定高度
        View parent = (View) contentLayout.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        contentLayout.measure(0, 0);
        int height = contentLayout.getMeasuredHeight() + AutoSizeUtils.dp2px(builder.activity, 100) + AutoSizeUtils.dp2px(builder.activity, 20);
        Timber.tag("jason").d("height = " + height);
        behavior.setPeekHeight(height);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);
    }

    //=============================================================public method
    public void onCreate() {
        EventBusManager.getInstance().register(this);
    }

    public void onDestory() {
        EventBusManager.getInstance().unregister(this);
        inputComment.removeTextChangedListener(this);
        // Don't keep a Context reference in the Builder after this point
        builder.activity = null;
        builder.fragment = null;
    }

    public void showDialog() {
        if (!isShowing()) show();
    }

    public void dismissDialog() {
        if (isShowing()) dismiss();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public EditText getInputComment() {
        return inputComment;
    }

    public View getChooseVideo() {
        return chooseVideo;
    }

    public View getChooseImage() {
        return chooseImage;
    }

    public View getChooseAt() {
        return chooseAt;
    }

    public View getChooseSend() {
        return chooseSend;
    }

    public PortalInputDialogAdapter getAdapter() {
        return adapter;
    }

    public void setOnItemDeleteListener(BaseQuickAdapter.OnItemChildClickListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    public void setOnSendListener(View.OnClickListener onSendListener) {
        this.onSendListener = onSendListener;
    }

    public void toCommentUser(String nickname) {
        if (TextUtils.isEmpty(nickname)) return;
        builder.inputHint = Utils.getApp().getString(R.string.portal_dialog_input_hint_user, nickname);
        inputComment.setHint(builder.inputHint);
    }

    public void toParentId(int toParentId) {
        builder.toParentId = toParentId;
    }

    /**
     * 必须先调用
     *
     * @param toParentId
     */
    public void resetData(int toParentId) {
        //和上次一样就不重置UI
        if (builder.toParentId == toParentId) return;
        resetData();
    }

    public void resetData() {
        inputComment.getText().clear();
        inputComment.setHint(DEFAULT_INPUT_HINT);
        adapter.setNewData(null);
        recyclerView.setVisibility(View.GONE);
        chooseVideo.setEnabled(true);
        chooseImage.setEnabled(true);
    }

    //=============================================================private method
    private void clickChooseVideo() {
        KeyboardUtils.hideSoftInput(inputComment);
        chooseVideo();
    }

    private void clickChooseImage() {
        KeyboardUtils.hideSoftInput(inputComment);
        chooseImage();
    }

    private void clickChooseAt() {
        KeyboardUtils.hideSoftInput(inputComment);
        ARouter.getInstance()
                .build(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY)
                .withTransition(R.anim.popup_up_in, R.anim.popup_up_out)
                .navigation(builder.activity, builder.chooseAtRequestCode);
    }

    private void clickCheckAt() {
        KeyboardUtils.hideSoftInput(inputComment);
        ARouter.getInstance()
                .build(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY)
                .withTransition(R.anim.popup_up_in, R.anim.popup_up_out)
                .navigation(builder.activity, builder.checkAtRequestCode);
    }

    private void clickChooseSend() {
        if (onSendListener != null) onSendListener.onClick(chooseSend);
    }

    @SuppressLint("CheckResult")
    private void chooseVideo() {
        new RxPermissions((FragmentActivity) builder.activity)
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        //授权成功
                        ImagePicker.from(builder.activity)
                                .choose(MimeType.ofVideo())
                                .capture(true)
                                .captureStrategy(new CaptureStrategy(true, getAuthority(), ""))
                                .showSingleMediaType(true)
                                .imageEngine(new Glide4Engine())
                                .forResult(builder.chooseVideoRequestCode);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //授权失败
                    } else {
                        //授权失败，不能再次询问
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void chooseImage() {
        new RxPermissions((FragmentActivity) builder.activity)
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        //授权成功
                        ImagePicker.from(builder.activity)
                                .choose(MimeType.ofImage())
                                .maxSelectable(9 - adapter.getData().size())
                                .capture(true)
                                .captureStrategy(new CaptureStrategy(true, getAuthority(), ""))
                                .showSingleMediaType(true)
                                .imageEngine(new Glide4Engine())
                                .forResult(builder.chooseImageRequestCode);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //授权失败
                    } else {
                        //授权失败，不能再次询问
                    }
                });
    }

    private String getAuthority() {
        return builder.activity.getPackageName() + ".imagepicker.provider";
    }

    //=============================================================TextWatcher
    private int beforeTextlength;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeTextlength = s.length();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int afterTextLength = s.length();
        // @人检测功能
        if (afterTextLength != 0 && beforeTextlength < afterTextLength) {
            char at = s.charAt(s.length() - 1);
            if ("@".equals(String.valueOf(at))) {
                //检测到用户有@操作
                clickCheckAt();
            }
        }
        // 字数限制
        chooseSend.setEnabled(afterTextLength >= 0);
    }

    //=============================================================builder
    public static class Builder {
        protected Activity activity;
        protected Fragment fragment;
        private int chooseVideoRequestCode;
        private int chooseImageRequestCode;
        private int chooseAtRequestCode;
        private int checkAtRequestCode;
        private boolean isShowImage = true;
        private boolean isShowVideo = true;
        private String inputHint;
        private int toParentId = -1;//记录被回复的评论ID

        public Builder(Activity activity) {
            this(activity, null);
        }

        public Builder(Fragment fragment) {
            this(fragment.getActivity(), fragment);
        }

        public Builder(Activity activity, Fragment fragment) {
            this.activity = activity;
            this.fragment = fragment;
        }

        public Builder chooseVideoForResult(int requestCode) {
            this.chooseVideoRequestCode = requestCode;
            return this;
        }

        public Builder chooseImageForResult(int requestCode) {
            this.chooseImageRequestCode = requestCode;
            return this;
        }

        public Builder chooseAtForResult(int requestCode) {
            this.chooseAtRequestCode = requestCode;
            return this;
        }

        public Builder checkAtForResult(int requestCode) {
            this.checkAtRequestCode = requestCode;
            return this;
        }

        public Builder setShowImage(boolean isShowImage) {
            this.isShowImage = isShowImage;
            return this;
        }

        public Builder setShowVideo(boolean isShowVideo) {
            this.isShowVideo = isShowVideo;
            return this;
        }

        public Builder toCommentUser(String nickname) {
            if (!TextUtils.isEmpty(nickname))
                this.inputHint = Utils.getApp().getString(R.string.portal_dialog_input_hint_user, nickname);
            return this;
        }

        public Builder toParentId(int toParentId) {
            this.toParentId = toParentId;
            return this;
        }

        public PortalInputDialog build() {
            return new PortalInputDialog(this);
        }
    }

}
