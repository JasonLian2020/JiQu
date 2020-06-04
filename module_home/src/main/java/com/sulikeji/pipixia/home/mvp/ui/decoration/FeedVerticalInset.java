package com.sulikeji.pipixia.home.mvp.ui.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.Utils;

public class FeedVerticalInset extends RecyclerView.ItemDecoration {
    private Paint dividerPaint;
    private int dividerHeight;
    @ColorRes
    private int dividerColor;

    public FeedVerticalInset(int dividerHeight, @ColorRes int dividerColor) {
        this.dividerHeight = dividerHeight;
        this.dividerColor = dividerColor;
        dividerPaint = new Paint();
        dividerPaint.setColor(Utils.getApp().getResources().getColor(dividerColor));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (position == 0) outRect.top = dividerHeight;
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        int childWidth = parent.getWidth();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view); // item position
            if (position == 0) {
                float extraLeft = 0;
                float extraTop = view.getTop() - dividerHeight;
                float extraRight = childWidth;
                float extraBottom = view.getBottom();
                c.drawRect(extraLeft, extraTop, extraRight, extraBottom, dividerPaint);
            }
            float left = 0;
            float top = view.getBottom();
            float right = childWidth;
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
