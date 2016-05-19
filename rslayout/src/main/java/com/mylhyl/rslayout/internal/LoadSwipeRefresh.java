package com.mylhyl.rslayout.internal;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.mylhyl.rslayout.internal.ISwipeRefresh;

public final class LoadSwipeRefresh extends SwipeRefreshLayout {

    private static final int[] COLOR_RES_IDS = new int[]{android.R.color.holo_blue_light, android.R.color.holo_red_light,
            android.R.color.holo_orange_light, android.R.color.holo_green_light};

    private int[] mColorResIds = COLOR_RES_IDS;

    private ISwipeRefresh mISwipeRefresh;

    public LoadSwipeRefresh(Context context) {
        this(context, null);
    }

    public LoadSwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public final void showRefreshHeader() {
        showRefreshHeader(mColorResIds);
    }

    public final void showRefreshHeader(int... colorResIds) {
        showRefreshHeader(false, 0, 24, colorResIds);
    }

    public final void showRefreshHeader(boolean scale, int start, int end, int... colorResIds) {
        // 设置刷新时动画的颜色，可以设置4个
        setColorSchemeResources(colorResIds);
        // 首次加载显示下拉动画，关键在于源码中的 mUsingCustomStart = true
        setProgressViewOffset(scale, start, end);
        mISwipeRefresh.setRefreshing(true);
    }

    public void setISwipeRefresh(ISwipeRefresh iSwipeRefresh) {
        this.mISwipeRefresh = iSwipeRefresh;
    }

    public ISwipeRefresh getISwipeRefresh() {
        return mISwipeRefresh;
    }
}
