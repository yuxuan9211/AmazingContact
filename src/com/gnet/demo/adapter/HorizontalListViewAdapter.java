package com.gnet.demo.adapter;

import java.util.ArrayList;
import com.gnet.demo.R;
import com.gnet.demo.biz.Contact;
import com.gnet.demo.util.ImageUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HorizontalListViewAdapter extends BaseAdapter {

	private ArrayList<Contact> selectedList = null;
	
	public HorizontalListViewAdapter(Context con, ArrayList<Contact> list) {
		mInflater = LayoutInflater.from(con);
		selectedList = list;
	}

	@Override
	public int getCount() {
		return selectedList.size();
	}

	private LayoutInflater mInflater;

	@Override
	public Object getItem(int position) {
		return position;
	}

	private ViewHolder vh = new ViewHolder();

	private static class ViewHolder {

		private ImageView icon;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null) 
		{
			convertView = mInflater.inflate(R.layout.horizontallistview_item,null);
			vh.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			convertView.setTag(vh);
		} else 
		{
			vh = (ViewHolder) convertView.getTag();
		}
		Contact contact = selectedList.get(position);
		vh.icon.setImageBitmap(ImageUtil.toRoundCorner(contact.getContactPhoto(), 30));
		return convertView;
	}
}