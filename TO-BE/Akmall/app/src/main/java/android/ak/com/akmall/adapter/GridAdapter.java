package android.ak.com.akmall.adapter;

import android.ak.com.akmall.R;
import android.ak.com.akmall.activity.MyWebviewActivity;
import android.ak.com.akmall.activity.MyWebviewActivity_;
import android.ak.com.akmall.activity.ShopContentActivity;
import android.ak.com.akmall.fragment.SelectPopup;
import android.ak.com.akmall.fragment.SharePopup;
import android.ak.com.akmall.utils.BaseUtils;
import android.ak.com.akmall.utils.Const;
import android.ak.com.akmall.utils.json.result.PageDatas;
import android.content.Context;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView I_GRID_MAINIV;
        RelativeLayout I_GRID_BRANCH_LAYOUT;
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

        public ViewHolder(View view) {
            super(view);
            I_GRID_MAINIV = (ImageView) view.findViewById(R.id.I_GRID_MAINIV);
            I_GRID_BRANCH_LAYOUT = (RelativeLayout)view.findViewById(R.id.I_GRID_BRANCH_LAYOUT);
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
            I_GRID_DELIVERY_BAR = (View)view.findViewById(R.id.I_GRID_DELIVERY_BAR);

            I_GRID_MENUIV = (ImageView) view.findViewById(R.id.I_GRID_MENUIV);
            I_GRID_CLOSEIV = (ImageView) view.findViewById(R.id.I_GRID_CLOSEIV);
            I_GRID_SHAREIV = (ImageView) view.findViewById(R.id.I_GRID_SHAREIV);
            I_GRID_LIKEIV = (ImageView) view.findViewById(R.id.I_GRID_LIKEIV);
            I_GRID_BAGIV = (ImageView) view.findViewById(R.id.I_GRID_BAGIV);
        }
    }

    public GridAdapter(Context context, List<PageDatas> list) {
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
                new SharePopup(context,result.goods_id).show();
            }
        });
        //좋아요 찜
        holder.I_GRID_LIKEIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectPopup(context,Const.ITME_HEART + "&goods_id=" + result.goods_id).show();
            }
        });
        //장바구니 담기
        holder.I_GRID_BAGIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShopContentActivity)context).callJavascript(result.goods_id);
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

        //상품명
        holder.I_GRID_NAME.setText(BaseUtils.nvl(result.goods_name));
        //할인률 - 할인률이 0이면 원가만 표시
        if(BaseUtils.nvl(result.info.sale_rate,"0").equals("0")) {
            holder.I_GRID_PERCENT.setText("");
            holder.I_GRID_PERCENT2.setText("");
            holder.I_GRID_PRICE.setText("");
            holder.I_GRID_SALEPRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)) + "원");
        }else {
            holder.I_GRID_PERCENT.setText(BaseUtils.nvl(result.info.sale_rate, "0"));
            holder.I_GRID_PERCENT2.setText("%");
            holder.I_GRID_PRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.sale_price)) + "원");
            holder.I_GRID_SALEPRICE.setText(BaseUtils.wonFormat(BaseUtils.nvl(result.info.final_price)) + "원");
        }
        holder.I_GRID_SCORE.setText("상품평(" + BaseUtils.nvl(result.comment_avg) + ")");

        //이미지
        Glide.with(context).load(BaseUtils.nvl(result.getImagePath).replace("@@@","350"))
                .centerCrop()
                .into(holder.I_GRID_MAINIV);

        //무료배송 여부
        if(BaseUtils.nvl(result.free_deliv_yn).equals("Y")) {
            holder.I_GRID_DELIVERY.setVisibility(View.VISIBLE);
            holder.I_GRID_DELIVERY_BAR.setVisibility(View.VISIBLE);
        }else {
            holder.I_GRID_DELIVERY.setVisibility(View.GONE);
            holder.I_GRID_DELIVERY_BAR.setVisibility(View.GONE);
        }
        //TODO : 추후 imagepath, isdetpventer, plazaname이 내려오는것에 대한 대응 필요
        //뺏지(지점이나 셀렉샵 픽 같은애들
        if(BaseUtils.nvl(result.launch_yn).equals("Y")) {
            holder.I_GRID_BRANCH_LAYOUT.setVisibility(View.VISIBLE);
            holder.I_GRID_BRANCH.setText(BaseUtils.nvl(result.getPlazaName));
            holder.I_GRID_SELECTIV.setVisibility(View.GONE);
            if(BaseUtils.nvl(result.smart_pick_yn).equals("Y")||BaseUtils.nvl(result.smart_pick_yn).equals("P")) {
                holder.I_GRID_PICKIV.setVisibility(View.VISIBLE);
            }else {
                holder.I_GRID_PICKIV.setVisibility(View.GONE);
            }
        }else {
            holder.I_GRID_BRANCH_LAYOUT.setVisibility(View.GONE);
            holder.I_GRID_SELECTIV.setVisibility(View.VISIBLE);
            holder.I_GRID_PICKIV.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }
}