package com.mylhyl.rslayout.internal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by hupei on 2016/5/19.
 */
public abstract class FooterLayoutBase extends LinearLayout implements IFooterLayout {
    protected ProgressBar mProgressBar;
    protected TextView mTextView;

    abstract void createFooter(ViewGroup v);

    public FooterLayoutBase(Context context) {
        super(context);
        init();
        createFooter(this);
    }

    public FooterLayoutBase(Context context, View v) {
        super(context);
        init();
        createFooter((ViewGroup) v);
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    protected int pxTdp(int height) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (height / scale + 0.5f);
    }
    @Override
    public void setFooterHeight(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = pxTdp(height);
        setLayoutParams(params);
    }

    @Override
    public void setFooterText(CharSequence text) {
        if (mTextView != null)
            mTextView.setText(text);
    }

    @Override
    public void setFooterTextSize(float size) {
        if (mTextView != null)
            mTextView.setTextSize(size);
    }

    @Override
    public void setFooterColor(@ColorInt int color) {
        if (mTextView != null)
            mTextView.setTextColor(color);
    }

    @Override
    public void setFooterBackgroundResource(@DrawableRes int resId) {
        setBackgroundResource(resId);
    }

    @Override
    public void setFooterBackgroundColor(@ColorInt int color) {
        setBackgroundColor(color);
    }

    @Override
    public void setProgressBarVisibility(int v) {
        if (mProgressBar != null)
            mProgressBar.setVisibility(v);
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        if (mProgressBar != null)
            mProgressBar.setIndeterminateDrawable(d);
    }
}
