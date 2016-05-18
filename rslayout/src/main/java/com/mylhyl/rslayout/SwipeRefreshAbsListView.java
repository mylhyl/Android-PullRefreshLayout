package com.mylhyl.rslayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * SwipeRefreshLayout 加 AbsListView 布局<br>
 * 子类必须实现 {@linkplain BaseSwipeRefresh#showFooter() showFooter}、
 * {@linkplain BaseSwipeRefresh#hideFooter() hideFooter}、
 * {@linkplain BaseSwipeRefresh#createFooter() createFooter}方法
 * <p> Created by hupei on 2016/5/12.
 */
public abstract class SwipeRefreshAbsListView<T extends AbsListView> extends BaseSwipeRefresh<T> {

    public SwipeRefreshAbsListView(Context context) {
        super(context);
    }

    public SwipeRefreshAbsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 为 ListView or GridView 设置适配器
     *
     * @param adapter
     */
    public final void setAdapter(ListAdapter adapter) {
        if (adapter == null)
            throw new NullPointerException("mAdapter is null please call CygSwipeRefreshLayout.setAdapter");
        getScrollView().setOnScrollListener(new SwipeRefreshOnScrollListener());
        getScrollView().setAdapter(adapter);
        super.setEmptyDataAdapter(adapter);
    }

    @Override
    protected View createFooter() {
        final float scale = getResources().getDisplayMetrics().density;
        int height = (int) (100 / scale + 0.5f);
        LinearLayout footerView = new LinearLayout(getContext());
        footerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height));
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

    public final void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        getScrollView().setOnItemClickListener(listener);
    }
}
