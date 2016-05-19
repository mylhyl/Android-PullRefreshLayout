package com.mylhyl.rslayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mylhyl.rslayout.internal.FooterLayoutConvert;
import com.mylhyl.rslayout.internal.FooterLayout;
import com.mylhyl.rslayout.internal.IFooterLayout;
import com.mylhyl.rslayout.internal.ISwipeRefresh;
import com.mylhyl.rslayout.internal.LoadSwipeRefresh;
import com.mylhyl.rslayout.internal.OnListLoadListener;

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
public abstract class BaseSwipeRefresh<T extends View> extends LinearLayout implements ISwipeRefresh {

    /**
     * 创建可滑动 View，子类实现
     *
     * @param context
     * @param attrs
     * @return
     */
    protected abstract T createScrollView(Context context, AttributeSet attrs);

    private boolean mLoading;// 是否处于上拉加载状态中
    private boolean mEnabledLoad;// 是否允许上拉加载
    private FrameLayout mRefreshLayoutWrapper;
    private LoadSwipeRefresh mLoadSwipeRefresh;
    private T mScrollView;
    private View mEmptyView;
    protected View mFooterView;

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    private OnListLoadListener mOnListLoadListener;

    public BaseSwipeRefresh(Context context) {
        this(context, null);
    }

    public BaseSwipeRefresh(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSwipeRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseSwipeRefresh(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        mLoadSwipeRefresh = new LoadSwipeRefresh(context, attrs);
        mLoadSwipeRefresh.setISwipeRefresh(this);

        mScrollView = createScrollView(context, attrs);
        mEmptyView = createEmptyView(context);
        addSwipeRefreshView(context, mLoadSwipeRefresh, mScrollView, mEmptyView);
    }

    private View createEmptyView(Context context) {
        TextView emptyView = new TextView(context);
        emptyView.setId(android.R.id.empty);
        emptyView.setTextAppearance(context, android.R.attr.textAppearanceSmall);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setVisibility(View.GONE);
        return emptyView;
    }

    private void addSwipeRefreshView(Context context, LoadSwipeRefresh loadSwipeRefresh, T scrollView, View emptyView) {
        mRefreshLayoutWrapper = new FrameLayout(context);
        mRefreshLayoutWrapper.addView(scrollView,
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mRefreshLayoutWrapper.addView(emptyView,
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        loadSwipeRefresh.addView(mRefreshLayoutWrapper, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        addView(loadSwipeRefresh, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
    }

    public final void showRefreshHeader() {
        mLoadSwipeRefresh.showRefreshHeader();
    }

    public final void showRefreshHeader(int... colorResIds) {
        mLoadSwipeRefresh.showRefreshHeader(colorResIds);
    }

    public final void showRefreshHeader(boolean scale, int start, int end, int... colorResIds) {
        mLoadSwipeRefresh.showRefreshHeader(scale, start, end, colorResIds);
    }

    @Override
    public final void setEmptyText(int resId) {
        setEmptyText(getContext().getString(resId));
    }

    @Override
    public final void setEmptyText(CharSequence text) {
        if (mEmptyView != null && mEmptyView instanceof TextView) {
            TextView textView = (TextView) mEmptyView;
            textView.setText(text);
        }
    }

    @Override
    public final void setEmptyView(View newEmptyView) {
        if (null != newEmptyView) {
            newEmptyView.setVisibility(GONE);
            mEmptyView = newEmptyView;
            newEmptyView.setClickable(true);

            ViewParent newEmptyViewParent = newEmptyView.getParent();
            if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
                ((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
            }
            FrameLayout.LayoutParams lp = convertEmptyViewLayoutParams(newEmptyView.getLayoutParams());
            if (null != lp) {
                mRefreshLayoutWrapper.addView(newEmptyView, lp);
            } else {
                mRefreshLayoutWrapper.addView(newEmptyView);
            }
        }
    }

    private static FrameLayout.LayoutParams convertEmptyViewLayoutParams(ViewGroup.LayoutParams lp) {
        FrameLayout.LayoutParams newLp = null;
        if (null != lp) {
            newLp = new FrameLayout.LayoutParams(lp);
            if (lp instanceof LinearLayout.LayoutParams)
                newLp.gravity = ((LinearLayout.LayoutParams) lp).gravity;
            else newLp.gravity = Gravity.CENTER;
        }
        return newLp;
    }

    @Override
    public final View getEmptyView() {
        return mEmptyView;
    }

    final void updateEmptyViewShown(boolean shown) {
        /*
         * ListView不能设置隐藏，否则下拉刷新有问题，因为 SwipeRefreshLayout 必须有滚动的view。
		 * mListView.setVisibility(!shown ? View.VISIBLE : View.GONE);
		 */
        if (mEmptyView != null)
            mEmptyView.setVisibility(shown ? View.VISIBLE : View.GONE);
    }

    public final void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mOnRefreshListener = listener;
        mLoadSwipeRefresh.setOnRefreshListener(listener);
    }

    /**
     * 注册上拉加载事件
     *
     * @param onListLoadListener
     */
    public final void setOnListLoadListener(OnListLoadListener onListLoadListener) {
        this.mOnListLoadListener = onListLoadListener;
        setEnabledLoad(true);
        addFooterView();
    }

    private void addFooterView() {
        if (mFooterView == null) {
            mFooterView = createFooter();//创建上拉加载 View
            if (mFooterView == null)
                throw new RuntimeException("call setOnListLoadListener after, abstract method onCreateFooterView cannot return null");
            boolean b = mFooterView instanceof IFooterLayout;
            if (!b) {
                mFooterView = new FooterLayoutConvert(getContext(), mFooterView);
            }
            addView(mFooterView);
            hideFooter();
        }
    }

    /**
     * 创建上拉加载 view 子类可重写
     *
     * @return
     */
    protected View createFooter() {
        FooterLayout footerLayout = new FooterLayout(getContext());
        footerLayout.setFooterText("加载数据中...");
        return footerLayout;
    }

    @Override
    public final void setRefreshing(boolean refreshing) {
        mLoadSwipeRefresh.setRefreshing(refreshing);
        // 下拉刷新中，此处目的是为了手动调用 setRefreshing(true)时，响应下拉刷新事件
        if (mLoadSwipeRefresh.isRefreshing() && mOnRefreshListener != null)
            mOnRefreshListener.onRefresh();
    }

    private void loadData() {
        if (mOnListLoadListener != null) {
            setLoading(true);// 设置状态
            mOnListLoadListener.onListLoad();
        }
    }

    @Override
    public final void setLoading(boolean loading) {
        this.mLoading = loading;
        if (mFooterView != null) {
            if (mLoading) showFooter();
            else hideFooter();
        }
    }

    /**
     * 显示上拉加载
     */
    private void showFooter() {
        mFooterView.setVisibility(VISIBLE);
    }

    /**
     * 隐藏上拉加载
     */
    private void hideFooter() {
        mFooterView.setVisibility(GONE);
    }

    @Override
    public final void setEnabledLoad(boolean enabled) {
        mEnabledLoad = enabled;
    }

    @Override
    public final boolean isLoading() {
        return mLoading;
    }

    @Override
    public final boolean isEnabledLoad() {
        return mEnabledLoad;
    }

    @Override
    public final T getScrollView() {
        return mScrollView;
    }

    @Override
    public IFooterLayout getFooterLayout() {
        addFooterView();
        if (mFooterView instanceof IFooterLayout)
            return (IFooterLayout) mFooterView;
        return null;
    }

    /**
     * SwipeRefreshLayout结合ListView使用的时候有时候存在下拉冲突
     * 现象：当第一个item长度超过一屏，明明还没有到达列表顶部，Scroll事件就被拦截，列表无法滚动，同时启动了刷新。
     */
    final class SwipeRefreshOnScrollListener implements AbsListView.OnScrollListener {
        private AbsListView.OnScrollListener mOnScrollListener;

        public SwipeRefreshOnScrollListener() {
        }

        public SwipeRefreshOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            /**
             正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
             回调顺序如下：
             SCROLL_STATE_TOUCH_SCROLL = 1     正在滚动
             SCROLL_STATE_FLING = 2            手指做了抛的动作（手指离开屏幕前，用力滑了一下）
             SCROLL_STATE_IDLE = 0             停止滚动
             */
            if (null != mOnScrollListener)
                mOnScrollListener.onScrollStateChanged(view, scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            /**
             滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
             firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
             visibleItemCount：当前能看见的列表项个数（小半个也算）
             totalItemCount：列表项共数
             */
            final View firstView = view.getChildAt(firstVisibleItem);
            // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部
            if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
                mLoadSwipeRefresh.setEnabled(true);
            } else {
                mLoadSwipeRefresh.setEnabled(false);
                if (isEnabledLoad() && !isLoading()
                        && view.getLastVisiblePosition() == (totalItemCount - 1)) {
                    loadData();// 滑动底部自动执行上拉加载
                }
            }

            if (null != mOnScrollListener)
                mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    final class SwipeRefreshRVOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
//            int lastVisiblePosition = 0;
//            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//            int totalItemCount = manager.getItemCount();
//            if (manager instanceof LinearLayoutManager) {
//                lastVisiblePosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
//            } else if (manager instanceof StaggeredGridLayoutManager) {
//                lastVisiblePosition =((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions();
//            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE) { // 只有在闲置状态情况下检查
                // 如果满足，允许上拉加载、非加载状态中、最后一个显示的 item 与数据源的大小一样，则表示滑动入底部
                if (isEnabledLoad() && !isLoading()
//                        && lastVisiblePosition == (totalItemCount - 1)) {
                        && isLastItemVisible(recyclerView)) {
                    loadData();// 滑动底部自动执行上拉加载
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
//            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//            int firstVisibleItem = 0;
//            if (manager instanceof LinearLayoutManager) {
//                firstVisibleItem = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
//            }
//            final View firstView = recyclerView.getChildAt(firstVisibleItem);
//            if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0))
            if (isFirstItemVisible(recyclerView))
                mLoadSwipeRefresh.setEnabled(true);
            else mLoadSwipeRefresh.setEnabled(false);
        }

        /**
         * 判断第一个条目是否完全可见
         *
         * @param recyclerView
         * @return
         */
        private boolean isFirstItemVisible(RecyclerView recyclerView) {
            final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
            // 如果未设置Adapter或者Adapter没有数据可以下拉刷新
            if (null == adapter || adapter.getItemCount() == 0) {
                return true;
            }
            // 第一个条目完全展示,可以刷新
            if (getFirstVisiblePosition(recyclerView) == 0) {
                return recyclerView.getChildAt(0).getTop() >= recyclerView.getTop();
            }
            return false;
        }

        /**
         * 获取第一个可见子View的位置下标
         *
         * @param recyclerView
         * @return
         */
        private int getFirstVisiblePosition(RecyclerView recyclerView) {
            View firstVisibleChild = recyclerView.getChildAt(0);
            return firstVisibleChild != null ?
                    recyclerView.getChildAdapterPosition(firstVisibleChild) : -1;
        }

        /**
         * 判断最后一个条目是否完全可见
         *
         * @param recyclerView
         * @return
         */
        private boolean isLastItemVisible(RecyclerView recyclerView) {
            final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
            // 如果未设置Adapter或者Adapter没有数据可以上拉刷新
            if (null == adapter || adapter.getItemCount() == 0) {
                return true;
            }
            // 最后一个条目View完全展示,可以刷新
            int lastVisiblePosition = getLastVisiblePosition(recyclerView);
            if (lastVisiblePosition >= recyclerView.getAdapter().getItemCount() - 1) {
                return recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom()
                        <= recyclerView.getBottom();
            }
            return false;
        }

        /**
         * 获取最后一个可见子View的位置下标
         *
         * @param recyclerView
         * @return
         */
        private int getLastVisiblePosition(RecyclerView recyclerView) {
            View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
            return lastVisibleChild != null ? recyclerView.getChildAdapterPosition(lastVisibleChild) : -1;
        }
    }
}
