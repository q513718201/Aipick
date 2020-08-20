package com.hazz.aipick.ui.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hazz.aipick.mvp.model.DepthItem;

import java.util.List;

public abstract class DepthListAdapter extends BaseAdapter {

    private List<DepthItem> depthItemList;
    private Context context;


    public DepthListAdapter(Context context) {
        this(context, null);
    }

    public DepthListAdapter(Context context, List<DepthItem> dataList) {
        this.context = context;
        this.depthItemList = dataList;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public Context getContext() {
        return context;
    }

    public List<DepthItem> getDataList() {
        return depthItemList;
    }

    public void replaceData(List<DepthItem> dataList) {
        this.depthItemList = dataList;
        notifyDataSetChanged();
    }


}
