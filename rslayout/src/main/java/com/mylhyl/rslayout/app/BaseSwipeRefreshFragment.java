/*
 * Copyright (c) 2014. hupei (hupei132@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mylhyl.rslayout.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mylhyl.rslayout.BaseSwipeRefresh;
import com.mylhyl.rslayout.internal.OnListLoadListener;
import com.mylhyl.rslayout.SwipeRefreshListView;

/**
 * Google自家下拉刷新 SwipeRefreshLayout <br>
 * <p/>
 * 注：SwipeRefreshLayout 只能有一个 childView，childView自身必须是可滚动的view<br>
 * 或 childView 必须包含可滚动的view，如ScrollView或者ListView<br>
 * 子类继承重写 onCreateView 必须 super
 *
 * @author hupei
 * @date 2015年7月31日 上午9:05:42
 */
abstract class BaseSwipeRefreshFragment<T extends View> extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, OnListLoadListener {

    /**
     * 创建下拉刷新控件 CygSwipeRefreshLayout 子类重写
     *
     * @return
     */
    public abstract BaseSwipeRefresh<T> createSwipeRefreshLayout();

    private BaseSwipeRefresh mSwipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSwipeRefresh = createSwipeRefreshLayout();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 注册下拉刷新
        mSwipeRefresh.setOnRefreshListener(this);
    }

    /**
     * 设置允许上拉加载，并注册监听器，重写{@link #onListLoad()}
     *
     * @param enabled
     * @see SwipeRefreshListView#setEnabledLoad(boolean)
     */
    public final void setEnabledLoad(boolean enabled) {
        mSwipeRefresh.setEnabledLoad(enabled);
        if (enabled)
            mSwipeRefresh.setOnListLoadListener(this);
    }

    /**
     * {@link BaseSwipeRefresh#setRefreshing(boolean)}
     */
    public final void setRefreshing(boolean refreshing) {
        getSwipeRefreshLayout().setRefreshing(refreshing);
    }

    /**
     * {@link BaseSwipeRefresh#setLoading(boolean)}
     */
    public final void setLoading(boolean loading) {
        getSwipeRefreshLayout().setLoading(loading);
    }

    public final void setEmptyText(int resId) {
        setEmptyText(getString(resId));
    }

    public final void setEmptyText(CharSequence text) {
        getSwipeRefreshLayout().setEmptyText(text);
    }

    public final void setEmptyView(View emptyView) {
        getSwipeRefreshLayout().setEmptyView(emptyView);
    }

    public final BaseSwipeRefresh<T> getSwipeRefreshLayout() {
        return mSwipeRefresh;
    }

    /**
     * Item 点击时此方法调用，子类可重写
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onListLoad() {

    }
}
