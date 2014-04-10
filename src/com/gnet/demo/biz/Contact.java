/****************************************************************************************
 * Copyright (c) 2010~2012 All Rights Reserved by
 * G-Net Integrated Service Co., Ltd. 
 ****************************************************************************************/
package com.gnet.demo.biz;

import java.io.Serializable;

import android.graphics.Bitmap;

/**   
 * @Title:  Contactor.java 
 * @author: wenhui.li 
 * @date:   2014-3-26 上午10:24:25   
 */
public class Contact implements Serializable, Comparable<Contact> 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 事件id
	 */
	private int id;
	/**
	 * 联系人姓名
	 */
	private String userName = null;
	
	/**
	 * 联系人电话
	 */
	private String phoneNum = null;

	/**
	 * 搜索关键字
	 * 
	 * 多个搜索词汇按空格隔开，中文词汇请转换为拼音。
	 */
	private String filterKey;
	
	/**
	 * 名称的第一个字母
	 * 
	 * 用于对列表进行分组，英文名称直接使用第一个字母，中文名称，转换为拼音后使用第一个字母。
	 */
	private String firstLetter;
	
	/**
	 * 联系人头像
	 */
	private Bitmap contactPhoto; 
	
	public Bitmap getContactPhoto()
	{
		return contactPhoto;
	}

	public void setContactPhoto(Bitmap contactPhoto)
	{
		this.contactPhoto = contactPhoto;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPhoneNum()
	{
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}

	/**
	 * 设置名称的第一个字母
	 * @param firstLetter
	 */
	public void setFirstLetter(String firstLetter)
	{
		this.firstLetter = firstLetter;
	}
	
	/**
	 * 获取名称的第一个字母
	 * @return
	 */
	public String getFirstLetter()
	{
		return firstLetter;
	}
	
	/**
	 * 设置关键字
	 * @param filterKey 关键字字串，多个关键字使用空格分隔，中文请转换为拼音。
	 */
	public void setFilterKey(String filterKey)
	{
		this.filterKey = filterKey;
	}
	
	/**
	 * 获取关键字
	 * @return String类型的关键字字串，用空格分隔。
	 */
	public String getFilterKey()
	{
		return filterKey;
	}
	
	 /**
     * 进行对象比较
     * @param arg0 要比较的对象
     * @return 比较结果
     */
    public int compareTo(Contact arg0) {
    	return this.getFilterKey().compareTo(arg0.getFilterKey());
    }
}

