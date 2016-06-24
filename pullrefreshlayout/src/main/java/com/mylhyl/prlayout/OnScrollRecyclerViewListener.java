package com.mylhyl.prlayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mylhyl.prlayout.internal.ISwipeRefresh;

/**
 * Created by hupei on 2016/5/20.
 */
public final class OnScrollRecyclerViewListener extends RecyclerView.OnScrollListener {
    private ISwipeRefresh mISwipeRefresh;
    private RecyclerView.OnScrollListener mOnScrollListener;

    public OnScrollRecyclerViewListener(ISwipeRefresh iSwipeRefresh) {
        mISwipeRefresh = iSwipeRefresh;
    }

    public OnScrollRecyclerViewListener(ISwipeRefresh iSwipeRefresh, RecyclerView.OnScrollListener onScrollListener) {
        mISwipeRefresh = iSwipeRefresh;
        mOnScrollListener = onScrollListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (null != mOnScrollListener)
            mOnScrollListener.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!isFirstItemVisible(recyclerView)
                && mISwipeRefresh.isEnabledLoad() && !mISwipeRefresh.isLoading()
                && isLastItemVisible(recyclerView)) {
            mISwipeRefresh.loadData();// 滑动底部自动执行上拉加载
        }

        if (null != mOnScrollListener)
            mOnScrollListener.onScrolled(recyclerView, dx, dy);
    }

    /**
     * 判断第一个条目是否完全可见
     *
     * @param recyclerView
     * @return
     */
    private boolean isFirstItemVisible(RecyclerView recyclerView) {
        final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        // 如果未设置Adapter或者Adapter没有数据可以下拉刷新
        if (null == adapter || adapter.getItemCount() == 0) {
            return true;
        }
        // 第一个条目完全展示,可以刷新
        if (getFirstVisiblePosition(recyclerView) == 0) {
            return recyclerView.getChildAt(0).getTop() >= recyclerView.getTop();
        }
        return false;
    }

    /**
     * 获取第一个可见子View的位置下标
     *
     * @param recyclerView
     * @return
     */
    private int getFirstVisiblePosition(RecyclerView recyclerView) {
        View firstVisibleChild = recyclerView.getChildAt(0);
        return firstVisibleChild != null ?
                recyclerView.getChildAdapterPosition(firstVisibleChild) : -1;
    }

    /**
     * 判断最后一个条目是否完全可见
     *
     * @param recyclerView
     * @return
     */
    private boolean isLastItemVisible(RecyclerView recyclerView) {
        final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        // 如果未设置Adapter或者Adapter没有数据可以上拉刷新
        if (null == adapter || adapter.getItemCount() == 0) {
            return true;
        }
        // 最后一个条目View完全展示,可以刷新
        int lastVisiblePosition = getLastVisiblePosition(recyclerView);
        if (lastVisiblePosition >= recyclerView.getAdapter().getItemCount() - 1) {
            return recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom()
                    <= recyclerView.getBottom();
        }
        return false;
    }

    /**
     * 获取最后一个可见子View的位置下标
     *
     * @param recyclerView
     * @return
     */
    private int getLastVisiblePosition(RecyclerView recyclerView) {
        View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        return lastVisibleChild != null ? recyclerView.getChildAdapterPosition(lastVisibleChild) : -1;
    }
}
