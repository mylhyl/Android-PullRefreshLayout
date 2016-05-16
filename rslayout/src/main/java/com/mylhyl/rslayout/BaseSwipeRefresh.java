package com.mylhyl.rslayout;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * android v4 兼容包 中 SwipeRefreshLayout 刷新控件，支持上拉加载
 * <p/>
 * <pre>
 * 实现原理是利用 {@link SwipeRefreshOnScrollListener#onScrollStateChanged(AbsListView, int) OnScrollListener} 事件控制
 * 此处主要封装以下几点：
 * 1.手动显示刷新动画 {@link #showRefreshHeader}
 * 2.支持上拉加载
 * 3.解决 SwipeRefreshLayout 与 可滑动控件{@link SwipeRefreshOnScrollListener#onScroll(AbsListView, int, int, int) 使用过程中冲突的问题}
 * </pre>
 * Created by hupei on 2016/5/12.
 */
public abstract class BaseSwipeRefresh<T extends View> extends SwipeRefreshLayout {
    /**
     * 创建上拉加载 view 子类重写
     *
     * @return
     */
    protected abstract View onCreateFooterView();

    /**
     * 添加上拉加载 View 子类重写
     */
    protected abstract void onAddFooterView();

    /**
     * 显示上拉加载 子类重写
     */
    protected abstract void showFooter();

    /**
     * 隐藏上拉加载 子类重写
     */
    protected abstract void hideFooter();

    private static final int[] COLOR_RES_IDS = new int[]{android.R.color.holo_blue_light, android.R.color.holo_red_light,
            android.R.color.holo_orange_light, android.R.color.holo_green_light};

    protected T mContentView;
    protected View mFooterView;

    private int[] mColorResIds = COLOR_RES_IDS;

    private boolean mLoading;// 是否处于上拉加载状态中
    private boolean mEnabledLoad;// 是否允许上拉加载
    private OnRefreshListener mOnRefreshListener;
    protected OnListLoadListener mOnListLoadListener;

    public BaseSwipeRefresh(Context context) {
        super(context);
    }

    public BaseSwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected final void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mContentView == null)
            getListView(this);
    }

    /**
     * 获取ListView对象
     */
    private void getListView(ViewGroup vg) {
        int childCount = vg.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = vg.getChildAt(i);
            if (childView instanceof FrameLayout) {
                getListView((ViewGroup) childView);
            } else if (childView instanceof AbsListView) {
                AbsListView absListView = (AbsListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                // 利用 ListView 的 OnScrollListener 滑动事件，解决 RefreshLayout 与 ListView 滑动冲突
                absListView.setOnScrollListener(new SwipeRefreshOnScrollListener(this));
                mContentView = (T) absListView;
                onAddFooterView();
                break;
            }
        }
    }

    /**
     * 显示下拉刷新
     *
     * @author hupei
     * @date 2015年10月30日 下午2:28:47
     */
    public final void showRefreshHeader() {
        showRefreshHeader(mColorResIds);
    }

    public final void showRefreshHeader(int... colorResIds) {
        showRefreshHeader(false, 0, 24, colorResIds);
    }

    public final void showRefreshHeader(boolean scale, int start, int end, int... colorResIds) {
        // 设置刷新时动画的颜色，可以设置4个
        setColorSchemeResources(colorResIds);
        // 首次加载显示下拉动画，关键在于源码中的 mUsingCustomStart = true
        setProgressViewOffset(scale, start, end);
        setRefreshing(true);
    }

    @Override
    public final void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
        super.setOnRefreshListener(listener);
    }

    /**
     * 注册上拉加载事件
     *
     * @param onListLoadListener
     * @author hupei
     * @date 2015年11月3日 上午10:36:59
     */
    public final void setOnListLoadListener(OnListLoadListener onListLoadListener) {
        this.mOnListLoadListener = onListLoadListener;
        setEnabledLoad(true);
        if (mFooterView == null)
            mFooterView = onCreateFooterView();//创建上拉加载 View

        if (mFooterView == null)
            throw new RuntimeException("call setOnListLoadListener after, abstract method onCreateFooterView cannot return null");
    }

    @Override
    public final void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        // 下拉刷新中，此处目的是为了手动调用 setRefreshing(true)时，响应下拉刷新事件
        if (isRefreshing() && mOnRefreshListener != null)
            mOnRefreshListener.onRefresh();
    }

    /**
     * 执行onLoad方法{@linkplain SwipeRefreshOnScrollListener#onScrollStateChanged(AbsListView, int) 使用}
     */
    private void loadData() {
        if (mOnListLoadListener != null) {
            setLoading(true);// 设置状态
            mOnListLoadListener.onListLoad();
        }
    }

    /**
     * 设置是否处于上拉加载状态
     *
     * @param loading 为true加载状态，false结束加载
     * @author hupei
     * @date 2015年7月31日 上午9:09:27
     */
    public final void setLoading(boolean loading) {
        this.mLoading = loading;
        if (mLoading) showFooter();
        else hideFooter();
    }

    /**
     * 允许上拉加载
     *
     * @param enabled
     * @author hupei
     * @date 2015年7月31日 下午4:03:28
     */
    public final void setEnabledLoad(boolean enabled) {
        mEnabledLoad = enabled;
    }

    /**
     * 是否在上拉加载中
     *
     * @return
     * @author hupei
     * @date 2015年11月3日 上午10:35:44
     */
    public final boolean isLoading() {
        return mLoading;
    }

    /**
     * 是否允许上拉加载
     *
     * @return
     * @author hupei
     * @date 2015年11月3日 上午10:35:28
     */
    public final boolean isEnabledLoad() {
        return mEnabledLoad;
    }

    /**
     * SwipeRefreshLayout结合ListView使用的时候有时候存在下拉冲突
     * 结果当第一个item长度超过一屏，明明还没有到达列表顶部，Scroll事件就被拦截，列表无法滚动，同时启动了刷新。
     *
     * @author hupei
     * @date 2015年7月30日 下午2:35:12
     */
    protected final class SwipeRefreshOnScrollListener implements OnScrollListener {
        private BaseSwipeRefresh mSwipeView;
        private OnScrollListener mOnScrollListener;

        public SwipeRefreshOnScrollListener(BaseSwipeRefresh swipeView) {
            mSwipeView = swipeView;
        }

        public SwipeRefreshOnScrollListener(BaseSwipeRefresh swipeView, OnScrollListener onScrollListener) {
            mSwipeView = swipeView;
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 只有在闲置状态情况下检查
            if (scrollState == SCROLL_STATE_IDLE) {
                // 如果满足，允许上拉加载、非加载状态中、最后一个显示的 item 与数据源的大小一样，则表示滑动入底部
                if (mSwipeView.isEnabledLoad() && !mSwipeView.isLoading()
                        && view.getLastVisiblePosition() == view.getCount() - 1) {
                    loadData();// 执行上拉加载数据
                }
            }
            if (null != mOnScrollListener)
                mOnScrollListener.onScrollStateChanged(view, scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            final View firstView = view.getChildAt(firstVisibleItem);
            // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部
            if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
                mSwipeView.setEnabled(true);
            } else mSwipeView.setEnabled(false);

            if (null != mOnScrollListener)
                mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
