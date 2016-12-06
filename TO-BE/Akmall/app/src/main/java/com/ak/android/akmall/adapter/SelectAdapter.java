package com.ak.android.akmall.adapter;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.ShopContentActivity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gkdud on 2016-11-02.
 * 셀렡트 다얄로그에 들어갈 아답터
 */
public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
    Context context;
    List<String> list = new ArrayList<>();
    Dialog dialog;
    String call;
    List<String> value;
    int idx;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView SELECT_TEXT;
        RelativeLayout SELECT_LAYOUT;

        public ViewHolder(View view) {
            super(view);
            SELECT_TEXT = (TextView)view.findViewById(R.id.SELECT_TEXT);
            SELECT_LAYOUT = (RelativeLayout)view.findViewById(R.id.SELECT_LAYOUT);
        }
    }

    public SelectAdapter(Context context, List<String> list, Dialog dialog, String call, List<String> map,int idx) {
        this.context = context;
        this.list = list;
        this.dialog = dialog;
        this.call = call;
        this.value = map;
        this.idx = idx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        holder.SELECT_TEXT.setText(list.get(pos));
        if(pos==idx) {
            holder.SELECT_TEXT.setTextColor(Color.parseColor("#e51c52"));
        }
        holder.SELECT_LAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.SELECT_TEXT.setTextColor(Color.parseColor("#e51c52"));
                dialog.dismiss();
                ((ShopContentActivity)context).callJavascriptChangeSort(call + "('" + value.get(pos) +"')");
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }
}