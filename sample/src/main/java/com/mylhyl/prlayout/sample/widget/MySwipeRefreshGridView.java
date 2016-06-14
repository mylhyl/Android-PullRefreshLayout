package com.mylhyl.prlayout.sample.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mylhyl.prlayout.SwipeRefreshGridView;
import com.mylhyl.prlayout.sample.R;

/**
 * Created by hupei on 2016/5/16.
 */
public class MySwipeRefreshGridView extends SwipeRefreshGridView {
    public MySwipeRefreshGridView(Context context) {
        super(context);
    }

    public MySwipeRefreshGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getFooterResource() {
        return R.layout.swipe_refresh_footer;
    }
}
