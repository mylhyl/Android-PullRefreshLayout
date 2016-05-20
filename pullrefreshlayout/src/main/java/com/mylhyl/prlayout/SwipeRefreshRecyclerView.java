package com.mylhyl.prlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.mylhyl.prlayout.internal.OnScrollRecyclerViewListener;

/**
 * SwipeRefreshLayout 加 RecyclerView 布局<br>
 * <p> Created by hupei on 2016/5/12.
 */
public class SwipeRefreshRecyclerView extends BaseSwipeRefresh<RecyclerView> {
    private RecyclerView.Adapter mEmptyDataSetAdapter;
    private EmptyDataSetObserver mDataSetObserver;

    public SwipeRefreshRecyclerView(Context context) {
        super(context);
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 为 RecyclerView 设置适配器
     *
     * @param adapter
     */
    public final void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null)
            throw new NullPointerException("mAdapter is null please call CygSwipeRefreshLayout.setAdapter");
        getScrollView().setOnScrollListener(new OnScrollRecyclerViewListener(getLoadSwipeRefresh()));
        getScrollView().setAdapter(adapter);
        registerAdapterDataObserver(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        getScrollView().setLayoutManager(layout);
    }

    @Override
    protected RecyclerView createScrollView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context, attrs);
        recyclerView.setId(android.R.id.list);
        return recyclerView;
    }


    private void registerAdapterDataObserver(RecyclerView.Adapter adapter) {
        mEmptyDataSetAdapter = adapter;
        if (mEmptyDataSetAdapter != null && mDataSetObserver != null) {
            mEmptyDataSetAdapter.unregisterAdapterDataObserver(mDataSetObserver);
        }
        if (mEmptyDataSetAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new EmptyDataSetObserver();
            mEmptyDataSetAdapter.registerAdapterDataObserver(mDataSetObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mEmptyDataSetAdapter != null && mDataSetObserver != null) {
            mEmptyDataSetAdapter.unregisterAdapterDataObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
    }

    private class EmptyDataSetObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            updateEmptyViewShown(mEmptyDataSetAdapter == null || mEmptyDataSetAdapter.getItemCount() == 0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }
    }
}
