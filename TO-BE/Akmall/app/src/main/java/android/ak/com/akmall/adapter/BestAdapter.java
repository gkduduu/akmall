package android.ak.com.akmall.adapter;

import android.ak.com.akmall.R;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gkdud on 2016-11-02.
 * 베스트 리스트 아답타
 */
public class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView I_BEST_MAINIV;
        TextView I_BEST_COUNT;
        TextView I_BEST_BRANCH;
        ImageView I_BEST_PICKIV;
        ImageView I_BEST_SELECTIV;
        TextView I_BEST_NAME;
        TextView I_BEST_PERCENT;
        TextView I_BEST_PERCENT2;
        TextView I_BEST_PRICE;
        TextView I_BEST_SALEPRICE;
        TextView I_BEST_DELIVERY;
        TextView I_BEST_SCORE;
        ImageView I_BEST_SIMILARIV;

        ImageView I_BEST_MENUIV;
        ImageView I_BEST_CLOSEIV;
        ImageView I_BEST_SHAREIV;
        ImageView I_BEST_LIKEIV;
        ImageView I_BEST_BAGIV;

        public ViewHolder(View view) {
            super(view);
            I_BEST_MAINIV = (ImageView) view.findViewById(R.id.I_BEST_MAINIV);
            I_BEST_COUNT = (TextView) view.findViewById(R.id.I_BEST_COUNT);
            I_BEST_BRANCH = (TextView) view.findViewById(R.id.I_BEST_BRANCH);
            I_BEST_PICKIV = (ImageView) view.findViewById(R.id.I_BEST_PICKIV);
            I_BEST_SELECTIV = (ImageView) view.findViewById(R.id.I_BEST_SELECTIV);
            I_BEST_NAME = (TextView) view.findViewById(R.id.I_BEST_NAME);
            I_BEST_PERCENT = (TextView) view.findViewById(R.id.I_BEST_PERCENT);
            I_BEST_PERCENT2 = (TextView) view.findViewById(R.id.I_BEST_PERCENT2);
            I_BEST_PRICE = (TextView) view.findViewById(R.id.I_BEST_PRICE);
            I_BEST_SALEPRICE = (TextView) view.findViewById(R.id.I_BEST_SALEPRICE);
            I_BEST_DELIVERY = (TextView) view.findViewById(R.id.I_BEST_DELIVERY);
            I_BEST_SCORE = (TextView) view.findViewById(R.id.I_BEST_SCORE);
            I_BEST_SIMILARIV = (ImageView) view.findViewById(R.id.I_BEST_PICKIV);

            I_BEST_MENUIV = (ImageView) view.findViewById(R.id.I_BEST_MENUIV);
            I_BEST_CLOSEIV = (ImageView) view.findViewById(R.id.I_BEST_CLOSEIV);
            I_BEST_SHAREIV = (ImageView) view.findViewById(R.id.I_BEST_SHAREIV);
            I_BEST_LIKEIV = (ImageView) view.findViewById(R.id.I_BEST_LIKEIV);
            I_BEST_BAGIV = (ImageView) view.findViewById(R.id.I_BEST_BAGIV);
        }
    }

    public BestAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_best, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        //할인전 가격 가로줄귿기
        holder.I_BEST_PRICE.setPaintFlags(holder.I_BEST_PRICE.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //메뉴버튼 클릭
        holder.I_BEST_MENUIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.I_BEST_MENUIV.setVisibility(View.GONE);
                holder.I_BEST_CLOSEIV.setVisibility(View.VISIBLE);
                holder.I_BEST_SHAREIV.setVisibility(View.VISIBLE);
                holder.I_BEST_LIKEIV.setVisibility(View.VISIBLE);
                holder.I_BEST_BAGIV.setVisibility(View.VISIBLE);
            }
        });
        //메뉴클로즈버튼 클릭
        holder.I_BEST_CLOSEIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.I_BEST_MENUIV.setVisibility(View.VISIBLE);
                holder.I_BEST_CLOSEIV.setVisibility(View.GONE);
                holder.I_BEST_SHAREIV.setVisibility(View.GONE);
                holder.I_BEST_LIKEIV.setVisibility(View.GONE);
                holder.I_BEST_BAGIV.setVisibility(View.GONE);
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