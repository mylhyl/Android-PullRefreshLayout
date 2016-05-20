package com.mylhyl.rslayout.internal;

import android.view.View;

/**
 * Created by hupei on 2016/5/18.
 */
public interface ISwipeRefresh<T> {
    void setEmptyText(CharSequence text);

    void setEmptyText(int resId);

    void setEmptyView(View emptyView);

    View getEmptyView();

    void setRefreshing(boolean refreshing);

    void loadData();

    /**
     * 设置是否处于上拉加载状态
     *
     * @param loading 为true加载状态，false结束加载
     */
    void setLoading(boolean loading);

    /**
     * 允许上拉加载
     *
     * @param enabled
     */
    void setEnabledLoad(boolean enabled);

    /**
     * 是否在上拉加载中
     *
     * @return
     */
    boolean isLoading();

    /**
     * 是否允许上拉加载
     *
     * @return
     */
    boolean isEnabledLoad();

    /**
     * 获取可滑动的 View：ListView、ExpandableListView、GridView等
     *
     * @return
     */
    T getScrollView();

    IFooterLayout getFooterLayout();
}
