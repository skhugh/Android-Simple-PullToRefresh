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
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DrawableRefreshIcon implements RefreshIcon {
    private ImageView imageView;
    private boolean isSpinning = false;
    private int refreshIconSpinDuration;

    public DrawableRefreshIcon(@NonNull Context context, @NonNull Drawable refreshIconDrawable, int refreshIconSize,
            int refreshIconSpinDuration) {
        imageView = new ImageView(context);
        RelativeLayout.LayoutParams progressBarLayoutParams = new RelativeLayout.LayoutParams(refreshIconSize,
                refreshIconSize);
        progressBarLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(progressBarLayoutParams);
        imageView.setImageDrawable(refreshIconDrawable);
        this.refreshIconSpinDuration = refreshIconSpinDuration;
    }

    @NonNull
    @Override
    public View getIconView() {
        return imageView;
    }

    @Override
    public void setProgress(float progress) {
        isSpinning = false;
        imageView.clearAnimation();
        imageView.setRotation(progress * 360);
    }

    @Override
    public void spin() {
        isSpinning = true;
        imageView.clearAnimation();
        imageView.startAnimation(createSpinningAnimation());
    }

    @Override
    public void setSpinSpeed(int spinSpeed) {
        refreshIconSpinDuration = spinSpeed;
    }

    private Animation createSpinningAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(imageView.getRotation(), imageView.getRotation() + 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setDuration(refreshIconSpinDuration);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        return rotateAnimation;
    }

    @Override
    public boolean isSpinning() {
        return isSpinning;
    }
}
