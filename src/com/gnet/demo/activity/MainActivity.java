package com.gnet.demo.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import com.gnet.demo.R;
import com.gnet.demo.adapter.ContactorAdapter;
import com.gnet.demo.adapter.ContactorAdapter.ViewHolder;
import com.gnet.demo.adapter.HorizontalListViewAdapter;
import com.gnet.demo.biz.Contact;
import com.gnet.demo.util.PinyinUtil;
import com.gnet.demo.util.VerifyUtil;
import com.gnet.demo.view.DropDownListView;
import com.gnet.demo.view.DropDownListView.OnDropDownListener;
import com.gnet.demo.view.HorizontalListView;
import com.gnet.demo.view.SideBar;
import com.gnet.demo.view.SideBar.OnTouchingLetterChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements OnClickListener,
				OnTouchingLetterChangedListener, TextWatcher, 
				OnItemClickListener, OnDropDownListener
{

	 private String TAG = MainActivity.class.getSimpleName();      ///<该类名称
	 private ContactorAdapter mAdapter;                                                ///<BaseAdapter对象
	 private DropDownListView contactorList;                                                ///<联系人ListView对象
	 private AsyncQueryHandler asyncQuery;                                       ///<异步加载通讯录对象
	 private EditText mSearchEdit; //顶部搜索框 
	 private SideBar mSideBar; //右侧SideBar
	 private ImageButton mClearSearchText; // 清除搜索输入按钮
	 private ContentResolver resolver = null;
	 private ArrayList<Contact> selectedList = null;
	 private HorizontalListView hListView = null;
	 private HorizontalListViewAdapter hAdapter = null;
	 
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		selectedList = new ArrayList<Contact>();
	}

	
	private void initView()
	{
		// 设置标题栏
		TextView titleTv = (TextView) findViewById(R.id.common_title_tv);
		titleTv.setText("联系人列表");

		// 联系人显示列表
		contactorList = (DropDownListView)findViewById(R.id.common_list);
		contactorList.setOnItemClickListener(this);
		contactorList.setOnDropDownListener(this);
		contactorList.setDropDownStyle(true);
		contactorList.setItemsCanFocus(false);    
		contactorList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		// 右侧字母索引
		TextView dialog = (TextView)findViewById(R.id.common_sidbar_dialog);
		mSideBar = (SideBar)findViewById(R.id.common_sidrbar);
		mSideBar.setTextView(dialog);

		// 设置右侧触摸监听
		mSideBar.setOnTouchingLetterChangedListener(this);

		// 搜索输入框
		mSearchEdit = (EditText)findViewById(R.id.common_search_btn);

		// 清除搜索文字
		mClearSearchText = (ImageButton)findViewById(R.id.common_search_clear_btn);
		mClearSearchText.setVisibility(View.GONE);
		mClearSearchText.setOnClickListener(this);

		// 注册搜索事件
		mSearchEdit.addTextChangedListener(this);
		
		hListView = (HorizontalListView)findViewById(R.id.horizontal_list);

	}

	/**
     * @brief 初始化数据，加载通讯录
     */
	private void initData() 
    {
		resolver = getContentResolver();
		asyncQuery = new MyAsyncQueryHandler(resolver);  
    	// 联系人的Uri
        Uri uri = Uri.parse("content://com.android.contacts/data/phones");  
        // 查询的列
        String[] projection = {Phone.DISPLAY_NAME, Phone.NUMBER,Phone.SORT_KEY_PRIMARY, Photo.PHOTO_ID,Phone.CONTACT_ID };    
        // 按照sort_key升序查询
        asyncQuery.startQuery(0, null, uri, projection, null, null,"sort_key COLLATE LOCALIZED asc");
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		
	}


	@Override
	public void afterTextChanged(Editable s)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		if(null != mAdapter.getFilter())
		{
			mAdapter.getFilter().filter(s);
		}

		if(TextUtils.isEmpty(s))
		{
			mClearSearchText.setVisibility(View.GONE);
		} else
		{
			mClearSearchText.setVisibility(View.VISIBLE);
		}
	}


	/**
	 * 点击搜索框“清除”按钮
	 */
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.common_search_clear_btn:
			if(null != mSearchEdit)
			{
				mSearchEdit.setText("");
				closeKeyboard(mSearchEdit);
			}
			break;
		default:
			break;
		}
	}


	@Override
	public void onDropDown()
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTouchingLetterChanged(String s)
	{
		int position = mAdapter.getPositionForSection(s.charAt(0));
		if(position != -1)
		{
			contactorList.setSelection(position);
		}
	}
	
	 /**
     * @brief 关闭软键盘
     * @param view 要显示键盘的View
     */
    private void closeKeyboard(View view) 
    {  
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); 
    }
	
	/**
     * @brief 设置数据加载Adapter
     * @param list 数据列表
     */
    private void setAdapter(final List<Contact> list) 
    {  
    	mAdapter = new ContactorAdapter(this, list);  
        contactorList.setAdapter(mAdapter);  
        contactorList.setOnItemClickListener(new OnItemClickListener() 
        {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,long id) 
			{
			
				Contact contact = list.get(position - 1);
				
				ViewHolder vHollder = (ViewHolder) view.getTag();   
				//在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。    
				vHollder.checkedBox.toggle();  
				boolean checked = vHollder.checkedBox.isChecked();
				((ContactorAdapter) mAdapter).updateCheckState(position - 1,checked);
				((ContactorAdapter) mAdapter).notifyDataSetChanged();
				if(checked)
				{
					if(selectedList!= null && !selectedList.contains(contact))
					{
						selectedList.add(contact);
					}
				}else
				{
					if(selectedList!= null && selectedList.contains(contact))
					{
						selectedList.remove(contact);
					}	
				}
				hAdapter = new HorizontalListViewAdapter(MainActivity.this, selectedList);
				hAdapter.notifyDataSetChanged();
				hListView.setAdapter(hAdapter);
			}
        });
    }  
    
	   /** 
     * @brief 数据库异步查询类AsyncQueryHandler 
     */  
    private class MyAsyncQueryHandler extends AsyncQueryHandler {  
  
    	/**
    	 * @param cr ContentResolver对象
    	 */
        public MyAsyncQueryHandler(ContentResolver cr) {  
            super(cr);  
        }  
  
        /** 
         * @brief 查询结束的回调函数 
         * @param token  索引
         * @param cookie 缓存
         * @param cursor 数据列表
         */  
        @Override  
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) 
        {  
            if (cursor != null && cursor.getCount() > 0) 
            {  
                List<Contact> list = new ArrayList<Contact>(); 
                cursor.moveToFirst();  
                for (int i = 0; i < cursor.getCount(); i++) 
                {  
                    cursor.moveToPosition(i);  
                    
                    String name = cursor.getString(0);  
                    String number = cursor.getString(1); 
                    //String sortKey = cursor.getString(2);
                    //得到联系人头像ID  
                    Long photoid = cursor.getLong(3); 
                    long contactId = cursor.getLong(4);
                    Contact contact = new Contact();
                    contact.setUserName(name);
                    contact.setPhoneNum(number);
                    String pinyinName = PinyinUtil.hanziToPinyin(name);
                    contact.setFilterKey(PinyinUtil.hanziToPinyin(pinyinName)+ " " + name);
                    if(VerifyUtil.isNotNull(pinyinName) &&  Character.isLetter(pinyinName.charAt(0)))
                    {
                    	contact.setFirstLetter(pinyinName.toUpperCase(Locale.US).substring(0, 1));
                    }else
                    {
                    	contact.setFirstLetter("#");
                    }
                   
                      
                    //得到联系人头像Bitamp  
                    Bitmap contactPhoto = null;  
                 
                    //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的  
                    if(photoid > 0 ) 
                    {  
                        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);  
                        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);  
                        contactPhoto = BitmapFactory.decodeStream(input);  
                    }else 
                    {  
                        contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.icon_profile_default);  
                    }  
                    contact.setContactPhoto(contactPhoto);
                    list.add(contact);
                } 
                Collections.sort(list);
                if (list.size() > 0) 
                {  
                    setAdapter(list);
                }else
                {
                	Log.d(TAG, "手机通讯录为空!");
                }
                
            }else
            {
            	Log.d(TAG, "手机通讯录为空!");
            }
        }  
    }
}
