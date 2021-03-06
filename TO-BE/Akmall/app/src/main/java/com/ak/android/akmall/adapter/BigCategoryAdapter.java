package com.ak.android.akmall.adapter;

import com.ak.android.akmall.R;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkdud on 2016-11-02.
 * 빅 카테고리 아답타
 */
public class BigCategoryAdapter extends RecyclerView.Adapter<BigCategoryAdapter.ViewHolder> {
    private Context context;
    private List<String> list = new ArrayList<>();
    private View.OnClickListener listener;
    private int seleted = 0;/*클릭한 카테고리 인덱스*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout I_BIG_LAYOUT;
        TextView I_BIG_CATE;
        ImageView I_BIG_ARROW1;
        ImageView I_BIG_ARROW2;
        RelativeLayout I_BIG_BIG;

        public ViewHolder(View view) {
            super(view);
            I_BIG_LAYOUT = (LinearLayout) view.findViewById(R.id.I_BIG_LAYOUT);
            I_BIG_CATE = (TextView) view.findViewById(R.id.I_BIG_CATE);
            I_BIG_ARROW1 = (ImageView) view.findViewById(R.id.I_BIG_ARROW1);
            I_BIG_ARROW2 = (ImageView) view.findViewById(R.id.I_BIG_ARROW2);
            I_BIG_BIG = (RelativeLayout) view.findViewById(R.id.I_BIG_BIG);
        }
    }

    public BigCategoryAdapter(Context context, List<String> list,int detCtgSize,int lastCtgSize, View.OnClickListener listener) {
        this.context = context;
        this.list.add("홈");
        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                this.list.add(list.get(i));
            }
            if (!list.isEmpty()) {
                if(list.size()==3) {
                    if(lastCtgSize != 0) {
                        this.list.add("카테고리 선택");
                    }
                }
                if(list.size() == 2) {
                    if(detCtgSize != 0) {
                        this.list.add("카테고리 선택");
                    }
                }
                if (list.size() == 1) this.list.add("카테고리 선택");
            }
        }
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bigcate, parent, false);
        v.setOnClickListener(listener);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        Log.i("jhy", pos + " // " + seleted);
        if (pos == 0) {
            holder.I_BIG_ARROW1.setVisibility(View.GONE);
            holder.I_BIG_CATE.setText("홈");
        } else {
            holder.I_BIG_ARROW1.setVisibility(View.VISIBLE);
            holder.I_BIG_CATE.setText(list.get(pos));
        }
        if (pos == list.size() - 1) {
            holder.I_BIG_ARROW2.setVisibility(View.GONE);
            holder.I_BIG_BIG.setVisibility(View.VISIBLE);
        } else {
            holder.I_BIG_ARROW2.setVisibility(View.VISIBLE);
            holder.I_BIG_BIG.setVisibility(View.GONE);
        }

        holder.itemView.setVerticalScrollBarEnabled(false);
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }

//    public void clickThis(int pos) {
//        seleted = pos;
//    }
}