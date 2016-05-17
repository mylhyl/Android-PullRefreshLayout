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
        final ListView contentView = getContentView();
        if (mFooterView != null && contentView != null && contentView.getFooterViewsCount() >= 0) {
            contentView.addFooterView(mFooterView, null, false);
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    contentView.smoothScrollByOffset(contentView.getBottom());
                }
            });
        }
    }

    @Override
    protected final void hideFooter() {
        ListView contentView = getContentView();
        if (mFooterView != null && contentView != null && contentView.getFooterViewsCount() > 0) {
            contentView.removeFooterView(mFooterView);
        }
    }

    @Override
    protected void addFooter() {
        ListView contentView = getContentView();
        if (getListAdapter() == null)
            throw new NullPointerException("mAdapter is null please call CygSwipeRefreshLayout.setAdapter");
        //如有设置上拉加载监听才添加 FooterView
        if (mOnListLoadListener != null && contentView.getFooterViewsCount() >= 0) {
            contentView.addFooterView(mFooterView, null, false);
        }
        contentView.setAdapter(getListAdapter());
        //避免数据不够一屏时，加载更新在显示中，所以得移除
        if (mOnListLoadListener != null && contentView.getFooterViewsCount() > 0)
            contentView.removeFooterView(mFooterView);
    }
}
