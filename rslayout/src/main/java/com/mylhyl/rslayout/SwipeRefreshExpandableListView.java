package com.mylhyl.rslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * SwipeRefreshLayout 包含 ExpandableListView 布局<br>
 * 如须自定义加载框，可继承此类重写 {@link #createFooter() createFooter}
 * <p>Created by hupei on 2016/5/16.
 */
public class SwipeRefreshExpandableListView extends SwipeRefreshListView<ExpandableListView> {
    protected ExpandableListAdapter mAdapter;

    public SwipeRefreshExpandableListView(Context context) {
        super(context);
    }

    public SwipeRefreshExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 为 ExpandableListAdapter 设置适配器
     *
     * @param adapter
     */
    public final void setAdapter(ExpandableListAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    protected final void addFooter() {
        if (mAdapter == null)
            throw new NullPointerException("mAdapter is null please call CygSwipeRefreshLayout.setAdapter");
        //如有设置上拉加载监听才添加 FooterView
        if (mOnListLoadListener != null && mContentView.getFooterViewsCount() >= 0) {
            mContentView.addFooterView(mFooterView);
        }
        mContentView.setAdapter(mAdapter);
        //避免数据不够一屏时，加载更新在显示中，所以得移除
        if (mOnListLoadListener != null && mContentView.getFooterViewsCount() > 0)
            mContentView.removeFooterView(mFooterView);
    }
}
