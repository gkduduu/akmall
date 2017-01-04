package com.ak.android.akmall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.ShopContentActivity;
import com.ak.android.akmall.fragment.SelectPopup;
import com.ak.android.akmall.fragment.SharePopup;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.json.result.PageDatas;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkdud on 2016-11-02.
 * 베스트 리스트 아답타
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    Context context;
    List<PageDatas> list = new ArrayList<>();
    View.OnClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView I_GRID_MAINIV;
        RelativeLayout I_GRID_BRANCH_LAYOUT;
        LinearLayout align_relative01;
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

        ImageView I_GRID_MENUIV;
        ImageView I_GRID_CLOSEIV;
        ImageView I_GRID_SHAREIV;
        ImageView I_GRID_LIKEIV;
        ImageView I_GRID_BAGIV;
        View I_GRID_DELIVERY_BAR;

        LinearLayout layout_grid_tag;
        LinearLayout linear_grid_sales;
        LinearLayout linear_grid_rental;

        TextView txt_rental_sales, txt_rental_price, txt_rental_contract, txt_rental_sales_1;

        public ViewHolder(View view) {
            super(view);
            I_GRID_MAINIV = (ImageView) view.findViewById(R.id.I_GRID_MAINIV);
            I_GRID_BRANCH_LAYOUT = (RelativeLayout) view.findViewById(R.id.I_GRID_BRANCH_LAYOUT);
            align_relative01 = (LinearLayout)view.findViewById(R.id.align_relative01);

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
            I_GRID_DELIVERY_BAR = (View) view.findViewById(R.id.I_GRID_DELIVERY_BAR);

            I_GRID_MENUIV = (ImageView) view.findViewById(R.id.I_GRID_MENUIV);
            I_GRID_CLOSEIV = (ImageView) view.findViewById(R.id.I_GRID_CLOSEIV);
            I_GRID_SHAREIV = (ImageView) view.findViewById(R.id.I_GRID_SHAREIV);
            I_GRID_LIKEIV = (ImageView) view.findViewById(R.id.I_GRID_LIKEIV);
            I_GRID_BAGIV = (ImageView) view.findViewById(R.id.I_GRID_BAGIV);

            layout_grid_tag = (LinearLayout)view.findViewById(R.id.layout_grid_tag);
            linear_grid_sales = (LinearLayout)view.findViewById(R.id.linear_grid_sales);
            linear_grid_rental = (LinearLayout)view.findViewById(R.id.linear_grid_rental);

            txt_rental_sales = (TextView) view.findViewById(R.id.txt_rental_sales);
            txt_rental_sales_1 = (TextView) view.findViewById(R.id.txt_rental_sales_1);
            txt_rental_price = (TextView) view.findViewById(R.id.txt_rental_price);
            txt_rental_contract = (TextView) view.findViewById(R.id.txt_rental_contract);
        }
    }

    public GridAdapter(Context context, List<PageDatas> list, View.OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false);
        v.setOnClickListener(listener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        final PageDatas result = list.get(pos);
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
        //공유하기
        holder.I_GRID_SHAREIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharePopup(context, result.goods_id).show();
            }
        });
        //좋아요 찜
        holder.I_GRID_LIKEIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectPopup(context, Const.ITME_HEART + "&goods_id=" + result.goods_id).show();
            }
        });
        //장바구니 담기
        holder.I_GRID_BAGIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShopContentActivity) context).callJavascript(result.goods_id);
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

        //이미지
        Glide.with(context).load(BaseUtils.nvl(result.getImagePath).replace("@@@", "350"))
