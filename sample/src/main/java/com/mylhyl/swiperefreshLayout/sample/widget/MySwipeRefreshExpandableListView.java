package com.mylhyl.swiperefreshLayout.sample.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.mylhyl.rslayout.SwipeRefreshExpandableListView;
import com.mylhyl.swiperefreshLayout.sample.R;

/**
 * Created by hupei on 2016/5/16.
 */
public class MySwipeRefreshExpandableListView extends SwipeRefreshExpandableListView {
    public MySwipeRefreshExpandableListView(Context context) {
        super(context);
    }

    public MySwipeRefreshExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View createFooter() {
        //自定义上拉加载
        return LayoutInflater.from(getContext()).inflate(R.layout.swipe_refresh_footer, null, true);
    }
}
