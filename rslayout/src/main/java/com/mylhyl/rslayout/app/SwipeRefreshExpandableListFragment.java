package com.mylhyl.rslayout.app;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.mylhyl.rslayout.BaseSwipeRefresh;
import com.mylhyl.rslayout.SwipeRefreshExpandableListView;
import com.mylhyl.rslayout.SwipeRefreshListView;

/**
 * Created by hupei on 2016/5/16.
 */
public abstract class SwipeRefreshExpandableListFragment extends SwipeRefreshListFragment {
    private final ExpandableListView.OnGroupClickListener mOnGroupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return onListGroupClick(parent, v, groupPosition, id);
        }
    };
    private final ExpandableListView.OnChildClickListener mOnChildClickListener = new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            return onListChildClick(parent, v, groupPosition, childPosition, id);
        }
    };

    public boolean onListChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    public boolean onListGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    @Override
    public final void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        //子类不允许重写
        super.onListItemClick(parent, view, position, id);
    }

    @Override
    public BaseSwipeRefresh createSwipeRefreshLayout() {
        return new SwipeRefreshExpandableListView(getActivity());
    }

    @Override
    public ExpandableListView createRefreshChildView() {
        ExpandableListView expandableListView = new ExpandableListView(getActivity());
        expandableListView.setId(android.R.id.list);
        return expandableListView;
    }

    public final void setListAdapter(ExpandableListAdapter adapter) {
        AbsListView refreshChildView = getRefreshChildView();
        BaseSwipeRefresh swipeRefreshLayout = getSwipeRefreshLayout();
        if (refreshChildView != null && refreshChildView instanceof ExpandableListView
                && swipeRefreshLayout != null && swipeRefreshLayout instanceof SwipeRefreshListView) {
            ExpandableListView expandableListView = (ExpandableListView) refreshChildView;
            expandableListView.setVisibility(View.VISIBLE);

            expandableListView.setOnGroupClickListener(mOnGroupClickListener);
            expandableListView.setOnChildClickListener(mOnChildClickListener);

            SwipeRefreshExpandableListView swipeRefreshExpandableListView = (SwipeRefreshExpandableListView) swipeRefreshLayout;
            swipeRefreshExpandableListView.setAdapter(adapter);
        }
    }
}
