package com.hazz.aipick.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hazz.aipick.R;

import java.io.File;
import java.util.ArrayList;


public class TraderCoinAddAdapter extends RecyclerView.Adapter<TraderCoinAddAdapter.PhotoViewHolder> {

    private ArrayList<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;

    private Context mContext;
    View itemView = null;
    public final static int TYPE_ADD = 1;
    public final static int TYPE_PHOTO = 2;

    final static int MAX = 10;

    public TraderCoinAddAdapter(Context mContext, ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_ADD:
                itemView = View.inflate(mContext, R.layout.coin_item_empty, null);
                break;
            case TYPE_PHOTO:
                itemView = inflater.inflate(R.layout.coin_item, parent, false);
                break;
        }
        return new PhotoViewHolder(itemView);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        if (getItemViewType(position) == TYPE_PHOTO) {

            holder.ivPhoto.setText(photoPaths.get(position));
            holder.ivdelete.setOnTouchListener((v, event) -> {
                photoPaths.remove(position);
                notifyDataSetChanged();
                return false;
            });


        } else {

            holder.itemView.setOnClickListener(v -> {

                if (onMyClick != null) {
                    onMyClick.onClick(position);
                }
            });

        }
    }

    public onMyClick onMyClick;

    public interface onMyClick {
        void onClick(int position);
    }

    public void setOnMyClick(onMyClick onMyClick) {
        this.onMyClick = onMyClick;
    }

    @Override
    public int getItemCount() {
        int count = photoPaths.size() + 1;
        if (count > MAX) {
            count = MAX;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photoPaths.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private TextView ivPhoto;
        private ImageView ivdelete;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (TextView) itemView.findViewById(R.id.tv_name);
            ivdelete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }
}
