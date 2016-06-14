package com.mylhyl.prlayout.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

import com.mylhyl.prlayout.SwipeRefreshAbsListView;
import com.mylhyl.prlayout.internal.ISwipeRefresh;
import com.mylhyl.prlayout.internal.OnListLoadListener;


/**
 * 如果开启{@linkplain BaseSwipeRefreshFragment#setEnabledLoad(boolean) 上拉加载}功能
 * 必须重写 {@linkplain OnListLoadListener#onListLoad() onListLoad()}方法<br>
 *
 * @author hupei
 * @date 2015年7月31日 上午9:05:42
 */
abstract class SwipeRefreshAbsListFragment<T extends AbsListView> extends BaseSwipeRefreshFragment<T> {
    private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /**
             * ListView or GridView 单点事件
             */
            onListItemClick(parent, view, position, id);
        }
    };

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return (View) getSwipeRefreshLayout();
    }

    /**
     * 适配器设置数据源
     *
     * @param adapter
     * @author hupei
     * @date 2015年7月31日 上午9:06:14
     */
    public final void setListAdapter(ListAdapter adapter) {
        AbsListView absListView = getSwipeRefreshLayout().getScrollView();
        if (absListView instanceof ExpandableListView) {
            new RuntimeException("please call SwipeRefreshExpandableListFragment.setListAdapter()");
        }
        ISwipeRefresh<T> swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null && swipeRefreshLayout instanceof SwipeRefreshAbsListView) {
            absListView.setVisibility(View.VISIBLE);
            absListView.setOnItemClickListener(mOnItemClickListener);

            SwipeRefreshAbsListView swipeRefreshAbsListView = (SwipeRefreshAbsListView) swipeRefreshLayout;
            swipeRefreshAbsListView.setAdapter(adapter);
        }
    }
}
