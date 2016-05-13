package com.mylhyl.rslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by hupei on 2016/5/12.
 *
 * @param <T>
 */
public abstract class AbsListViewSwipeRefresh<T extends AbsListView> extends BaseSwipeRefresh<T> {
    protected ListAdapter mAdapter;

    public AbsListViewSwipeRefresh(Context context) {
        super(context);
    }

    public AbsListViewSwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public final void setAdapter(ListAdapter adapter) {
        this.mAdapter = adapter;
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
    protected void onAddFooterView() {
        if (mAdapter == null)
            throw new NullPointerException("mAdapter is null please call CygSwipeRefreshLayout.setAdapter");
    }
}
