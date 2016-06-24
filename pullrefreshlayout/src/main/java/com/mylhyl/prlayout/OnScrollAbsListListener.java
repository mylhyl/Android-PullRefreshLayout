package com.mylhyl.prlayout;

import android.widget.AbsListView;

import com.mylhyl.prlayout.internal.ISwipeRefresh;

/**
 * Created by hupei on 2016/5/20.
 */
public final class OnScrollAbsListListener implements AbsListView.OnScrollListener {
    private ISwipeRefresh mISwipeRefresh;
    private AbsListView.OnScrollListener mOnScrollListener;

    public OnScrollAbsListListener(ISwipeRefresh iSwipeRefresh) {
        mISwipeRefresh = iSwipeRefresh;
    }

    public OnScrollAbsListListener(ISwipeRefresh iSwipeRefresh, AbsListView.OnScrollListener onScrollListener) {
        mISwipeRefresh = iSwipeRefresh;
        mOnScrollListener = onScrollListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        /**
         正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
         回调顺序如下：
         SCROLL_STATE_TOUCH_SCROLL = 1     正在滚动
         SCROLL_STATE_FLING = 2            手指做了抛的动作（手指离开屏幕前，用力滑了一下）
         SCROLL_STATE_IDLE = 0             停止滚动
         */
        if (null != mOnScrollListener)
            mOnScrollListener.onScrollStateChanged(view, scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        /**
         滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
         firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
         visibleItemCount：当前能看见的列表项个数（小半个也算）
         totalItemCount：列表项共数
         */
        if (firstVisibleItem > 0
                && mISwipeRefresh.isEnabledLoad() && !mISwipeRefresh.isLoading()
                && view.getLastVisiblePosition() == (totalItemCount - 1)) {
            mISwipeRefresh.loadData();// 滑动底部自动执行上拉加载
        }


        if (null != mOnScrollListener)
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }
}
