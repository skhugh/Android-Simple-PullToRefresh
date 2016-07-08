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

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skhugh.pulltorefresh.PullToRefreshListener;
import com.skhugh.pulltorefresh.PullToRefreshLayout;

public class RecyclerActivity extends AppCompatActivity implements PullToRefreshListener {
    private PullToRefreshLayout pullToRefreshLayout;
    private DummyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new DummyAdapter(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setUpPullToRefreshLayout();
    }

    private void setUpPullToRefreshLayout() {
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pulltorefresh);
        pullToRefreshLayout.setPullToRefreshListener(this);
        pullToRefreshLayout.setRefreshLayoutBackgroundColor(Color.WHITE);
    }

    @Override
    public void onStartRefresh(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.refreshDone();
                adapter.refresh();
            }
        };
        final Handler handler = new Handler();
        handler.postDelayed(runnable, 700);
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

    private class DummyAdapter extends RecyclerView.Adapter<DummyViewHolder> {
        private Context context;
        private String itemPrefix = "Old";

        private DummyAdapter(Context context) {
            super();
            this.context = context.getApplicationContext();
        }

        @Override
        public DummyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.dummy_recycler_item, parent, false);
            return new DummyViewHolder(view, (TextView) view.findViewById(R.id.text_view));
        }

        @Override
        public void onBindViewHolder(DummyViewHolder holder, int position) {
            holder.textView.setText(itemPrefix + " item " + (position + 1));
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        void refresh() {
            itemPrefix = "New";
            notifyDataSetChanged();
        }
    }

    class DummyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        DummyViewHolder(View itemView, TextView textView) {
            super(itemView);
            this.textView = textView;
        }
    }
}
