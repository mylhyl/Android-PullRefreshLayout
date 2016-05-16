package com.mylhyl.rslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * SwipeRefreshLayout 包含 ListView 布局<br>
 * 如须自定义加载框，可继承此类重写 {@link #createFooter() createFooter}方法
 * <p>Created by hupei on 2016/5/12.
 */
public class SwipeRefreshListView<T extends ListView> extends SwipeRefreshAbsListView<T> {

    public SwipeRefreshListView(Context context) {
        super(context);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected final void showFooter() {
        if (mFooterView != null && mContentView != null && mContentView.getFooterViewsCount() >= 0) {
            mContentView.addFooterView(mFooterView);
            mContentView.post(new Runnable() {
                @Override
                public void run() {
                    mContentView.smoothScrollByOffset(mContentView.getBottom());
                }
            });
        }
    }

    @Override
    protected final void hideFooter() {
        if (mFooterView != null && mContentView != null && mContentView.getFooterViewsCount() > 0) {
            mContentView.removeFooterView(mFooterView);
        }
    }

    @Override
    protected void addFooter() {
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
