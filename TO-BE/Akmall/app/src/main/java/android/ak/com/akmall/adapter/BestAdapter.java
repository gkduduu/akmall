package android.ak.com.akmall.adapter;

import android.ak.com.akmall.R;
import android.ak.com.akmall.utils.BaseUtils;
import android.ak.com.akmall.utils.json.result.PageDatas;
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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkdud on 2016-11-02.
 * 베스트 리스트 아답타
 */
public class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {
    Context context;
    List<PageDatas> list = new ArrayList<>();

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
            I_BEST_SIMILARIV = (ImageView) view.findViewById(R.id.I_BEST_PICKIV);

            I_BEST_MENUIV = (ImageView) view.findViewById(R.id.I_BEST_MENUIV);
            I_BEST_CLOSEIV = (ImageView) view.findViewById(R.id.I_BEST_CLOSEIV);
            I_BEST_SHAREIV = (ImageView) view.findViewById(R.id.I_BEST_SHAREIV);
            I_BEST_LIKEIV = (ImageView) view.findViewById(R.id.I_BEST_LIKEIV);
            I_BEST_BAGIV = (ImageView) view.findViewById(R.id.I_BEST_BAGIV);
        }
    }

    public BestAdapter(Context context, List<PageDatas> list) {
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
        PageDatas result = list.get(pos);
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

        //상품명
        holder.I_BEST_NAME.setText(result.goods_name);
        //상품 이미지
        Glide.with(context).load("http://91.3.115.135:8082/resources/images/branch/gallery_visual2.jpg")
                .centerCrop()
                .into(holder.I_BEST_MAINIV);

        //가격표시 - 할인률을 보고 할인인이 아닌지 판단
        if(!BaseUtils.nvl(result.info.sale_rate).equals("0")||!BaseUtils.nvl(result.info.sale_rate).equals("")) {
            //할인률
            holder.I_BEST_PERCENT.setText(result.info.sale_rate);
            //원래가격
            holder.I_BEST_PRICE.setText(BaseUtils.wonFormat(result.info.sale_price) + "원");
            holder.I_BEST_PERCENT2.setVisibility(View.VISIBLE);
        }else {
            //할인이 아니므로 가격만 표시
            holder.I_BEST_PERCENT.setText("");
            holder.I_BEST_PERCENT2.setVisibility(View.GONE);
            holder.I_BEST_PRICE.setText("");
        }
        //할인가격
        holder.I_BEST_SALEPRICE.setText(BaseUtils.wonFormat(result.info.final_price)+"원");

        //상품평 갯수
        holder.I_BEST_SCORE.setText("상품평(" + result.comment_cnt + ")");
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

    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }
}