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

package com.skhugh.pulltorefresh.refreshicon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.pnikosis.materialishprogress.ProgressWheel;

public class DefaultRefreshIcon implements RefreshIcon {
    private ProgressWheel refreshIcon;

    public DefaultRefreshIcon(@NonNull Context context, int refreshIconColor, int refreshIconSize,
            int refreshIconSpinDuration) {
        refreshIcon = new ProgressWheel(context);
        RelativeLayout.LayoutParams refreshIconLayoutParams = new RelativeLayout.LayoutParams(refreshIconSize,
                refreshIconSize);
        refreshIconLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        setSpinSpeed(refreshIconSpinDuration);
        refreshIcon.setLayoutParams(refreshIconLayoutParams);
        refreshIcon.setBarColor(refreshIconColor);
        refreshIcon.setCircleRadius(refreshIconSize);
        refreshIcon.setProgress(0);
    }

    public void setRefreshIconColor(int refreshIconColor) {
        refreshIcon.setBarColor(refreshIconColor);
    }

    public void setRefreshIconSize(int refreshIconSize) {
        refreshIcon.setCircleRadius(refreshIconSize);
    }

    @NonNull
    @Override
    public View getIconView() {
        return refreshIcon;
    }

    @Override
    public void setProgress(float progress) {
        if (refreshIcon.isSpinning()) {
            refreshIcon.setInstantProgress(progress);
        } else {
            refreshIcon.setProgress(progress);
        }
    }

    @Override
    public void spin() {
        refreshIcon.spin();
    }

    @Override
    public void setSpinSpeed(int spinSpeed) {
        refreshIcon.setSpinSpeed(1 / (spinSpeed / 1000f));
    }

    @Override
    public boolean isSpinning() {
        return refreshIcon.isSpinning();
    }
}
