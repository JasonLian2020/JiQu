package com.sulikeji.pipixia.publish.mvp.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DetailGridInset extends RecyclerView.ItemDecoration {
    private int mSpanCount;
    private int mSpacing;
    private boolean mHasHeaderView;
    private boolean mIncludeEdge;

    public DetailGridInset(int spanCount, int spacing, boolean hasHeaderView) {
        this(spanCount, spacing, hasHeaderView, false);
    }

    public DetailGridInset(int spanCount, int spacing, boolean hasHeaderView, boolean includeEdge) {
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
        this.mHasHeaderView = hasHeaderView;
        this.mIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        // 如果有头布局，就是相当于一个item，占用了0的位置
        if (mHasHeaderView && position == 0) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        // 有头需要去掉1
        if (mHasHeaderView) position = position - 1;
        // item column
        int column = position % mSpanCount;

        if (mIncludeEdge) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = mSpacing - column * mSpacing / mSpanCount;
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * mSpacing / mSpanCount;

            if (position < mSpanCount) { // top edge
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing; // item bottom
        } else {
            // column * ((1f / spanCount) * spacing)
            outRect.left = column * mSpacing / mSpanCount;
            // spacing - (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount;
            if (position >= mSpanCount) {
                outRect.top = mSpacing; // item top
            }
        }
    }
}
