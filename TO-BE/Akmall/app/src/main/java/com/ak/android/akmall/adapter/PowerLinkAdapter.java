package com.ak.android.akmall.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.MyWebviewActivity;
import com.ak.android.akmall.activity.MyWebviewActivity_;
import com.ak.android.akmall.activity.ShopContentActivity;
import com.ak.android.akmall.activity.WebviewActivity_;
import com.ak.android.akmall.utils.json.result.PowerLinkResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkdud on 2016-11-02.
 * 파워링크 아답타
 */
public class PowerLinkAdapter extends RecyclerView.Adapter<PowerLinkAdapter.ViewHolder> {
    Context context;
    List<PowerLinkResult> list = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView POWERAD_TITLE;
        TextView POWERAD_CONTENT;
        TextView POWERAD_LINK;
        LinearLayout POWERAD_LAYOUT;

        public ViewHolder(View view) {
            super(view);
            POWERAD_TITLE = (TextView)view.findViewById(R.id.POWERAD_TITLE);
            POWERAD_CONTENT = (TextView)view.findViewById(R.id.POWERAD_CONTENT);
            POWERAD_LINK = (TextView)view.findViewById(R.id.POWERAD_LINK);
            POWERAD_LAYOUT = (LinearLayout)view.findViewById(R.id.POWERAD_LAYOUT);
        }
    }

    public PowerLinkAdapter(Context context,List<PowerLinkResult> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_powerad, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        holder.POWERAD_LAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(pos).cUrl.contains("akmall.com")) {
                    context.startActivity(new Intent(context, MyWebviewActivity_.class).putExtra("url",list.get(pos).cUrl));
                }else {
                    context.startActivity(new Intent(context, WebviewActivity_.class).putExtra("url",list.get(pos).cUrl));
                }
            }
        });
        holder.POWERAD_TITLE.setText(list.get(pos).title);
        holder.POWERAD_LINK.setText(list.get(pos).vUrl);
        holder.POWERAD_CONTENT.setText(list.get(pos).desc);
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }
}