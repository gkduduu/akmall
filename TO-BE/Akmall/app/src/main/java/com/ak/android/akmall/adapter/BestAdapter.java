package com.ak.android.akmall.adapter;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.MainActivity_;
import com.ak.android.akmall.activity.MyWebviewActivity_;
import com.ak.android.akmall.activity.ShopContentActivity;
import com.ak.android.akmall.fragment.SelectPopup;
import com.ak.android.akmall.fragment.SharePopup;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.json.result.PageDatas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkdud on 2016-11-02.
 * 베스트 리스트 아답타
 */
public class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {
    Context context;
    List<PageDatas> list = new ArrayList<>();
    View.OnClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView I_BEST_MAINIV;
        TextView I_BEST_COUNT;
        RelativeLayout I_BEST_BRANCH_LAYOUT;
        TextView I_BEST_BRANCH;
        ImageView I_BEST_PICKIV;
        ImageView I_BEST_SELECTIV;
        TextView I_BEST_NAME;
        TextView I_BEST_PERCENT;
        TextView I_BEST_PERCENT2;
        TextView I_BEST_PRICE;
        TextView I_BEST_SALEPRICE;
        LinearLayout I_BEST_DELIVERY;
        TextView I_BEST_SCORE;
        ImageView I_BEST_SIMILARIV;

        ImageView I_BEST_MENUIV;
        ImageView I_BEST_CLOSEIV;
        ImageView I_BEST_SHAREIV;
        ImageView I_BEST_LIKEIV;
        ImageView I_BEST_BAGIV;

        LinearLayout layout_best_tag;
        LinearLayout linear_best_sales;
        LinearLayout linear_best_rental;

        TextView txt_rental_sales, txt_rental_price, txt_rental_contract, txt_rental_sales_1;

