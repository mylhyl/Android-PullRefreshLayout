package com.mylhyl.rslayout.extras;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mylhyl.rslayout.SwipeRefreshListView;


/**
 * Google自家下拉刷新 SwipeRefreshLayout <br>
 * 
 * 注：SwipeRefreshLayout 只能有一个 childView，childView自身必须是可滚动的view<br>
 * 或 childView 必须包含可滚动的view，如ScrollView或者ListView
 * 
 * @author hupei
 * @date 2015年7月31日 上午9:05:42
 */
public abstract class SwipeRefreshListFragment extends BaseSwipeRefreshFragment {

	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout rootView = new LinearLayout(getActivity());
		rootView.setOrientation(LinearLayout.VERTICAL);// 显示方式
		rootView.setLayoutParams(params);

		rootView.addView(getSwipeRefreshLayout(), params);

		// 构造 SwipeRefreshLayout 中的childVie布局，包含二个view：ListView 与 TextView
		final FrameLayout frameLayout = new FrameLayout(getActivity());
		getSwipeRefreshLayout().addView(frameLayout, params);

		// 添加可滑动的 View，可由子提供，此处默认为 ListView
		frameLayout.addView(getRefreshChildView(), params);

		frameLayout.addView(getEmptyView(), params);

		return rootView;
	}

	@Override
	public final SwipeRefreshListView createSwipeRefreshLayout() {
		return new SwipeRefreshListView(getActivity());
	}

	@Override
	public View createRefreshChildView() {
		ListView listView = new ListView(getActivity());
		listView.setId(android.R.id.list);
		return listView;
	}
}
