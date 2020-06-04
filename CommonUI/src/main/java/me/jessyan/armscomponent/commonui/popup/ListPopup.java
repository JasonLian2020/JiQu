package me.jessyan.armscomponent.commonui.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import com.chad.library.adapter.base.BaseQuickAdapter;

import me.jessyan.armscomponent.commonui.R;
import razerdp.basepopup.BasePopupWindow;

public class ListPopup extends BasePopupWindow {

    private RecyclerView recyclerView;

    public ListPopup(@NonNull Builder builder) {
        super(builder.context, builder.width, builder.height);
        setClipChildren(false);//不裁剪contentView
        initView(builder);
        // Don't keep a Context reference in the Builder after this point
        builder.context = null;
    }

    public static class Builder {
        public int width = -2;
        public int height = -2;
        public RecyclerView.LayoutManager layoutManager;
        public BaseQuickAdapter adapter;
        public boolean isUpPopup = true;//true表示从上至下弹出，false表示从下至上弹出
        public Context context;

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

        public Builder setUpPopup(boolean isUpPopup) {
            this.isUpPopup = isUpPopup;
            return this;
        }

        public Builder setLayoutManager(RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
            return this;
        }

        public Builder setAdapter(BaseQuickAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public ListPopup build() {
            return new ListPopup(this);
        }
    }

    //=============================================================init
    private void initView(@NonNull Builder builder) {
        recyclerView = findViewById(R.id.recyclerView);
        if (builder.layoutManager != null)
            recyclerView.setLayoutManager(builder.layoutManager);
        if (builder.adapter != null)
            recyclerView.setAdapter(builder.adapter);
        //动画
        setShowAnimation(builder.isUpPopup ? getPopupUpAnimation(true) : getPopupDownAnimation(true));
        setDismissAnimation(builder.isUpPopup ? getPopupUpAnimation(false) : getPopupDownAnimation(false));
    }

    //=============================================================super methods
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.public_popup_list);
    }

    //向下弹出，向上收回
    private Animation getPopupUpAnimation(boolean isShow) {
        float fromX = isShow ? 0 : 1.0f;
        float toX = isShow ? 1.0f : 0;
        float fromY = isShow ? 0 : 1.0f;
        float toY = isShow ? 1.0f : 0;
        int pivotXType = Animation.RELATIVE_TO_SELF;
        float pivotXValue = isShow ? 1.0f : 1.0f;
        int pivotYType = Animation.RELATIVE_TO_SELF;
        float pivotYValue = isShow ? 0 : 0;
        Animation showAnimation = new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);
        if (isShow) showAnimation.setInterpolator(new OvershootInterpolator());
        showAnimation.setDuration(250);
        return showAnimation;
    }

    //向上弹出，向下收回
    private Animation getPopupDownAnimation(boolean isShow) {
        float fromX = isShow ? 0 : 1.0f;
        float toX = isShow ? 1.0f : 0;
        float fromY = isShow ? 0 : 1.0f;
        float toY = isShow ? 1.0f : 0;
        int pivotXType = Animation.RELATIVE_TO_SELF;
        float pivotXValue = isShow ? 1.0f : 1.0f;
        int pivotYType = Animation.RELATIVE_TO_SELF;
        float pivotYValue = isShow ? 1.0f : 1.0f;
        Animation showAnimation = new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);
        if (isShow) showAnimation.setInterpolator(new OvershootInterpolator());
        showAnimation.setDuration(250);
        return showAnimation;
    }
}
