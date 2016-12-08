package com.ak.android.akmall.adapter;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.MyWebviewActivity_;
import com.ak.android.akmall.activity.ShopContentActivity;
import com.ak.android.akmall.fragment.SelectPopup;
import com.ak.android.akmall.fragment.SharePopup;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.json.result.PageDatas;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkdud on 2016-11-02.
 * 베스트 리스트 아답타
 */
public class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.ViewHolder> {
    Context context;
    List<PageDatas> list = new ArrayList<>();
    View.OnClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView I_GALLARY_MAINIV;
        TextView I_GALLARY_BRANCH;
        ImageView I_GALLARY_PICKIV;
        ImageView I_GALLARY_SELECTIV;
        TextView I_GALLARY_NAME;
        TextView I_GALLARY_PERCENT;
        TextView I_GALLARY_PERCENT2;
        TextView I_GALLARY_PRICE;
        TextView I_GALLARY_SALEPRICE;
        TextView I_GALLARY_DELIVERY;
        TextView I_GALLARY_SCORE;

        ImageView I_GALLARY_MENUIV;
        ImageView I_GALLARY_CLOSEIV;
        ImageView I_GALLARY_SHAREIV;
        ImageView I_GALLARY_LIKEIV;
        ImageView I_GALLARY_BAGIV;
        ImageView I_GALLARY_SIMILARIV;
        View I_GALLARY_DELIVERY_BAR;

        public ViewHolder(View view) {
            super(view);
            I_GALLARY_MAINIV = (ImageView) view.findViewById(R.id.I_GALLARY_MAINIV);
            I_GALLARY_BRANCH = (TextView) view.findViewById(R.id.I_GALLARY_BRANCH);
            I_GALLARY_PICKIV = (ImageView) view.findViewById(R.id.I_GALLARY_PICKIV);
            I_GALLARY_SELECTIV = (ImageView) view.findViewById(R.id.I_GALLARY_SELECTIV);
            I_GALLARY_NAME = (TextView) view.findViewById(R.id.I_GALLARY_NAME);
            I_GALLARY_PERCENT = (TextView) view.findViewById(R.id.I_GALLARY_PERCENT);
            I_GALLARY_PERCENT2 = (TextView) view.findViewById(R.id.I_GALLARY_PERCENT2);
            I_GALLARY_PRICE = (TextView) view.findViewById(R.id.I_GALLARY_PRICE);
            I_GALLARY_SALEPRICE = (TextView) view.findViewById(R.id.I_GALLARY_SALEPRICE);
            I_GALLARY_DELIVERY = (TextView) view.findViewById(R.id.I_GALLARY_DELIVERY);
            I_GALLARY_SCORE = (TextView) view.findViewById(R.id.I_GALLARY_SCORE);
            I_GALLARY_DELIVERY_BAR = (View) view.findViewById(R.id.I_GALLARY_DELIVERY_BAR);

            I_GALLARY_MENUIV = (ImageView) view.findViewById(R.id.I_GALLARY_MENUIV);
            I_GALLARY_CLOSEIV = (ImageView) view.findViewById(R.id.I_GALLARY_CLOSEIV);
            I_GALLARY_SHAREIV = (ImageView) view.findViewById(R.id.I_GALLARY_SHAREIV);
            I_GALLARY_LIKEIV = (ImageView) view.findViewById(R.id.I_GALLARY_LIKEIV);
            I_GALLARY_BAGIV = (ImageView) view.findViewById(R.id.I_GALLARY_BAGIV);
            I_GALLARY_SIMILARIV = (ImageView)view.findViewById(R.id.I_GALLARY_SIMILARIV);
        }
    }

    public GallaryAdapter(Context context, List<PageDatas> list, View.OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallary, parent, false);
        v.setOnClickListener(listener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        final PageDatas result = list.get(pos);
        //할인전 가격 가로줄귿기
        holder.I_GALLARY_PRICE.setPaintFlags(holder.I_GALLARY_PRICE.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //메뉴버튼 클릭
        holder.I_GALLARY_MENUIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.I_GALLARY_MENUIV.setVisibility(View.GONE);
                holder.I_GALLARY_CLOSEIV.setVisibility(View.VISIBLE);
                holder.I_GALLARY_SHAREIV.setVisibility(View.VISIBLE);
                holder.I_GALLARY_LIKEIV.setVisibility(View.VISIBLE);
                holder.I_GALLARY_BAGIV.setVisibility(View.VISIBLE);
            }
        });
        //공유하기
        holder.I_GALLARY_SHAREIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharePopup(context, result.goods_id).show();
            }
        });
        //좋아요 찜
        holder.I_GALLARY_LIKEIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectPopup(context, Const.ITME_HEART + "&goods_id=" + result.goods_id).show();
            }
        });
        //장바구니 담기
        holder.I_GALLARY_BAGIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShopContentActivity) context).callJavascript(result.goods_id);
            }
        });
        //메뉴클로즈버튼 클릭
        holder.I_GALLARY_CLOSEIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.I_GALLARY_MENUIV.setVisibility(View.VISIBLE);
                holder.I_GALLARY_CLOSEIV.setVisibility(View.GONE);
                holder.I_GALLARY_SHAREIV.setVisibility(View.GONE);
                holder.I_GALLARY_LIKEIV.setVisibility(View.GONE);
                holder.I_GALLARY_BAGIV.setVisibility(View.GONE);
            }
        });
        //연관상품
        holder.I_GALLARY_SIMILARIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyWebviewActivity_.class).
                        putExtra("url", "/common/AppPage?forwardPage=RelatedGoodsList&" +
                                "goods_id=" + result.goods_id));
            }
        });

        //상품명
        holder.I_GALLARY_NAME.setText(BaseUtils.nvl(result.goods_name));
        //할인률 - 할인률이 0이면 원가만 표시
        if (BaseUtils.nvl(result.info.sale_rate, "0").equals("0")) {
            holder.I_GALLARY_PERCENT.setText("");
            holder.I_GALLARY_PERCENT2.setText("");
            holder.I_GALLARY_PRICE.setText("");
            holder.I_GALLARY_SALEPRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)) + "원");
        } else {
            holder.I_GALLARY_PERCENT.setText(BaseUtils.nvl(result.info.sale_rate, "0"));
            holder.I_GALLARY_PERCENT2.setText("%");
            holder.I_GALLARY_PRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.sale_price)) + "원");
            holder.I_GALLARY_SALEPRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)) + "원");
        }
        holder.I_GALLARY_SCORE.setText("상품평(" + BaseUtils.nvl(result.comment_avg,"0") + ")");

