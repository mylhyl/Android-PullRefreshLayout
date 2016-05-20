package com.mylhyl.swiperefreshLayout.sample.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mylhyl.rslayout.SwipeRefreshGridView;
import com.mylhyl.swiperefreshLayout.sample.R;

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
    protected View createFooter(ViewGroup root) {
        //自定义上拉加载，第二个参数必须为 root ，第三个参数必须为 false
        return LayoutInflater.from(getContext()).inflate(R.layout.swipe_refresh_footer, root, false);
    }
}
