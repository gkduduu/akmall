package com.ak.android.akmall.widget;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ak.android.akmall.R;
import com.ak.android.akmall.model.SearchKeyword;

public class SearchKeywordAdapter extends BaseAdapter {

	public static final String TAG = "SearchKeywordAdapter";

	private List<? extends SearchKeyword> mList;

	private Context mContext;
	private LayoutInflater mInflater;
	private int mResource;

	public SearchKeywordAdapter(Context context, List<? extends SearchKeyword> list) {

		if(list == null) {
			throw new NullPointerException("search keyword list is null..");
		}

		mList = list;
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResource = R.layout.search_keyword_list_item;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public SearchKeyword getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewWrapper wrapper;

		if (convertView == null) {
			view = mInflater.inflate(mResource, parent, false);
			wrapper = new ViewWrapper(view);
			view.setTag(wrapper);
		} else {
			view = convertView;
			wrapper = (ViewWrapper) view.getTag();
		}

		wrapper.setSearchKeyword(position, getItem(position));

		return view;
	}

	private class ViewWrapper {

		private TextView keywordView;
		private ViewGroup listItem;

		public ViewWrapper(View base) {
			keywordView = (TextView) base.findViewById(R.id.searchKeywordTextView);
			listItem = (ViewGroup) base.findViewById(R.id.search_keyword_list_item);
		}

		public void setSearchKeyword(int position, SearchKeyword keyword) {
			keywordView.setText(position + 1 + ". " + keyword.getKeyword());
			listItem.setBackgroundResource(position % 2 == 0 ? R.drawable.list_bg_even : R.drawable.list_bg_odd);
		}
	} // ViewWrapper class

}
