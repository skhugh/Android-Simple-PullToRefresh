/*
 * The MIT License (MIT)
 * Copyright (c) 2016. Hyowoo Kim
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.skhugh.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.skhugh.pulltorefresh.layoutanimation.ChildViewAnimation;
import com.skhugh.pulltorefresh.layoutanimation.RefreshLayoutAnimation;
import com.skhugh.pulltorefresh.refreshicon.DefaultRefreshIcon;
import com.skhugh.pulltorefresh.refreshicon.RefreshIcon;
import com.skhugh.pulltorefresh.refreshicon.RefreshIconFactory;

import java.lang.ref.WeakReference;

public class PullToRefreshLayout extends FrameLayout implements ChildViewTopMarginCalculator, PullToRefresh {
    private static final String TAG = "PullToRefreshLayout";

    private static final int DEFAULT_REFRESH_ICON_SPIN_DURATION = 800;
    private static final int DEFAULT_REFRESH_LAYOUT_MAX_HEIGHT = 500;
    private static final int DEFAULT_REFRESH_LAYOUT_THRESHOLD_HEIGHT = 300;
    private static final int DEFAULT_REFRESH_LAYOUT_BACKGROUND_COLOR = Color.LTGRAY;
    private static final int DEFAULT_REFRESH_ICON_COLOR = Color.DKGRAY;
    private static final double SCROLL_GRAVITY = 1.5;

    private RelativeLayout refreshLayout;
    private RefreshIcon refreshIcon;
    private PullToRefreshListener pullToRefreshListener;
    private WeakReference<View> childViewWeakRef;
    private boolean refreshing = false;
    private int[] initialChildViewPosition;
    private int initialChildViewMarginTop;
    private boolean blockScrollWhileRefreshing = true;

    private Drawable refreshIconDrawable;
    private int refreshIconSpinDuration = DEFAULT_REFRESH_ICON_SPIN_DURATION;
    private int refreshIconColor = DEFAULT_REFRESH_ICON_COLOR;
    private int refreshIconSize = getResources().getDimensionPixelOffset(R.dimen.refresh_icon_size);

    private int refreshLayoutBackgroundColor = DEFAULT_REFRESH_LAYOUT_BACKGROUND_COLOR;
    private int refreshLayoutPadding = getResources().getDimensionPixelSize(R.dimen.refresh_layout_padding);
    private int refreshLayoutMaxHeight = DEFAULT_REFRESH_LAYOUT_MAX_HEIGHT;
    private int refreshLayoutThresholdHeight = DEFAULT_REFRESH_LAYOUT_THRESHOLD_HEIGHT;

    private OnTouchListener childViewOnTouchListener = new ChildViewOnTouchListener();

    public PullToRefreshLayout(Context context) {
        super(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initializeAttributes(attrs);
            initialize();
        }
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initializeAttributes(attrs);
            initialize();
        }
    }

    private void initializeAttributes(AttributeSet attrs) {
        TypedArray typedAttributes = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PullToRefreshLayout,
                0, 0);

        try {
            blockScrollWhileRefreshing = typedAttributes.getBoolean(R.styleable.PullToRefreshLayout_blockScrollWhileRefreshing, true);

            refreshIconDrawable = typedAttributes.getDrawable(R.styleable.PullToRefreshLayout_refreshIconDrawable);
            refreshIconSpinDuration = typedAttributes
                    .getInteger(R.styleable.PullToRefreshLayout_refreshIconSpinDuration,
                            DEFAULT_REFRESH_ICON_SPIN_DURATION);
            refreshIconColor = typedAttributes
                    .getColor(R.styleable.PullToRefreshLayout_refreshIconColor, DEFAULT_REFRESH_ICON_COLOR);
            refreshIconSize = typedAttributes.getDimensionPixelSize(R.styleable.PullToRefreshLayout_refreshIconSize,
                    getResources().getDimensionPixelOffset(R.dimen.refresh_icon_size));

            refreshLayoutBackgroundColor = typedAttributes
                    .getColor(R.styleable.PullToRefreshLayout_refreshLayoutBackgroundColor,
                            DEFAULT_REFRESH_LAYOUT_BACKGROUND_COLOR);
            refreshLayoutPadding = typedAttributes
                    .getDimensionPixelSize(R.styleable.PullToRefreshLayout_refreshLayoutPadding,
                            getResources().getDimensionPixelSize(R.dimen.refresh_layout_padding));
            refreshLayoutMaxHeight = typedAttributes
                    .getDimensionPixelOffset(R.styleable.PullToRefreshLayout_refreshLayoutMaxHeight,
                            DEFAULT_REFRESH_LAYOUT_MAX_HEIGHT);
            refreshLayoutThresholdHeight = typedAttributes
                    .getDimensionPixelOffset(R.styleable.PullToRefreshLayout_refreshLayoutThresholdHeight,
                            DEFAULT_REFRESH_LAYOUT_THRESHOLD_HEIGHT);
        } finally {
            typedAttributes.recycle();
        }

        initialize();
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);

        if (getChildCount() == 1 || (getChildCount() == 2 && getChildAt(0) == refreshLayout)) {
            if (view != refreshLayout) {
                setUpChildView(view);
            }
        } else {
            Log.d(TAG,
                    "You are adding more than one child view. PullToRefreshLayout works best with one child view...");
        }
    }

    private void setUpChildView(final View childView) {
        if (childViewWeakRef != null && childViewWeakRef.get() != null) {
            childViewWeakRef.get().setOnTouchListener(null);
        }

        childViewWeakRef = new WeakReference<>(childView);
        childView.setClickable(true);
        childView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (initialChildViewPosition == null) {
                    saveInitialStateOfChildView();
                }
            }
        });
        childView.setOnTouchListener(childViewOnTouchListener);
    }

    private void initialize() {
        createRefreshLayout();
        createRefreshIcon();
    }

    private void removeRefreshIcon() {
        if (refreshIcon != null)
            removeView(refreshIcon.getIconView());
    }

    private void createRefreshLayout() {
        removeView(refreshLayout);
        refreshLayout = new RelativeLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        refreshLayout.setLayoutParams(layoutParams);
        refreshLayout
                .setPadding(refreshLayoutPadding, refreshLayoutPadding, refreshLayoutPadding, refreshLayoutPadding);
        refreshLayout.setBackgroundColor(refreshLayoutBackgroundColor);
        addView(refreshLayout, 0);
    }

    private void createRefreshIcon() {
        removeRefreshIcon();
        refreshIcon = RefreshIconFactory
                .createRefreshIcon(getContext(), refreshIconColor, refreshIconSize, refreshIconSpinDuration,
                        refreshIconDrawable);
        refreshLayout.addView(refreshIcon.getIconView());
    }

    @Override
    public void setPullToRefreshListener(@NonNull PullToRefreshListener pullToRefreshListener) {
        refreshDone();
        this.pullToRefreshListener = pullToRefreshListener;
    }

    @Override
    public void setRefreshLayoutPadding(int refreshLayoutPadding) {
        this.refreshLayoutPadding = refreshLayoutPadding;
        if (refreshLayout != null) {
            refreshLayout
                    .setPadding(refreshLayoutPadding, refreshLayoutPadding, refreshLayoutPadding, refreshLayoutPadding);
        }
    }

    @Override
    public void setRefreshLayoutBackgroundColor(int refreshLayoutBackgroundColor) {
        this.refreshLayoutBackgroundColor = refreshLayoutBackgroundColor;
        if (refreshLayout != null) {
            refreshLayout.setBackgroundColor(refreshLayoutBackgroundColor);
        }
    }

    @Override
    public void setRefreshIconColor(int refreshIconColor) {
        this.refreshIconColor = refreshIconColor;
        if (refreshIcon != null && refreshIcon instanceof DefaultRefreshIcon) {
            ((DefaultRefreshIcon) refreshIcon).setRefreshIconColor(refreshIconColor);
        }
    }

    @Override
    public void setRefreshIconSize(int refreshIconSizeInPx) {
        refreshIconSize = refreshIconSizeInPx;
        if (refreshIcon != null && refreshIcon instanceof DefaultRefreshIcon) {
            ((DefaultRefreshIcon) refreshIcon).setRefreshIconSize(refreshIconSize);
        }
    }

    @Override
    public void setRefreshIconSpinDuration(int refreshIconSpinDuration) {
        this.refreshIconSpinDuration = refreshIconSpinDuration;
        if (refreshIcon != null)
            refreshIcon.setSpinSpeed(refreshIconSpinDuration);
    }

    @Override
    public void setRefreshIconDrawable(@NonNull Drawable refreshIconDrawable) {
        this.refreshIconDrawable = refreshIconDrawable;
        createRefreshIcon();
    }

    @Override
    public void setRefreshIcon(@NonNull RefreshIcon refreshIcon) {
        removeRefreshIcon();
        this.refreshIcon = refreshIcon;
        refreshLayout.addView(refreshIcon.getIconView());
    }

    @Override
    public boolean isRefreshing() {
        return refreshing;
    }

    @Override
    public void setBlockScrollWhileRefreshing(boolean blockScrollWhileRefreshing) {
        this.blockScrollWhileRefreshing = blockScrollWhileRefreshing;
    }

    @Override
    public void refreshDone() {
        if (refreshing) {
            animateRefreshLayout(0);
            refreshing = false;
        }
    }

    private void saveInitialStateOfChildView() {
        try {
            initialChildViewPosition = new int[2];
            childViewWeakRef.get().getLocationOnScreen(initialChildViewPosition);
            initialChildViewMarginTop = getChildViewMarginLayoutParams().topMargin;
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private MarginLayoutParams getChildViewMarginLayoutParams() {
        try {
            return (MarginLayoutParams) childViewWeakRef.get().getLayoutParams();
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage());
            return new MarginLayoutParams(getContext(), null);
        }
    }

    private void animateRefreshLayout(final int refreshLayoutHeight) {
        try {
            childViewWeakRef.get().startAnimation(
                    new ChildViewAnimation(PullToRefreshLayout.this, childViewWeakRef.get(), refreshLayoutHeight));
            refreshLayout.startAnimation(
                    new RefreshLayoutAnimation(PullToRefreshLayout.this, refreshLayout, refreshLayoutHeight));
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public int calculateNewTopMarginAtInterpolatedTime(int refreshLayoutHeight, float interpolatedTime) {
        final int currentTopMargin = getChildViewMarginLayoutParams().topMargin;
        return (int) (currentTopMargin - (currentTopMargin - (refreshLayoutHeight + initialChildViewMarginTop)) * interpolatedTime);
    }

    @Override
    public int getInitialTopMargin() {
        return initialChildViewMarginTop;
    }


    private class ChildViewOnTouchListener implements OnTouchListener {
        private boolean isDown = false;
        private float prevY = 0;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (refreshing)
                return blockScrollWhileRefreshing;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                isDown = true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (isDown) {
                    startRefreshingOrRestoreToInitialState();
                    isDown = false;
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && isDown) {
                if (view.getScrollY() == 0 && !view.canScrollVertically(-1)) {
                    int[] location = new int[2];
                    view.getLocationInWindow(location);

                    if (isRefreshLayoutInInitialState(location)) {
                        moveRefreshLayout(view, motionEvent.getRawY());
                        prevY = motionEvent.getRawY();

                        return shouldNotPassTouchEventToParent();
                    }
                }
            }
            prevY = motionEvent.getRawY();

            return false;
        }

        private void startRefreshingOrRestoreToInitialState() {
            if (refreshLayout.getHeight() >= refreshLayoutThresholdHeight) {
                animateRefreshLayout(refreshLayoutThresholdHeight);
                refreshing = true;

                if (pullToRefreshListener != null) {
                    try {
                        pullToRefreshListener.onStartRefresh(childViewWeakRef.get());
                    } catch (NullPointerException e) {
                        pullToRefreshListener.onStartRefresh(null);
                    }
                }
            } else {
                animateRefreshLayout(0);
            }
        }

        @org.jetbrains.annotations.Contract(pure = true)
        private boolean isRefreshLayoutInInitialState(int[] location) {
            return location[1] + initialChildViewMarginTop == initialChildViewPosition[1] + getChildViewMarginLayoutParams().topMargin;
        }

        private void moveRefreshLayout(View view, float motionEventRawY) {
            int newTopMargin = calculateNewTopMargin(motionEventRawY);
            MarginLayoutParams layoutParams = getChildViewMarginLayoutParams();
            layoutParams.topMargin = newTopMargin;
            view.setLayoutParams(layoutParams);

            refreshLayout.getLayoutParams().height = calculateNewRefreshLayoutHeight();
            spinOrSetProgressOfRefreshIcon();
        }

        private void spinOrSetProgressOfRefreshIcon() {
            if (getChildViewMarginLayoutParams().topMargin >= refreshLayoutThresholdHeight) {
                if (!refreshIcon.isSpinning()) {
                    refreshIcon.spin();
                }
            } else {
                refreshIcon.setProgress((float) calculateNewRefreshLayoutHeight() / refreshLayoutThresholdHeight);
            }
        }

        private int calculateNewTopMargin(float motionEventRawY) {
            int maxTopMargin = refreshLayoutMaxHeight + initialChildViewMarginTop;
            int newTopMargin = (int) Math.min(maxTopMargin,
                    Math.max(getChildViewMarginLayoutParams().topMargin + motionEventRawY - prevY,
                            initialChildViewMarginTop));
            if (newTopMargin > refreshLayoutThresholdHeight && newTopMargin <= maxTopMargin) {
                newTopMargin -= (motionEventRawY - prevY) / SCROLL_GRAVITY;
            }
            return newTopMargin;
        }

        private int calculateNewRefreshLayoutHeight() {
            return getChildViewMarginLayoutParams().topMargin - initialChildViewMarginTop;
        }

        private boolean shouldNotPassTouchEventToParent() {
            return getChildViewMarginLayoutParams().topMargin != initialChildViewMarginTop;
        }
    }
}
