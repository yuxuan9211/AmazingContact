/****************************************************************************************
 * Copyright (c) 2010~2012 All Rights Reserved by
 * G-Net Integrated Service Co., Ltd. 
 ****************************************************************************************/
package com.gnet.demo.util;

/**   
 * @Title:  VerifyUtil.java 
 * @author: wenhui.li 
 * @date:   2014-3-26 下午3:01:27   
 */
public class VerifyUtil
{

	/**
	 * @brief 判断字符串全为数字
	 * @return boolean true表示是，false表示否
	 */
	public static boolean isNumeric(String str)
	{
		for (int i = str.length(); --i >= 0;)
		{
			if (!Character.isDigit(str.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @brief 判读输入的字符串参数是否为空
	 * @param str 输入的字符参数
	 * @return 返回字符串是否为空 true：字符串为空 false：字符串不为空
	 */
	public static boolean isNotNull(String str)
	{
		if(str != null && str.length() != 0)
		{
			return true;
		} else
		{
			return false;
		}	
	}
		
}

