package com.mylhyl.prlayout.app;

import android.widget.ListView;

import com.mylhyl.prlayout.BaseSwipeRefresh;
import com.mylhyl.prlayout.SwipeRefreshListView;

public abstract class SwipeRefreshListFragment extends SwipeRefreshAbsListFragment<ListView> {

    @Override
    public BaseSwipeRefresh createSwipeRefreshLayout() {
        return new SwipeRefreshListView(getActivity());
    }
}
