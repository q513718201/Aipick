package com.hazz.aipick.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hazz.aipick.mvp.model.DepthItem;
import com.hazz.aipick.view.DepthItemView;

import java.util.List;


public class AskDepthListAdapter extends DepthListAdapter {


    public AskDepthListAdapter(Context context) {
        this(context, null);
    }

    public AskDepthListAdapter(Context context, List<DepthItem> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DepthItemView view = null;
        if (convertView == null) {
            view = new DepthItemView(getContext());
            view.setDefaultType(DepthItem.Type.ASK);
        } else {
            view = (DepthItemView) convertView;
        }
        if (getDataList().size() >= getCount()) {
            int adjustPosition = position + getDataList().size() - getCount();
            view.bindView(getDataList().get(adjustPosition));
        } else {
            int margin = getCount() - getDataList().size();
            if (position >= margin) {
                int adjustPosition = position - margin;
                view.bindView(getDataList().get(adjustPosition));
            } else {
                view.reset();
            }
        }
        return view;
    }
}
