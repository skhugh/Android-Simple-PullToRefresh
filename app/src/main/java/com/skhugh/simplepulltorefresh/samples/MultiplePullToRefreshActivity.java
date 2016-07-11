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

package com.skhugh.simplepulltorefresh.samples;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skhugh.simplepulltorefresh.PullToRefreshListener;
import com.skhugh.simplepulltorefresh.PullToRefreshLayout;
import com.skhugh.simplepulltorefresh.refreshicon.RefreshIcon;

public class MultiplePullToRefreshActivity extends AppCompatActivity implements PullToRefreshListener {
    private PullToRefreshLayout pullToRefreshLayout1;
    private PullToRefreshLayout pullToRefreshLayout2;
    private ImageView imageView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_pull_to_refresh);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pullToRefreshLayout1 = (PullToRefreshLayout) findViewById(R.id.pulltorefresh1);
        pullToRefreshLayout2 = (PullToRefreshLayout) findViewById(R.id.pulltorefresh2);
        imageView = (ImageView) findViewById(R.id.image_view);
        webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com");

        pullToRefreshLayout1.setRefreshIcon(new CustomRefreshIcon());

        setUpPullToRefreshLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpPullToRefreshLayout() {
        pullToRefreshLayout1.setPullToRefreshListener(this);
        pullToRefreshLayout2.setPullToRefreshListener(this);
    }

    @Override
    public void onStartRefresh(View view) {
        if (view == imageView) {
            new GetAndroidNImageTask().execute(imageView.getWidth(), imageView.getHeight());
        } else if (view == webView) {
            webView.loadUrl("https://www.bing.com");
            pullToRefreshLayout2.refreshDone();
        }
    }

    private class CustomRefreshIcon implements RefreshIcon {
        boolean isSpinning = false;
        TextView textView;

        CustomRefreshIcon() {
            textView = new TextView(MultiplePullToRefreshActivity.this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            textView.setBackgroundColor(Color.TRANSPARENT);
            textView.setLayoutParams(layoutParams);
        }

        @NonNull
        @Override
        public View getIconView() {
            return textView;
        }

        @Override
        public void setProgress(float progress) {
            isSpinning = false;
            textView.setText((int) (progress * 100) + "%");
        }

        @Override
        public void spin() {
            isSpinning = true;
            textView.setText("Loading...");
        }

        @Override
        public void setSpinSpeed(int spinSpeed) {
        }

        @Override
        public boolean isSpinning() {
            return isSpinning;
        }
    }

    private class GetAndroidNImageTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... imageViewSizes) {
            return decodeSampledBitmapFromResource(getResources(), R.drawable.androidn, imageViewSizes[0],
                    imageViewSizes[1]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
            pullToRefreshLayout1.refreshDone();
        }

        Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }
}