        public ViewHolder(View view) {
            super(view);
            I_BEST_MAINIV = (ImageView) view.findViewById(R.id.I_BEST_MAINIV);
            I_BEST_COUNT = (TextView) view.findViewById(R.id.I_BEST_COUNT);
            I_BEST_BRANCH = (TextView) view.findViewById(R.id.I_BEST_BRANCH);
            I_BEST_BRANCH_LAYOUT = (RelativeLayout)view.findViewById(R.id.I_BEST_BRANCH_LAYOUT);
            I_BEST_PICKIV = (ImageView) view.findViewById(R.id.I_BEST_PICKIV);
            I_BEST_SELECTIV = (ImageView) view.findViewById(R.id.I_BEST_SELECTIV);
            I_BEST_NAME = (TextView) view.findViewById(R.id.I_BEST_NAME);
            I_BEST_PERCENT = (TextView) view.findViewById(R.id.I_BEST_PERCENT);
            I_BEST_PERCENT2 = (TextView) view.findViewById(R.id.I_BEST_PERCENT2);
            I_BEST_PRICE = (TextView) view.findViewById(R.id.I_BEST_PRICE);
            I_BEST_SALEPRICE = (TextView) view.findViewById(R.id.I_BEST_SALEPRICE);
            I_BEST_DELIVERY = (LinearLayout) view.findViewById(R.id.I_BEST_DELIVERY);
            I_BEST_SCORE = (TextView) view.findViewById(R.id.I_BEST_SCORE);
            I_BEST_SIMILARIV = (ImageView) view.findViewById(R.id.I_BEST_SIMILARIV);

            I_BEST_MENUIV = (ImageView) view.findViewById(R.id.I_BEST_MENUIV);
            I_BEST_CLOSEIV = (ImageView) view.findViewById(R.id.I_BEST_CLOSEIV);
            I_BEST_SHAREIV = (ImageView) view.findViewById(R.id.I_BEST_SHAREIV);
            I_BEST_LIKEIV = (ImageView) view.findViewById(R.id.I_BEST_LIKEIV);
            I_BEST_BAGIV = (ImageView) view.findViewById(R.id.I_BEST_BAGIV);

            layout_best_tag = (LinearLayout)view.findViewById(R.id.layout_best_tag);
            linear_best_sales = (LinearLayout)view.findViewById(R.id.linear_best_sales);
            linear_best_rental = (LinearLayout)view.findViewById(R.id.linear_best_rental);

            txt_rental_sales = (TextView) view.findViewById(R.id.txt_rental_sales);
            txt_rental_sales_1 = (TextView) view.findViewById(R.id.txt_rental_sales_1);
            txt_rental_price = (TextView) view.findViewById(R.id.txt_rental_price);
            txt_rental_contract = (TextView) view.findViewById(R.id.txt_rental_contract);
        }
    }

    public BestAdapter(Context context, List<PageDatas> list,View.OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best, parent, false);
        v.setOnClickListener(listener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        final PageDatas result = list.get(pos);

        //상품평 터치 막기
        holder.I_BEST_SCORE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

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

        //공유하기
        holder.I_BEST_SHAREIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharePopup(context, result.goods_id).show();
            }
        });
        //좋아요 찜
        holder.I_BEST_LIKEIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity_.loginCheck) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setTitle("AK MALL");
                    alert.setMessage("로그인 후 이용해주세요.");
                    alert.show();

                } else {
                    new SelectPopup(context, Const.ITME_HEART + "&goods_id=" + result.goods_id).show();
                }
            }
        });
        //장바구니 담기
        holder.I_BEST_BAGIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShopContentActivity) context).callJavascript(result.goods_id);
            }
        });
        //연관상품
        holder.I_BEST_SIMILARIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyWebviewActivity_.class).
                        putExtra("url", "/common/AppPage.do?forwardPage=RelatedGoodsList&" +
                                "goods_id=" + result.goods_id));
            }
        });

        //상품명
        holder.I_BEST_NAME.setText(result.goods_name);
        //상품 이미지
        Glide.with(context).load(result.getImagePath.replace("@@@","350"))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.I_BEST_MAINIV);



        //렌탈
        if(result.goods_kind_code.equals("010")) {
            holder.linear_best_sales.setVisibility(View.GONE);
            holder.linear_best_rental.setVisibility(View.VISIBLE);
            holder.I_BEST_NAME.setVisibility(View.VISIBLE);
            holder.txt_rental_sales.setVisibility(View.VISIBLE);
            holder.txt_rental_sales_1.setVisibility(View.VISIBLE);

            holder.txt_rental_sales.setText("할인가 "+BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)) +"원");
            holder.I_BEST_NAME.setTextSize(12);
            holder.txt_rental_price.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.rental_month_price)) +"원(/월)");
            holder.txt_rental_contract.setText(result.rental_months + "개월");

        } else if(result.goods_kind_code.equals("009")) {
            holder.linear_best_sales.setVisibility(View.GONE);
            holder.linear_best_rental.setVisibility(View.VISIBLE);
            holder.I_BEST_NAME.setVisibility(View.GONE);
            holder.txt_rental_sales.setVisibility(View.GONE);
            holder.txt_rental_sales_1.setVisibility(View.GONE);

            holder.txt_rental_price.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.rental_month_price)) + "원(/월)");
            holder.txt_rental_contract.setText(result.rental_months + "개월");

        } else {
            holder.linear_best_sales.setVisibility(View.VISIBLE);
            holder.linear_best_rental.setVisibility(View.GONE);
            holder.I_BEST_NAME.setVisibility(View.VISIBLE);

            //할인
//            if(!BaseUtils.nvl(result.info.sale_rate, "0").equals("0") || !BaseUtils.nvl(result.info.sale_rate).equals("")) {
            if (BaseUtils.nvl(result.info.sale_rate, "0").equals("0")) {
//                holder.I_BEST_PERCENT.setText(result.info.sale_rate);
//                holder.I_BEST_PERCENT2.setVisibility(View.VISIBLE);
//                holder.I_BEST_PRICE.setText(BaseUtils.wonFormat(result.info.sale_price) + "원");

                holder.I_BEST_PERCENT.setText("");
                holder.I_BEST_PERCENT2.setText("");
                holder.I_BEST_PRICE.setText("");
                holder.I_BEST_SALEPRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)));

            // 할인 아님
            }else {
//                holder.I_BEST_PERCENT.setText("");
//                holder.I_BEST_PERCENT2.setVisibility(View.GONE);
//                holder.I_BEST_PRICE.setText("");

                holder.I_BEST_PERCENT.setText(BaseUtils.nvl(result.info.sale_rate, "0"));
                holder.I_BEST_PERCENT2.setText("%");
                holder.I_BEST_PRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.sale_price)) + "원");
                holder.I_BEST_SALEPRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)));
            }
        }

        //할인가격
        holder.I_BEST_SALEPRICE.setText(BaseUtils.wonFormat(result.info.final_price));

        //상품평 갯수
        holder.I_BEST_SCORE.setText("상품평(" + BaseUtils.nvl(result.comment_cnt,"0") + ")");
        //무료배송 여부
        if(BaseUtils.nvl(result.free_deliv_yn).equals("Y")) {
            holder.I_BEST_DELIVERY.setVisibility(View.VISIBLE);
        }else {
            holder.I_BEST_DELIVERY.setVisibility(View.GONE);
        }
        //TODO : 추후 imagepath, isdetpventer, plazaname이 내려오는것에 대한 대응 필요
        //뺏지(지점이나 셀렉샵 픽 같은애들
        if(BaseUtils.nvl(result.launch_yn).equals("Y")) {
            holder.I_BEST_BRANCH_LAYOUT.setVisibility(View.VISIBLE);
            holder.I_BEST_BRANCH.setText(BaseUtils.nvl(result.getPlazaName));
            holder.I_BEST_SELECTIV.setVisibility(View.GONE);
            if(BaseUtils.nvl(result.smart_pick_yn).equals("Y")||BaseUtils.nvl(result.smart_pick_yn).equals("P")) {
                holder.I_BEST_PICKIV.setVisibility(View.VISIBLE);
            }else {
                holder.I_BEST_PICKIV.setVisibility(View.GONE);
            }
        }else {
            holder.I_BEST_BRANCH_LAYOUT.setVisibility(View.GONE);
            holder.I_BEST_SELECTIV.setVisibility(View.VISIBLE);
            holder.I_BEST_PICKIV.setVisibility(View.GONE);
        }

        if(holder.I_BEST_BRANCH_LAYOUT.getVisibility() == View.GONE
                && holder.I_BEST_PICKIV.getVisibility() == View.GONE
                && holder.I_BEST_SELECTIV.getVisibility() == View.GONE) {
            holder.layout_best_tag.setVisibility(View.GONE);
        }

    }

    public void clear() {
        list.clear();
    }

    public void add(PageDatas datas) {
        list.add(list.size(),datas);
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }
}