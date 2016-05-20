package com.mylhyl.rslayout.internal;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by hupei on 2016/5/19.
 */
public final class FooterLayout extends FooterLayoutBase {
    public FooterLayout(Context context) {
        super(context);
    }

    @Override
    void createFooter(ViewGroup v) {
        v.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, pxTdp(100)));

        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmallInverse);
        v.addView(mProgressBar, new AbsListView.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));

        mTextView = new TextView(getContext());
        mTextView.setTextAppearance(getContext(), android.R.attr.textAppearanceMedium);
        v.addView(mTextView, new LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
    }
}
