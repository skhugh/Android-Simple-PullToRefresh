/*
 * The MIT License (MIT)
 * Copyright (c) 2016. skhugh@gmail.com
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

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.skhugh.pulltorefresh.refreshicon.DefaultRefreshIcon;
import com.skhugh.pulltorefresh.refreshicon.RefreshIcon;

public interface PullToRefresh {
    /**
     * Sets pullToRefreshListener which implements {@link PullToRefreshListener}.
     * This is used to notify when the refresh task should be started.
     *
     * @param pullToRefreshListener interface to notify when the refresh task should be started
     */
    void setPullToRefreshListener(@NonNull PullToRefreshListener pullToRefreshListener);

    /**
     * Sets padding for the refresh layout.
     * Default value is 4dp.
     *
     * @param refreshLayoutPadding padding for refresh layout
     */
    void setRefreshLayoutPadding(int refreshLayoutPadding);

    /**
     * Sets the background color for the refresh layout.
     * Default value is Color.LTGRAY.
     *
     * @param refreshLayoutBackgroundColor background color for the refresh layout
     */
    void setRefreshLayoutBackgroundColor(int refreshLayoutBackgroundColor);

    /**
     * Sets the color for the refresh icon.
     * This value is ignored if refreshIconDrawable is set.
     * Default value is Color.DKGRAY.
     *
     * @param refreshIconColor color of the {@link DefaultRefreshIcon}
     */
    void setRefreshIconColor(int refreshIconColor);

    /**
     * Sets the size of the refresh icon.
     * Default value is 24dp.
     *
     * @param refreshIconSizeInPx size of the refresh icon in px
     */
    void setRefreshIconSize(int refreshIconSizeInPx);

    /**
     * Sets the duration of one full spin of the refresh icon.
     * Default value is 800ms.
     *
     * @param refreshIconSpinDuration duration of one full spin of the refresh icon in ms
     */
    void setRefreshIconSpinDuration(int refreshIconSpinDuration);

    /**
     * Sets the drawable for the refresh icon.
     *
     * @param refreshIconDrawable drawable to be used for the refresh icon
     */
    void setRefreshIconDrawable(@NonNull Drawable refreshIconDrawable);

    /**
     * Sets custom view for the refresh icon.
     *
     * @param refreshIcon object for custom refresh icon which implements {@link RefreshIcon}
     */
    void setRefreshIcon(@NonNull RefreshIcon refreshIcon);

    /**
     * Returns whether PullToRefreshLayout is refreshing.
     *
     * @return true if PullToRefreshLayout is currently refreshing
     */
    boolean isRefreshing();

    /**
     * Sets whether to block user-driven scroll event while refreshing.
     * Default value is true
     *
     * @param blockScrollWhileRefreshing true to block scroll
     */
    void setBlockScrollWhileRefreshing(boolean blockScrollWhileRefreshing);

    /**
     * Notifies {@link PullToRefreshLayout} when refresh is done.
     */
    void refreshDone();
}
