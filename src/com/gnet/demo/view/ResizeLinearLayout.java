package com.gnet.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 可监听布局大小变化的LinearLayout
 *
 */
public class ResizeLinearLayout extends LinearLayout
{

	public ResizeLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	private OnResizeListener mListener;

	public interface OnResizeListener
	{
		void onResize(int w, int h, int oldw, int oldh);
	}

	public void setOnResizeListener(OnResizeListener l)
	{
		mListener = l;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		if(mListener != null)
		{
			mListener.onResize(w, h, oldw, oldh);
		}
	}

}
