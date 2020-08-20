package com.hazz.aipick.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hazz.aipick.mvp.model.DepthItem;
import com.hazz.aipick.view.DepthItemView;

import java.util.List;

public class BidDepthListAdapter extends DepthListAdapter {


    public BidDepthListAdapter(Context context) {
        this(context, null);
    }

    public BidDepthListAdapter(Context context, List<DepthItem> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DepthItemView view = null;
        if (convertView == null) {
            view = new DepthItemView(getContext());
            view.setDefaultType(DepthItem.Type.BID);
        } else  {
            view = (DepthItemView) convertView;
        }
        if (position < getDataList().size()) {
            view.bindView(getDataList().get(position));
        } else {
            view.reset();
        }
        return view;
    }
}
