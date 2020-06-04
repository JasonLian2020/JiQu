package me.jessyan.armscomponent.commonui.share;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
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

public class SharePopup extends BasePopupWindow {
    public SharePopup(@NonNull Builder builder) {
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
        protected List<ClickItemEvent> shareItemList = new ArrayList<>();
        protected List<ClickItemEvent> customItemList = new ArrayList<>();
        protected boolean autoDismiss;

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

        public Builder addShareItem(@DrawableRes int iconId, @StringRes int titleId, String clickTag) {
            shareItemList.add(new ClickItemEvent(iconId, titleId, clickTag));
            return this;
        }

        public Builder addCustomItem(@DrawableRes int iconId, @StringRes int titleId, String clickTag) {
            customItemList.add(new ClickItemEvent(iconId, titleId, clickTag));
            return this;
        }

        public Builder autoDismiss(boolean autoDismiss) {
            this.autoDismiss = autoDismiss;
            return this;
        }

        public SharePopup build() {
            return new SharePopup(this);
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
        //第一行
        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new GridLayoutManager(builder.context, 5));
        ShareListAdapter adapter1 = new ShareListAdapter(builder.shareItemList);
        adapter1.setOnItemClickListener((adapter, view, position) -> {
            ClickItemEvent item = (ClickItemEvent) adapter.getItem(position);
            if (item == null) return;
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(item.iconId, item.titleId, item.clickTag);
            if (builder.autoDismiss) dismiss();
        });
        recyclerView1.setAdapter(adapter1);
        //第二行
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new GridLayoutManager(builder.context, 5));
        ShareListAdapter adapter2 = new ShareListAdapter(builder.customItemList);
        adapter2.setOnItemClickListener((adapter, view, position) -> {
            ClickItemEvent item = (ClickItemEvent) adapter.getItem(position);
            if (item == null) return;
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(item.iconId, item.titleId, item.clickTag);
            if (builder.autoDismiss) dismiss();
        });
        recyclerView2.setAdapter(adapter2);
        View divider = findViewById(R.id.divider);
        if (builder.customItemList == null || builder.customItemList.isEmpty()) {
            recyclerView2.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
        View cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            if (onBottomClickListener != null) onBottomClickListener.onClick(v);
            if (builder.autoDismiss) dismiss();
        });
    }

    //=============================================================adapter
    class ShareListAdapter extends BaseQuickAdapter<ClickItemEvent, BaseViewHolder> {

        public ShareListAdapter(@Nullable List<ClickItemEvent> data) {
            super(R.layout.public_item_popup_share, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ClickItemEvent item) {
            helper.setImageResource(R.id.icon, item.getIconId());
            helper.setText(R.id.title, item.getTitleId());
        }
    }

    //=============================================================super methods
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.public_popup_share);
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
