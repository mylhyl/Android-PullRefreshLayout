package com.mylhyl.rslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * SwipeRefreshLayout 加 ListView 刷新，可继承此类重写 {@link #onCreateFooterView() 自定义加载框}
 * Created by hupei on 2016/5/12.
 */
public class SwipeRefreshListView extends BaseSwipeRefresh<ListView> {
    protected ListAdapter mAdapter;

    public SwipeRefreshListView(Context context) {
        super(context);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public final void setAdapter(ListAdapter adapter) {
        this.mAdapter = adapter;
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
    protected View onCreateFooterView() {
        LinearLayout footerView = new LinearLayout(getContext());
        footerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100));
        footerView.setOrientation(LinearLayout.HORIZONTAL);
        footerView.setGravity(Gravity.CENTER);

        ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmallInverse);
        footerView.addView(progressBar, new AbsListView.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView(getContext());
        textView.setText("加载数据中...");
        textView.setTextAppearance(getContext(), android.R.attr.textAppearanceMedium);
        footerView.addView(textView, new LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        return footerView;
    }

    @Override
    protected final void onAddFooterView() {
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
