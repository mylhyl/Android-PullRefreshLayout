package com.mylhyl.rslayout.extras;

import android.widget.AbsListView;
import android.widget.ListView;

import com.mylhyl.rslayout.BaseSwipeRefresh;
import com.mylhyl.rslayout.SwipeRefreshListView;

public abstract class SwipeRefreshListFragment extends SwipeRefreshAbsListFragment<AbsListView> {

    @Override
    public BaseSwipeRefresh createSwipeRefreshLayout() {
        return new SwipeRefreshListView(getActivity());
    }

    @Override
    public AbsListView createRefreshChildView() {
        ListView listView = new ListView(getActivity());
        listView.setId(android.R.id.list);
        return listView;
    }
}
