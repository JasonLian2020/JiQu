package me.jessyan.armscomponent.commonui.base.mvp.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public abstract class BaseRecyclerAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {
    public BaseRecyclerAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseRecyclerAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseRecyclerAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    //=========================================getResources method
    protected String getString(@StringRes int resId) {
        return Utils.getApp().getString(resId);
    }

    protected String getString(@StringRes int id, Object... formatArgs) {
        return Utils.getApp().getString(id, formatArgs);
    }

    @ColorInt
    protected int getColor(@ColorRes int id) {
        return Utils.getApp().getResources().getColor(id);
    }

    protected Drawable getDrawable(@DrawableRes int id) {
        return Utils.getApp().getResources().getDrawable(id);
    }

    protected float getDimension(@DimenRes int id) {
        return Utils.getApp().getResources().getDimension(id);
    }

    /**
     * 结果转换为int，并且小数部分四舍五入
     */
    protected int getDimensionPixelSize(@DimenRes int id) {
        return Utils.getApp().getResources().getDimensionPixelSize(id);
    }

    /**
     * 将结果转换为int，并且偏移转换，直接截断小数位，即取整
     */
    protected int getDimensionPixelOffset(@DimenRes int id) {
        return Utils.getApp().getResources().getDimensionPixelOffset(id);
    }
}