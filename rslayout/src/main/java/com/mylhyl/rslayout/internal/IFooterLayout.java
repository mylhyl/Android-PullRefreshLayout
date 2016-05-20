package com.mylhyl.rslayout.internal;

import android.graphics.drawable.Drawable;

/**
 * Created by hupei on 2016/5/19.
 */
public interface IFooterLayout {
    void setFooterHeight(int height);

    void setFooterText(CharSequence label);

    void setFooterTextSize(float size);

    void setFooterColor(int color);

    void setFooterBackgroundResource(int resId);

    void setFooterBackgroundColor(int color);

    void setProgressBarVisibility(int v);

    void setIndeterminateDrawable(Drawable d);
}
