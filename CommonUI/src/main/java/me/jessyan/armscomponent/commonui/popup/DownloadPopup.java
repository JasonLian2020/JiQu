package me.jessyan.armscomponent.commonui.popup;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;

import me.jessyan.armscomponent.commonui.R;
import me.jessyan.autosize.utils.AutoSizeUtils;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.SimpleAnimationUtils;

public class DownloadPopup extends BasePopupWindow {

    private TextView downloadProgress;

    public DownloadPopup(@NonNull Builder builder) {
        super(builder.context, builder.width, builder.height);
        setPopupGravity(Gravity.CENTER);
        setOutSideDismiss(false);
        setOutSideTouchable(false);
        setBackPressEnable(false);
        initView(builder);
        // Don't keep a Context reference in the Builder after this point
        builder.context = null;
    }

    public static class Builder {
        protected Context context;
        protected int width = AutoSizeUtils.dp2px(Utils.getApp(), 300);
        protected int height = AutoSizeUtils.dp2px(Utils.getApp(), 300);
        protected int progress = 0;
        protected boolean autoDismiss = true;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setProgress(int progress) {
            this.progress = progress;
            return this;
        }

        public Builder autoDismiss(boolean autoDismiss) {
            this.autoDismiss = autoDismiss;
            return this;
        }

        public DownloadPopup build() {
            return new DownloadPopup(this);
        }
    }

    //=============================================================init
    private void initView(@NonNull final Builder builder) {
        findViewById(R.id.downloadClose).setOnClickListener(v -> {
            if (onCloseListener != null) onCloseListener.onClick(v);
        });
        downloadProgress = findViewById(R.id.downloadProgress);
        setProgress(builder.progress);
    }

    //=============================================================public methods
    public void setProgress(int progress) {
        if (downloadProgress != null) {
            downloadProgress.setText(progress + "%");
        }
    }

    //=============================================================super methods
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.public_popup_download);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return SimpleAnimationUtils.getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return SimpleAnimationUtils.getDefaultScaleAnimation(false);
    }

    @Override
    protected View onFindDecorView(Activity activity) {
        if (onRootViewProvider != null) return onRootViewProvider.onProvideRootView();
        return super.onFindDecorView(activity);
    }

    //=============================================================interface
    private View.OnClickListener onCloseListener;

    public void setOnCloseListener(View.OnClickListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    private OnRootViewProvider onRootViewProvider;

    public void setOnRootViewProvider(OnRootViewProvider onRootViewProvider) {
        this.onRootViewProvider = onRootViewProvider;
    }

    public interface OnRootViewProvider {
        View onProvideRootView();
    }
}
