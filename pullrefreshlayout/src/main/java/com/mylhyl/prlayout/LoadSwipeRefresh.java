package com.mylhyl.prlayout;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.mylhyl.prlayout.internal.ISwipeRefresh;

final class LoadSwipeRefresh extends SwipeRefreshLayout {
    // Default offset in dips from the top of the view to where the progress spinner should stop
    private static final int DEFAULT_CIRCLE_TARGET = 64;
    private float mSpinnerFinalOffset;
    private static final int[] COLOR_RES_IDS = new int[]{android.R.color.holo_blue_light, android.R.color.holo_red_light,
            android.R.color.holo_orange_light, android.R.color.holo_green_light};

    private int[] mColorResIds = COLOR_RES_IDS;

    public LoadSwipeRefresh(Context context) {
        this(context, null);
    }

    public LoadSwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mSpinnerFinalOffset = DEFAULT_CIRCLE_TARGET * metrics.density;
    }

    public final void autoRefresh() {
        autoRefresh(mColorResIds);
    }

    public final void autoRefresh(int... colorResIds) {
        autoRefresh(false, 0, (int) mSpinnerFinalOffset, colorResIds);
    }

    public final void autoRefresh(boolean scale, int start, int end, int... colorResIds) {
        // 设置刷新时动画的颜色，可以设置4个
        setColorSchemeResources(colorResIds);
        // 首次加载显示下拉动画，关键在于源码中的 mUsingCustomStart = true
        setProgressViewOffset(scale, start, end);
    }
}
