package com.mylhyl.prlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * SwipeRefreshLayout 加 AbsListView 布局<br>
 * 子类必须实现 {@linkplain BaseSwipeRefresh#createScrollView(Context, AttributeSet)}  createScrollView}方法
 * <p> Created by hupei on 2016/5/12.
 */
public abstract class SwipeRefreshAbsListView<T extends AbsListView> extends BaseSwipeRefresh<T> {
    private ListAdapter mEmptyDataSetAdapter;
    private EmptyDataSetObserver mDataSetObserver;

    public SwipeRefreshAbsListView(Context context) {
        super(context);
    }

    public SwipeRefreshAbsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public final void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        getScrollView().setOnItemClickListener(listener);
    }

    /**
     * 为 ListView or GridView 设置适配器
     *
     * @param adapter
     */
    public final void setAdapter(ListAdapter adapter) {
        if (adapter == null)
            throw new NullPointerException("mAdapter is null please call CygSwipeRefreshLayout.setAdapter");
        getScrollView().setOnScrollListener(new OnScrollAbsListListener(this));
        getScrollView().setAdapter(adapter);
        registerDataSetObserver(adapter);
    }

    final void registerDataSetObserver(ListAdapter adapter) {
        mEmptyDataSetAdapter = adapter;
        if (mEmptyDataSetAdapter != null && mDataSetObserver != null) {
            mEmptyDataSetAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        if (mEmptyDataSetAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new EmptyDataSetObserver();
            mEmptyDataSetAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mEmptyDataSetAdapter != null && mDataSetObserver != null) {
            mEmptyDataSetAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
    }

    private class EmptyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            updateEmptyViewShown(mEmptyDataSetAdapter == null || mEmptyDataSetAdapter.isEmpty());
        }
    }
}
