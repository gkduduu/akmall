package com.ak.android.akplaza.qrcode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ak.android.akplaza.R;
import com.google.zxing.Result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter{

	private LayoutInflater inflater	= null;
	private List<Result> mList = null;
	
	public HistoryAdapter(Context context, List<Result> list){
		mList = list;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = inflater.inflate(com.ak.android.akplaza.R.layout.notiboxlistview, parent, false);
		}
		Result result = mList.get(position);
//		TextView resultTextView = (TextView)convertView.findViewById(R.id.barcode_result_text);
		TextView resultTextView = (TextView)convertView.findViewById(R.id.alram_save_text);
//		TextView resultStampView = (TextView)convertView.findViewById(R.id.barcode_result_stamp);
		TextView resultStampView = (TextView)convertView.findViewById(R.id.alram_save_date);
		
		Date date = new Date(result.getTimestamp());
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		String strDate = df.format(date);
		
		resultTextView.setText(result.getText());
		resultStampView.setText(strDate);
		
		return convertView;
	}

}
