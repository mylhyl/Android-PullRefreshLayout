package com.mylhyl.prlayout;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.mylhyl.prlayout.internal.ISwipeRefresh;

final class LoadSwipeRefresh extends SwipeRefreshLayout {
    // Default offset in dips from the top of the view to where the progress spinner should stop
    private static final int DEFAULT_CIRCLE_TARGET = 64;
    private float mSpinnerFinalOffset;
    private static final int[] COLOR_RES_IDS = new int[]{android.R.color.holo_blue_light, android.R.color.holo_red_light,
            android.R.color.holo_orange_light, android.R.color.holo_green_light};

    private int[] mColorResIds = COLOR_RES_IDS;
    private int mTouchSlop;
    private float mPrevX;// 上一次触摸时的X坐标

    public LoadSwipeRefresh(Context context) {
        this(context, null);
    }

    public LoadSwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mSpinnerFinalOffset = DEFAULT_CIRCLE_TARGET * metrics.density;
        // 触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);
                // 增加容差，让下拉刷新在竖直滑动时就可以触发
                if (xDiff > mTouchSlop + DEFAULT_CIRCLE_TARGET) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);
    }
}
