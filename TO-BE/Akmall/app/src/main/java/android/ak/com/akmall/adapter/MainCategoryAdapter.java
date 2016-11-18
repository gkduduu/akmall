package android.ak.com.akmall.adapter;

import android.ak.com.akmall.R;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gkdud on 2016-11-02.
 * 파워딜 아답타
 */
public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list = new ArrayList<>();
    View.OnClickListener listener;
    int seleted = 0;/*클릭한 카테고리 인덱스*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout I_MAIN_LAYOUT;
        TextView I_MAIN_CATE;
        ImageView I_MAIN_AK;
        ImageView I_MAIN_ARROW;
        public ViewHolder(View view) {
            super(view);
            I_MAIN_LAYOUT = (RelativeLayout)view.findViewById(R.id.I_MAIN_LAYOUT);
            I_MAIN_CATE = (TextView)view.findViewById(R.id.I_MAIN_CATE);
            I_MAIN_AK = (ImageView)view.findViewById(R.id.I_MAIN_AK);
            I_MAIN_ARROW = (ImageView)view.findViewById(R.id.I_MAIN_ARROW);
        }
    }

    public MainCategoryAdapter(Context context, ArrayList<String> list, View.OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_maincate, parent, false);
        v.setOnClickListener(listener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        Log.i("jhy",pos + " // " + seleted);
        if(pos == 0) {
            holder.I_MAIN_AK.setVisibility(View.VISIBLE);
            holder.I_MAIN_CATE.setText("");
        }else {
            holder.I_MAIN_AK.setVisibility(View.GONE);
            holder.I_MAIN_CATE.setText("카테고리명");
        }
        //선택한 셀에 하이라이트 처리
        if(seleted == pos) {
            holder.I_MAIN_LAYOUT.setBackgroundResource(R.drawable.border_bottom);
            holder.I_MAIN_ARROW.setVisibility(View.VISIBLE);
        }else {
            holder.I_MAIN_LAYOUT.setBackgroundResource(R.drawable.border_no);
            holder.I_MAIN_ARROW.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }

    public void clickThis(int pos) {
        seleted = pos;
    }
}