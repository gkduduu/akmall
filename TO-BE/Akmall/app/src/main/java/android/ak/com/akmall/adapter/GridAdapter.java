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
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView I_GRID_MAINIV;
        TextView I_GRID_BRANCH;
        ImageView I_GRID_PICKIV;
        ImageView I_GRID_SELECTIV;
        TextView I_GRID_NAME;
        TextView I_GRID_PERCENT;
        TextView I_GRID_PERCENT2;
        TextView I_GRID_PRICE;
        TextView I_GRID_SALEPRICE;
        TextView I_GRID_DELIVERY;
        TextView I_GRID_SCORE;
        ImageView I_GRID_SIMILARIV;

        ImageView I_GRID_MENUIV;
        ImageView I_GRID_CLOSEIV;
        ImageView I_GRID_SHAREIV;
        ImageView I_GRID_LIKEIV;
        ImageView I_GRID_BAGIV;

        public ViewHolder(View view) {
            super(view);
            I_GRID_MAINIV = (ImageView) view.findViewById(R.id.I_GRID_MAINIV);
            I_GRID_BRANCH = (TextView) view.findViewById(R.id.I_GRID_BRANCH);
            I_GRID_PICKIV = (ImageView) view.findViewById(R.id.I_GRID_PICKIV);
            I_GRID_SELECTIV = (ImageView) view.findViewById(R.id.I_GRID_SELECTIV);
            I_GRID_NAME = (TextView) view.findViewById(R.id.I_GRID_NAME);
            I_GRID_PERCENT = (TextView) view.findViewById(R.id.I_GRID_PERCENT);
            I_GRID_PERCENT2 = (TextView) view.findViewById(R.id.I_GRID_PERCENT2);
            I_GRID_PRICE = (TextView) view.findViewById(R.id.I_GRID_PRICE);
            I_GRID_SALEPRICE = (TextView) view.findViewById(R.id.I_GRID_SALEPRICE);
            I_GRID_DELIVERY = (TextView) view.findViewById(R.id.I_GRID_DELIVERY);
            I_GRID_SCORE = (TextView) view.findViewById(R.id.I_GRID_SCORE);
            I_GRID_SIMILARIV = (ImageView) view.findViewById(R.id.I_GRID_PICKIV);

            I_GRID_MENUIV = (ImageView) view.findViewById(R.id.I_GRID_MENUIV);
            I_GRID_CLOSEIV = (ImageView) view.findViewById(R.id.I_GRID_CLOSEIV);
            I_GRID_SHAREIV = (ImageView) view.findViewById(R.id.I_GRID_SHAREIV);
            I_GRID_LIKEIV = (ImageView) view.findViewById(R.id.I_GRID_LIKEIV);
            I_GRID_BAGIV = (ImageView) view.findViewById(R.id.I_GRID_BAGIV);
        }
    }

    public GridAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        //할인전 가격 가로줄귿기
        holder.I_GRID_PRICE.setPaintFlags(holder.I_GRID_PRICE.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //메뉴버튼 클릭
        holder.I_GRID_MENUIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.I_GRID_MENUIV.setVisibility(View.GONE);
                holder.I_GRID_CLOSEIV.setVisibility(View.VISIBLE);
                holder.I_GRID_SHAREIV.setVisibility(View.VISIBLE);
                holder.I_GRID_LIKEIV.setVisibility(View.VISIBLE);
                holder.I_GRID_BAGIV.setVisibility(View.VISIBLE);
            }
        });
        //메뉴클로즈버튼 클릭
        holder.I_GRID_CLOSEIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.I_GRID_MENUIV.setVisibility(View.VISIBLE);
                holder.I_GRID_CLOSEIV.setVisibility(View.GONE);
                holder.I_GRID_SHAREIV.setVisibility(View.GONE);
                holder.I_GRID_LIKEIV.setVisibility(View.GONE);
                holder.I_GRID_BAGIV.setVisibility(View.GONE);
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