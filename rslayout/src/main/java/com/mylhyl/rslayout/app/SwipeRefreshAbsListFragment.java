package com.mylhyl.rslayout.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.mylhyl.rslayout.BaseSwipeRefresh;
import com.mylhyl.rslayout.OnListLoadListener;
import com.mylhyl.rslayout.SwipeRefreshListView;


/**
 * 如果开启{@linkplain BaseSwipeRefreshFragment#setEnabledLoad(boolean) 上拉加载}功能
 * 必须重写 {@linkplain OnListLoadListener#onListLoad() onListLoad()}方法<br>
 * 须自定义 ListView 数据为空时的 View ，可重写 {@linkplain BaseSwipeRefreshFragment#createEmptyView() createEmptyView()}主法实现
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
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        BaseSwipeRefresh swipeRefreshLayout = getSwipeRefreshLayout();

        final FrameLayout frameLayout = new FrameLayout(getActivity());
        swipeRefreshLayout.addView(frameLayout, params);

        frameLayout.addView(getRefreshChildView(), params);
        frameLayout.addView(swipeRefreshLayout.getEmptyView(), params);

        return swipeRefreshLayout;
    }

    /**
     * 适配器设置数据源
     *
     * @param adapter
     * @author hupei
     * @date 2015年7月31日 上午9:06:14
     */
    public final void setListAdapter(ListAdapter adapter) {
        AbsListView absListView = getRefreshChildView();
        if (absListView instanceof ExpandableListView) {
            new RuntimeException("please call SwipeRefreshExpandableListFragment.setListAdapter()");
        }
        BaseSwipeRefresh swipeRefreshLayout = getSwipeRefreshLayout();
        if (absListView != null
                && swipeRefreshLayout != null && swipeRefreshLayout instanceof SwipeRefreshListView) {
            absListView.setVisibility(View.VISIBLE);
            absListView.setOnItemClickListener(mOnItemClickListener);

            SwipeRefreshListView swipeRefreshListView = (SwipeRefreshListView) swipeRefreshLayout;
            swipeRefreshListView.setAdapter(adapter);
        }
    }
}
