package com.mylhyl.rslayout.internal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by hupei on 2016/5/19.
 */
public final class FooterLayoutConvert extends FooterLayoutBase implements IFooterLayout {

    public FooterLayoutConvert(Context context, View v) {
        super(context, v);
    }

    @Override
    void createFooter(ViewGroup v) {
        int count = v.getChildCount();
        for (int i = 0; i < count; i++) {
            View childAt = v.getChildAt(i);
            if (childAt instanceof ProgressBar)
                mProgressBar = (ProgressBar) childAt;
            else if (childAt instanceof TextView)
                mTextView = (TextView) childAt;
        }
        addView(v, getLayoutParams());
    }
}
