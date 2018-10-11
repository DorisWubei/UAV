package com.projects.wu.wateruav;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.projects.wu.wateruav.adapter.Adapter;

public class DatalistActivity  extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datalist);

        initView();//调用初始化控件方法
        setAdapter();//调用设置适配器方法
    }

    /**
     * 设置适配器方法
     */
    private void setAdapter() {
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        mRecyclerView.setAdapter(mAdapter = new Adapter(this));
        //设置列表中子项的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //获取列表控件
        mRecyclerView = findViewById(R.id.recyclerView);
    }
}
