package android.ak.com.akmall.adapter;

import android.ak.com.akmall.R;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gkdud on 2016-08-10.
 */
public class PowerDealAdapter extends RecyclerView.Adapter<PowerDealAdapter.ViewHolder> {
    boolean isModifying;
    Context context;
    ArrayList<String> list = new ArrayList<>();
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView holderTitle;

        public ViewHolder(View view) {
            super(view);
//            holderTitle = (TextView)view.findViewById(R.id.MY_TV_TITLE);
        }
    }

    public PowerDealAdapter(Context context, ArrayList<String> historyList) {
        this.context = context;
        this.isModifying = isModifying;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_powerdeal, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int pos) {
    }

    @Override
    public int getItemCount() {
        if(null == list) {
            return 0;
        }
        return list.size();
    }
}