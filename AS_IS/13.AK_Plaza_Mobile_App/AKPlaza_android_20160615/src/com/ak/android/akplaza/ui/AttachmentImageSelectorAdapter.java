package com.ak.android.akplaza.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.ui.AttachmentImageSelectorAdapter.AttachmentListItem;



/**
 * An adapter to strings for image attachment type list.
 */
public class AttachmentImageSelectorAdapter extends ArrayAdapter<AttachmentListItem> {

    public final static int ADD_IMAGE = 0;
    public final static int TAKE_PICTURE = 1;

    protected LayoutInflater mInflater;
    private static final int mResource = android.R.layout.select_dialog_item;

    public AttachmentImageSelectorAdapter(Context context) {
        super(context, mResource, getData(context));
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text;
        View view;

        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
        } else {
            view = convertView;
        }

        // Set text field
        text = (TextView) view.findViewById(android.R.id.text1);
        text.setText(getItem(position).getTitle());

        return view;
    }

    public int buttonToCommand(int whichButton) {
        AttachmentListItem item = getItem(whichButton);
        return item.getCommand();
    }

    protected static List<AttachmentListItem> getData(Context context) {
        List<AttachmentListItem> data = new ArrayList<AttachmentListItem>(2);
        addItem(data, context.getString(R.string.attach_take_photo), TAKE_PICTURE);
        addItem(data, context.getString(R.string.attach_image), ADD_IMAGE);
        return data;
    }

    protected static void addItem(List<AttachmentListItem> data, String title, int command) {
        AttachmentListItem temp = new AttachmentListItem(title, command);
        data.add(temp);
    }

    public static class AttachmentListItem {

    	private String mTitle;
        private int mCommand;

        public AttachmentListItem(String title, int command) {
        	mTitle = title;
            mCommand = command;
        }

        public int getCommand() {
            return mCommand;
        }

        public String getTitle() {
        	return mTitle;
        }
    }
}