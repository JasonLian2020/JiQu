package me.jessyan.armscomponent.commonui.popup;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.armscomponent.commonui.R;
import me.jessyan.autosize.AutoSizeConfig;
import razerdp.basepopup.BasePopupWindow;

public class MorePopup extends BasePopupWindow {
    public MorePopup(@NonNull Builder builder) {
        super(builder.context, builder.width, builder.height);
        setPopupGravity(Gravity.BOTTOM);
        initView(builder);
        // Don't keep a Context reference in the Builder after this point
        builder.context = null;
    }

    public static class Builder {
        protected Context context;
        protected int width = AutoSizeConfig.getInstance().getScreenWidth();
        protected int height = -2;
        protected List<ClickItemEvent> clickItemList = new ArrayList<>();
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

        public Builder addClickItem(@DrawableRes int iconId, @StringRes int titleId, String clickTag) {
            clickItemList.add(new ClickItemEvent(iconId, titleId, clickTag));
            return this;
        }

        public Builder autoDismiss(boolean autoDismiss) {
            this.autoDismiss = autoDismiss;
            return this;
        }

        public MorePopup build() {
            return new MorePopup(this);
        }
    }

    public static class ClickItemEvent {
        @DrawableRes
        private int iconId;
        @StringRes
        private int titleId;
        private String clickTag;

        public int getIconId() {
            return iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public int getTitleId() {
            return titleId;
        }

        public void setTitleId(int titleId) {
            this.titleId = titleId;
        }

        public String getClickTag() {
            return clickTag;
        }

        public void setClickTag(String clickTag) {
            this.clickTag = clickTag;
        }

        public ClickItemEvent(int iconId, int titleId, String clickTag) {
            this.iconId = iconId;
            this.titleId = titleId;
            this.clickTag = clickTag;
        }
    }

    //=============================================================init
    private void initView(@NonNull final Builder builder) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ClickListAdapter listAdapter = new ClickListAdapter(builder.clickItemList);
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            ClickItemEvent item = (ClickItemEvent) adapter.getItem(position);
            if (item == null) return;
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(item.iconId, item.titleId, item.clickTag);
            if (builder.autoDismiss) dismiss();
        });
        recyclerView.setAdapter(listAdapter);
        //cancel
        View cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            if (onBottomClickListener != null) onBottomClickListener.onClick(v);
            if (builder.autoDismiss) dismiss();
        });
    }

    //=============================================================adapter
    class ClickListAdapter extends BaseQuickAdapter<ClickItemEvent, BaseViewHolder> {

        public ClickListAdapter(@Nullable List<ClickItemEvent> data) {
            super(R.layout.public_item_popup_more, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ClickItemEvent item) {
            helper.setText(R.id.title, item.getTitleId());
        }
    }

    //=============================================================super methods
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.public_popup_more);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 250);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 250);
    }

    @Override
    protected View onFindDecorView(Activity activity) {
        if (onRootViewProvider != null) return onRootViewProvider.onProvideRootView();
        return super.onFindDecorView(activity);
    }

    //=============================================================interface
    private OnBottomClickListener onBottomClickListener;

    public void setOnBottomClickListener(OnBottomClickListener onBottomClickListener) {
        this.onBottomClickListener = onBottomClickListener;
    }

    public interface OnBottomClickListener {
        void onClick(View v);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(@DrawableRes int iconId, @StringRes int titleId, String clickTag);
    }

    private OnRootViewProvider onRootViewProvider;

    public void setOnRootViewProvider(OnRootViewProvider onRootViewProvider) {
        this.onRootViewProvider = onRootViewProvider;
    }

    public interface OnRootViewProvider {
        View onProvideRootView();
    }
}
