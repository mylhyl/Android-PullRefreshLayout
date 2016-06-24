package com.mylhyl.prlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mylhyl.prlayout.internal.FooterLayout;
import com.mylhyl.prlayout.internal.FooterLayoutConvert;
import com.mylhyl.prlayout.internal.IFooterLayout;
import com.mylhyl.prlayout.internal.ISwipeRefresh;
import com.mylhyl.prlayout.internal.OnListLoadListener;

/**
 * android v4 兼容包 中 SwipeRefreshLayout 刷新控件，支持上拉加载
 * <p/>
 * <pre>
 * 此处主要封装以下几点：
 * 1.手动刷新 {@link #autoRefresh() autoRefresh}
 * 2.支持上拉加载，并可自定义
 * 3.解决 SwipeRefreshLayout 与可滑动控件使用过程中冲突的问题
 * </pre>
 * Created by hupei on 2016/5/12.
 */
abstract class BaseSwipeRefresh<T extends View> extends LinearLayout implements ISwipeRefresh {

    /**
     * 创建可滑动 View，子类实现
     *
     * @param context
     * @param attrs
     * @return
     */
    protected abstract T createScrollView(Context context, AttributeSet attrs);

    private static final String DEFAULT_FOOTER_TEXT = "加载数据中...";

    private boolean mLoading;// 是否处于上拉加载状态中
    private boolean mEnabledLoad;// 是否允许上拉加载
    private FrameLayout mRefreshLayoutWrapper;
    private LoadSwipeRefresh mLoadSwipeRefresh;
    private T mScrollView;
    private View mEmptyView;
    private View mFooterView;
    private int mFooterResource;
    private String mFooterText;
    private Drawable mFooterIndeterminateDrawable;

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
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseSwipeRefresh(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.prLayout, defStyleAttr, defStyleRes);
        if (a != null) {
            mFooterResource = a.getResourceId(R.styleable.prLayout_footer_layout, 0);
            mFooterText = a.getString(R.styleable.prLayout_footer_text);
            mFooterIndeterminateDrawable = a.getDrawable(R.styleable.prLayout_footer_indeterminate_drawable);

            a.recycle();
        }

        if (TextUtils.isEmpty(mFooterText))
            mFooterText = DEFAULT_FOOTER_TEXT;

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        mLoadSwipeRefresh = new LoadSwipeRefresh(context, attrs);

        mScrollView = createScrollView(context, attrs);
        if (mEmptyView == null)
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
        loadSwipeRefresh.setSwipeableChildren(scrollView, emptyView);
    }

    @Override
    public final void autoRefresh() {
        mLoadSwipeRefresh.autoRefresh();
        this.setRefreshing(true);
    }

    @Override
    public final void autoRefresh(boolean scale, int start, int end) {
        mLoadSwipeRefresh.autoRefresh(scale, start, end);
        this.setRefreshing(true);
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

    @Override
    public final void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mOnRefreshListener = listener;
        mLoadSwipeRefresh.setOnRefreshListener(listener);
    }

    @Override
    public final void setOnListLoadListener(OnListLoadListener onListLoadListener) {
        this.mOnListLoadListener = onListLoadListener;
        setEnabledLoad(mOnListLoadListener != null);
        addFooterView();
    }

    private void addFooterView() {
        if (mFooterView == null && mOnListLoadListener != null) {
            createFooter();//创建上拉加载 View
            if (mFooterView == null)
                throw new NullPointerException("method onCreateFooterView cannot return null");
            //如是自定义 FooterView 的，则转换
            boolean b = mFooterView instanceof IFooterLayout;
            if (!b) {
                mFooterView = new FooterLayoutConvert(getContext(), mFooterView);
            }
            addView(mFooterView);
            hideFooter();
        }
    }

    private void createFooter() {
        if ((mFooterResource = getFooterResource()) > 0) {
            mFooterView = LayoutInflater.from(getContext()).inflate(mFooterResource, this, false);
        } else {
            FooterLayout footerLayout = new FooterLayout(getContext());
            footerLayout.setFooterText(mFooterText);
            if (mFooterIndeterminateDrawable != null)
                footerLayout.setIndeterminateDrawable(mFooterIndeterminateDrawable);
            mFooterView = footerLayout;
        }
    }

    @Override
    public void setFooterResource(int resource) {
        mFooterResource = resource;
    }

    /**
     * 获取自定义上拉加载 layoutResource，子类可重写
     *
     * @return
     */
    protected int getFooterResource() {
        return mFooterResource;
    }

    @Override
    public final void setRefreshing(boolean refreshing) {
        mLoadSwipeRefresh.setRefreshing(refreshing);
        // 下拉刷新中，此处目的是为了手动调用 setRefreshing(true)时，响应下拉刷新事件
        if (mLoadSwipeRefresh.isRefreshing() && mOnRefreshListener != null)
            mOnRefreshListener.onRefresh();
    }

    @Override
    public void loadData() {
        if (mOnListLoadListener != null) {
            setLoading(true);// 设置状态
            mOnListLoadListener.onListLoad();
        }
    }

    @Override
    public final void setLoading(boolean loading) {
        this.mLoading = loading;
        if (mLoading) showFooter();
        else hideFooter();
    }

    private void showFooter() {
        if (mFooterView != null)
            mFooterView.setVisibility(VISIBLE);
    }

    private void hideFooter() {
        if (mFooterView != null)
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
        if (mFooterView == null)
            throw new NullPointerException("mFooterView is null please call after setOnListLoadListener");
        if (mFooterView instanceof IFooterLayout)
            return (IFooterLayout) mFooterView;
        throw new RuntimeException("mFooterView is no interface IFooterLayout");
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mLoadSwipeRefresh;
    }
}
