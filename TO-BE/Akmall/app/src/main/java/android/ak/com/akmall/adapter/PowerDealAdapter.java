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
 * Created by gkdud on 2016-11-02.
 * 파워딜 아답타
 */
public class PowerDealAdapter extends RecyclerView.Adapter<PowerDealAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView I_POWERDEAL_REDTV;
        TextView I_POWERDEAL_TIMETV;
        ImageView I_POWERDEAL_MAINIV;
        TextView I_POWERDEAL_TITLE;
        TextView I_POWERDEAL_CONTENT;
        TextView I_POWERDEAL_EA;

        public ViewHolder(View view) {
            super(view);
             I_POWERDEAL_REDTV = (TextView)view.findViewById(R.id.I_POWERDEAL_REDTV);
             I_POWERDEAL_TIMETV = (TextView)view.findViewById(R.id.I_POWERDEAL_TIMETV);
             I_POWERDEAL_MAINIV = (ImageView) view.findViewById(R.id.I_POWERDEAL_MAINIV);
             I_POWERDEAL_TITLE = (TextView)view.findViewById(R.id.I_POWERDEAL_TITLE);
             I_POWERDEAL_CONTENT = (TextView)view.findViewById(R.id.I_POWERDEAL_CONTENT);
             I_POWERDEAL_EA = (TextView)view.findViewById(R.id.I_POWERDEAL_EA);
        }
    }

    public PowerDealAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
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
    public void onBindViewHolder(ViewHolder holder, final int pos) {
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }
}