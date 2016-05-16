package com.mylhyl.swiperefreshLayout.sample.app;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mylhyl.rslayout.OnListLoadListener;
import com.mylhyl.rslayout.SwipeRefreshListView;
import com.mylhyl.swiperefreshLayout.sample.R;

import java.util.ArrayList;
import java.util.List;


public class ListViewXmlActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnListLoadListener {
    private SwipeRefreshListView swipeRefreshListView;
    private ArrayAdapter<String> adapter;
    private List<String> objects = new ArrayList<>();
    private int index;
    private int footerIndex = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list_view_xml);

        swipeRefreshListView = (SwipeRefreshListView) findViewById(R.id.swipeRefresh);
        ListView listView = (ListView) findViewById(R.id.listView);
        TextView textView = new TextView(this);
        textView.setText("我是新用户!");
        listView.addFooterView(textView);

        swipeRefreshListView.setOnListLoadListener(this);
        swipeRefreshListView.setOnRefreshListener(this);

        for (int i = 0; i < footerIndex; i++) {
            objects.add("数据 = " + i);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, objects);
        swipeRefreshListView.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        swipeRefreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                objects.add(0, "下拉 = " + (--index));
                adapter.notifyDataSetChanged();
                swipeRefreshListView.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onListLoad() {
        swipeRefreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = footerIndex + 5;
                for (int i = footerIndex; i < count; i++) {
                    objects.add("上拉 = " + i);
                }
                footerIndex = count;
                adapter.notifyDataSetChanged();
                swipeRefreshListView.setLoading(false);
            }
        }, 2000);
    }
}