//                .centerCrop()
                .into(holder.I_GRID_MAINIV);

        //상품명
        holder.I_GRID_NAME.setText(BaseUtils.nvl(result.goods_name));

        //렌탈
        if(result.goods_kind_code.equals("010")) {
            holder.linear_grid_sales.setVisibility(View.GONE);
            holder.linear_grid_rental.setVisibility(View.VISIBLE);
            holder.I_GRID_NAME.setVisibility(View.VISIBLE);
            holder.txt_rental_sales.setVisibility(View.VISIBLE);
            holder.txt_rental_sales_1.setVisibility(View.VISIBLE);

            holder.txt_rental_sales.setText("할인가 "+BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)) +"원");
            holder.I_GRID_NAME.setTextSize(12);
            holder.txt_rental_price.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.rental_month_price)) +"원(/월)");
            holder.txt_rental_contract.setText(result.rental_months + "개월");

        } else if(result.goods_kind_code.equals("009")) {
            holder.linear_grid_sales.setVisibility(View.GONE);
            holder.linear_grid_rental.setVisibility(View.VISIBLE);
            holder.I_GRID_NAME.setVisibility(View.GONE);
            holder.txt_rental_sales.setVisibility(View.GONE);
            holder.txt_rental_sales_1.setVisibility(View.GONE);

            holder.txt_rental_price.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.rental_month_price)) +"원(/월)");
            holder.txt_rental_contract.setText(result.rental_months + "개월");

        } else {
            holder.linear_grid_sales.setVisibility(View.VISIBLE);
            holder.linear_grid_rental.setVisibility(View.GONE);
            holder.I_GRID_NAME.setVisibility(View.VISIBLE);

            //할인률 - 할인률이 0이면 원가만 표시
            if (BaseUtils.nvl(result.info.sale_rate, "0").equals("0")) {
                holder.I_GRID_PERCENT.setText("");
                holder.I_GRID_PERCENT2.setText("");
                holder.I_GRID_PRICE.setVisibility(View.GONE);
                holder.I_GRID_SALEPRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)) + "원");
            } else {
                holder.I_GRID_PERCENT.setText(BaseUtils.nvl(result.info.sale_rate, "0"));
                holder.I_GRID_PERCENT2.setText("%");
                holder.I_GRID_PRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.sale_price)) + "원");
                holder.I_GRID_SALEPRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)) + "원");
            }
        }

        //TODO : 추후 imagepath, isdetpventer, plazaname이 내려오는것에 대한 대응 필요
        //뺏지(지점이나 셀렉샵 픽 같은애들
        if (BaseUtils.nvl(result.launch_yn).equals("Y")) {
            holder.I_GRID_BRANCH_LAYOUT.setVisibility(View.VISIBLE);
            holder.I_GRID_BRANCH.setText(BaseUtils.nvl(result.getPlazaName));
            holder.I_GRID_SELECTIV.setVisibility(View.GONE);
            if (BaseUtils.nvl(result.smart_pick_yn).equals("Y") || BaseUtils.nvl(result.smart_pick_yn).equals("P")) {
                holder.I_GRID_PICKIV.setVisibility(View.VISIBLE);
            } else {
                holder.I_GRID_PICKIV.setVisibility(View.GONE);
            }
        } else {
            holder.I_GRID_BRANCH_LAYOUT.setVisibility(View.GONE);
            holder.I_GRID_SELECTIV.setVisibility(View.VISIBLE);
            holder.I_GRID_PICKIV.setVisibility(View.GONE);
        }

        if(holder.I_GRID_BRANCH_LAYOUT.getVisibility() == View.GONE
                && holder.I_GRID_PICKIV.getVisibility() == View.GONE
                && holder.I_GRID_SELECTIV.getVisibility() == View.GONE) {
            holder.layout_grid_tag.setVisibility(View.GONE);
        }


        //무료배송 여부
        if (BaseUtils.nvl(result.free_deliv_yn).equals("Y")) {
            holder.I_GRID_DELIVERY.setVisibility(View.VISIBLE);
            holder.I_GRID_DELIVERY_BAR.setVisibility(View.VISIBLE);
        } else {
            holder.I_GRID_DELIVERY.setVisibility(View.GONE);
            holder.I_GRID_DELIVERY_BAR.setVisibility(View.GONE);
        }

        holder.I_GRID_SCORE.setText("상품평(" + BaseUtils.nvl(result.comment_avg,"0") + ")");

    }

    public void clear() {
        list.clear();
    }

    public void add(PageDatas datas) {
        list.add(list.size(), datas);
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }
}