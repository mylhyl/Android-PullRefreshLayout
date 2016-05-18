package com.mylhyl.rslayout.app;

import android.widget.ListView;

import com.mylhyl.rslayout.BaseSwipeRefresh;
import com.mylhyl.rslayout.SwipeRefreshListView;

public abstract class SwipeRefreshListFragment extends SwipeRefreshAbsListFragment<ListView> {

    @Override
    public BaseSwipeRefresh createSwipeRefreshLayout() {
        return new SwipeRefreshListView(getActivity());
    }
}
