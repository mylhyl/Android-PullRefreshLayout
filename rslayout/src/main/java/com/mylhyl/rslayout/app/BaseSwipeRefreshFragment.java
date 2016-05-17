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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mylhyl.rslayout.BaseSwipeRefresh;
import com.mylhyl.rslayout.OnListLoadListener;
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
     * @author hupei
     * @date 2015年11月3日 下午4:27:52
     */
    public abstract BaseSwipeRefresh createSwipeRefreshLayout();

    /**
     * 创建 SwipeRefreshLayout 中包含的子控件，必须是可滑动的。子类重写
     *
     * @return
     * @author hupei
     * @date 2015年10月30日 下午1:48:43
     */
    public abstract T createRefreshChildView();

    private T mRefreshChildView;// SwipeRefreshLayout 的子 view
    private BaseSwipeRefresh mSwipeRefresh;
    private View mEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSwipeRefresh = createSwipeRefreshLayout();
        mRefreshChildView = createRefreshChildView();
        mEmptyView = createEmptyView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 注册下拉刷新
        mSwipeRefresh.setOnRefreshListener(this);
    }

    /**
     * 创建空数据显示view
     *
     * @return
     * @author hupei
     * @date 2015年11月3日 下午4:28:43
     */
    protected View createEmptyView() {
        TextView emptyView = new TextView(getActivity());
        emptyView.setId(android.R.id.empty);
        emptyView.setTextAppearance(getActivity(), android.R.attr.textAppearanceSmall);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setVisibility(View.GONE);
        return emptyView;
    }

    /**
     * 设置允许上拉加载，并注册监听器，重写{@link #onListLoad()}
     *
     * @param enabled
     * @author hupei
     * @date 2015年11月3日 上午9:43:34
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

    /**
     * @param resId
     * @author hupei
     * @date 2015年11月3日 上午11:16:28
     * @see #setEmptyText(CharSequence)
     */
    public final void setEmptyText(int resId) {
        setEmptyText(getString(resId));
    }

    /**
     * 设置ListView 空数据时 TextView 显示信息
     *
     * @param text
     * @author hupei
     * @date 2015年7月31日 上午9:06:57
     */
    public final void setEmptyText(CharSequence text) {
        if (mEmptyView instanceof TextView) {
            TextView textView = (TextView) mEmptyView;
            textView.setText(text);
        }
        setEmptyViewShown(true);
    }

    /**
     * 设置是否显示空数据提示语
     *
     * @param shown
     * @author hupei
     * @date 2015年7月31日 上午9:07:25
     */
    public final void setEmptyViewShown(boolean shown) {
        if (mRefreshChildView instanceof ExpandableListView) {
            ExpandableListAdapter adapter = ((ExpandableListView) mRefreshChildView).getExpandableListAdapter();
            if (adapter == null || adapter.getGroupCount() == 0) return;
        } else if (mRefreshChildView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) mRefreshChildView;
            ListAdapter adapter = absListView.getAdapter();
            if (adapter == null || adapter.getCount() == 0) return;
        }
        /*
         * ListView不能设置隐藏，否则下拉刷新有问题，因为 SwipeRefreshLayout 必须有滚动的view。
		 * mListView.setVisibility(!shown ? View.VISIBLE : View.GONE);
		 */
        mEmptyView.setVisibility(shown ? View.VISIBLE : View.GONE);
    }

    public final BaseSwipeRefresh getSwipeRefreshLayout() {
        return mSwipeRefresh;
    }

    protected final T getRefreshChildView() {
        return mRefreshChildView;
    }

    protected final View getEmptyView() {
        return mEmptyView;
    }

    /**
     * Item 点击时此方法调用，子类可重写
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @author hupei
     * @date 2015年7月31日 上午9:08:08
     */
    public void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onListLoad() {

    }
}
