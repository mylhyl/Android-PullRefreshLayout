package com.mylhyl.prlayout.app;

import android.widget.ListView;

import com.mylhyl.prlayout.SwipeRefreshListView;
import com.mylhyl.prlayout.internal.ISwipeRefresh;

public abstract class SwipeRefreshListFragment extends SwipeRefreshAbsListFragment<ListView> {

    @Override
    public ISwipeRefresh createSwipeRefreshLayout() {
        return new SwipeRefreshListView(getActivity());
    }
}
