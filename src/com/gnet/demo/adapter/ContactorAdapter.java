/****************************************************************************************
 * Copyright (c) 2010~2012 All Rights Reserved by
 * G-Net Integrated Service Co., Ltd. 
 ****************************************************************************************/
package com.gnet.demo.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.gnet.demo.R;
import com.gnet.demo.biz.Contact;
import com.gnet.demo.util.ImageUtil;


/**   
 * @Title:  ContactorAdapter.java 
 * @author: wenhui.li 
 * @date:   2014-3-26 上午9:17:02   
 */
public class ContactorAdapter extends BaseAdapter implements SectionIndexer, Filterable
{

	private LayoutInflater mInflater;

	/** 原始数据 */
	private List<Contact> mContact;

	/** 搜索过滤数据 */
	private List<Contact> mUnfilteredData;
	
	/** 过滤对象 */
	private SimpleFilter mFilter;

	/** 选中对象 */
	private ArrayList<Boolean> mCheckedStates = null;
	/**
	 * 构造方法
	 * @param context
	 */
	public ContactorAdapter(Context context, List<Contact> contactorList)
	{
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContact = contactorList;
		this.mCheckedStates = new ArrayList<Boolean>();
		for (int i =0; i <mContact.size(); i++) 
		{ 
        	mCheckedStates.add(false);
        }  
	}
	
	@Override
	public int getCount()
	{
		if(null != mContact)
		{
			return mContact.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position)
	{
		if(null != mContact)
		{
			return mContact.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		if(null != mContact)
		{
			return mContact.get(position).getId();
		}
		return 0;
	}

	@Override
	public Filter getFilter()
	{
		if(mFilter == null)
		{
			mFilter = new SimpleFilter();
		}
		return mFilter;
	}

	@Override
	public int getPositionForSection(int section)
	{
		int len = mContact != null ? mContact.size() : 0;
		String firstLetter;
		for(int i = 0; i < len; i++)
		{
			firstLetter = mContact.get(i).getFirstLetter();
			if(!TextUtils.isEmpty(firstLetter))
			{
				char firstChar = firstLetter.toUpperCase(Locale.US).charAt(0);
				if(firstChar == section)
				{
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position)
	{
		if(null != mContact && null != mContact.get(position)
				&& !TextUtils.isEmpty(mContact.get(position).getFirstLetter()))
		{
			return mContact.get(position).getFirstLetter().toUpperCase(Locale.US).charAt(0);
		}
		return 0;
	}

	@Override
	public Object[] getSections()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @brief 对外提供更改选择条目CheckBox选中状态
	 * @param position 对应位置
	 * @param tag true表示选中，false表示否
	 */
    public void updateCheckState(int position,boolean tag)
    {
    	mCheckedStates.set(position, tag);
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if(null == convertView)
		{
			convertView = mInflater.inflate(R.layout.item_contact_list, null);
			if(null != convertView)
			{
				viewHolder = new ViewHolder();
				viewHolder.userPhoto = (ImageView) convertView.findViewById(R.id.iv_contact_photo);
				viewHolder.userName = (TextView) convertView.findViewById(R.id.tv_contact_name);
				viewHolder.userPhone = (TextView) convertView.findViewById(R.id.tv_contact_phone);
				viewHolder.userSection = (TextView) convertView.findViewById(R.id.tv_contactor_section);
				viewHolder.checkedBox=(CheckBox) convertView.findViewById(R.id.select_checked);
			}
			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 填充数据
		fillingData(position, viewHolder);
		viewHolder.checkedBox.setChecked(mCheckedStates.get(position));
		return convertView;
	}
	
	/**
	 * 填充数据
	 * @param position 第几行数据
	 * @param view 显示对象
	 */
	private void fillingData(int position, ViewHolder view)
	{
		if(null != mContact && null != view)
		{
			final Contact contact = mContact.get(position);
			if(null != contact)
			{
				// TODO:设置头像
				view.userPhoto.setImageBitmap(ImageUtil.toRoundCorner(contact.getContactPhoto(), 30));
				// 设置名称
				view.userName.setText(contact.getUserName());
				
				// 设置电话
				view.userPhone.setText(contact.getPhoneNum());

				// 处理section，如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
				int section = getSectionForPosition(position);
				if(position == getPositionForSection(section))
				{
					view.userSection.setVisibility(View.VISIBLE);
					view.userSection.setText(contact.getFirstLetter().toUpperCase(Locale.US));
				} else
				{
					view.userSection.setVisibility(View.GONE);
				}
			}
		}
	}
	
	/**
	 * 显示对象类
	 */
	public class ViewHolder
	{
		public TextView userName;
		public TextView userPhone;
		public ImageView userPhoto;
		public TextView userSection;
		public  CheckBox checkedBox;
	}
	
	/**
	 * 过滤类
	 */
	private class SimpleFilter extends Filter
	{
		@Override
		protected FilterResults performFiltering(CharSequence prefix)
		{
			FilterResults results = new FilterResults();

			if(mUnfilteredData == null)
			{
				mUnfilteredData = new ArrayList<Contact>(mContact);
			}

			if(prefix == null || prefix.length() == 0)
			{
				List<Contact> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			} else
			{
				String prefixString = prefix.toString().toLowerCase(Locale.US);

				List<Contact> unfilteredValues = mUnfilteredData;
				int count = unfilteredValues.size();

				ArrayList<Contact> newValues = new ArrayList<Contact>(count);

				for(int i = 0; i < count; i++)
				{
					Contact h = unfilteredValues.get(i);
					if(h != null)
					{
						String str = h.getFilterKey();

						String[] words = str.split(" ");
						int wordCount = words.length;

						for(int k = 0; k < wordCount; k++)
						{
							String word = words[k];

							if(word.toLowerCase(Locale.US).startsWith(prefixString))
							{
								if(!newValues.contains(h))
								{
									newValues.add(h);
								}
								break;
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			mContact = (List<Contact>) results.values;
			if(results.count > 0)
			{
				notifyDataSetChanged();
			} else
			{
				notifyDataSetInvalidated();
			}
		}
	}
}