//        //이미지
        Glide.with(context).load(BaseUtils.nvl(result.getImagePath).replace("@@@", "350"))
                .into(holder.I_GALLARY_MAINIV);

        //무료배송 여부
        if (BaseUtils.nvl(result.free_deliv_yn).equals("Y")) {
            holder.I_GALLARY_DELIVERY.setVisibility(View.VISIBLE);
            holder.I_GALLARY_DELIVERY_BAR.setVisibility(View.VISIBLE);
        } else {
            holder.I_GALLARY_DELIVERY.setVisibility(View.GONE);
            holder.I_GALLARY_DELIVERY_BAR.setVisibility(View.GONE);
        }
        //TODO : 추후 imagepath, isdetpventer, plazaname이 내려오는것에 대한 대응 필요
        //뺏지(지점이나 셀렉샵 픽 같은애들
        if (BaseUtils.nvl(result.launch_yn).equals("Y")) {
            holder.I_GALLARY_BRANCH.setText(BaseUtils.nvl(result.getPlazaName));
            holder.I_GALLARY_SELECTIV.setVisibility(View.GONE);
            if (BaseUtils.nvl(result.smart_pick_yn).equals("Y") || BaseUtils.nvl(result.smart_pick_yn).equals("P")) {
                holder.I_GALLARY_PICKIV.setVisibility(View.VISIBLE);
            } else {
                holder.I_GALLARY_PICKIV.setVisibility(View.GONE);
            }
        } else {
            holder.I_GALLARY_SELECTIV.setVisibility(View.VISIBLE);
            holder.I_GALLARY_PICKIV.setVisibility(View.GONE);
        }

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