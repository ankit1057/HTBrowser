package com.uibaike.browser.launcher.menu;

import java.util.ArrayList;

import com.uibaike.browser.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �����˵�
 * @author wangcheng/494518071@qq.com
 *
 */
public class PopupMenu extends PopupWindow {
	private Context mContext;
	private ImageView mCursor;
	private int mCursorWidth;

	public PopupMenu(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	private void init() {
		/*���л�ȡ�ؼ�*/
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout popupMenu = (LinearLayout) inflater.inflate(R.layout.popup_menu, null);
		
		final ViewPager menuViewPager = (ViewPager) popupMenu.findViewById(R.id.menu_viewpager);
		mCursor = (ImageView) popupMenu.findViewById(R.id.cursor);
		LinearLayout cursorBg = (LinearLayout) popupMenu.findViewById(R.id.cursor_bg);
		TextView normalTitle = (TextView) popupMenu.findViewById(R.id.normal_title);
		TextView settingTitle = (TextView) popupMenu.findViewById(R.id.setting_title);
		TextView toolTitle = (TextView) popupMenu.findViewById(R.id.tool_title);
		
		/*��viewPageָʾ�����д���*/
		cursorBg.setBackgroundColor(Color.DKGRAY);
		mCursor.setImageDrawable(new ColorDrawable(Color.WHITE));
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mCursorWidth = windowManager.getDefaultDisplay().getWidth()/3;
		LayoutParams lp = mCursor.getLayoutParams();
		lp.width = mCursorWidth;
		
		/*����ѡ�������*/
		normalTitle.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				menuViewPager.setCurrentItem(0, true);
			}
		});
		settingTitle.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				menuViewPager.setCurrentItem(1, true);
			}
		});
		toolTitle.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				menuViewPager.setCurrentItem(2, true);
			}
		});
		
		MenuData menuData = new MenuData(mContext);
		ArrayList<View> arrayList = new ArrayList<View>();
		/*����*/
		GridView gridViewNormal = (GridView) inflater.inflate(R.layout.menu_gridview, null);
		gridViewNormal.setAdapter(
				new MenuAdapter(mContext, menuData.getMenuList(MenuData.MENU_TYPE_NORMAL)));
		gridViewNormal.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(mContext, "����Գ����լ...", Toast.LENGTH_LONG).show();
			}
		});
		arrayList.add(gridViewNormal);
		
		/*����*/
		GridView gridViewSetting = (GridView) inflater.inflate(R.layout.menu_gridview, null);
		gridViewSetting.setAdapter(
				new MenuAdapter(mContext, menuData.getMenuList(MenuData.MENU_TYPE_SETTING)));
		gridViewSetting.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(mContext, "����Գ����լ...", Toast.LENGTH_LONG).show();
			}
		});
		arrayList.add(gridViewSetting);
		
		/*����*/
		GridView gridViewTool = (GridView) inflater.inflate(R.layout.menu_gridview, null);
		gridViewTool.setAdapter(
				new MenuAdapter(mContext, menuData.getMenuList(MenuData.MENU_TYPE_TOOL)));
		gridViewTool.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(mContext, "����Գ����լ...", Toast.LENGTH_LONG).show();
			}
		});
		arrayList.add(gridViewTool);
		
		/*����ViewPage���������ͼ���*/
		menuViewPager.setAdapter(new MenuViewPagerAdapter(arrayList));
		menuViewPager.setOnPageChangeListener(new MenuOnPageChangeListener());
		
		/*����popup menu��ʽ*/
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.popupmenu_height));
		setFocusable(true);
		//setBackgroundDrawable(new ColorDrawable(0x00000000));
		setAnimationStyle(android.R.style.Animation_Toast);
		setContentView(popupMenu);	
		
		/*
		 * ������menu��֮���ٴε���޷���Ӧ������
		 * http://blog.csdn.net/admin_/article/details/7278402
		 * */
		popupMenu.setFocusableInTouchMode(true);  
		popupMenu.setOnKeyListener(new OnKeyListener() {  
	        @Override  
	        public boolean onKey(View v, int keyCode, KeyEvent event) {  
	            if (keyCode == KeyEvent.KEYCODE_MENU && isShowing()) {  
	                dismiss();
	                return true;  
	            }  
	            return false;  
	        }  
	    });  
	}
	
	private class MenuViewPagerAdapter extends PagerAdapter {
		public ArrayList<View> arrayList;

		public MenuViewPagerAdapter(ArrayList<View> arrayList) {
			this.arrayList = arrayList;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(arrayList.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(arrayList.get(arg1), 0);
			return arrayList.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
	
	private class MenuOnPageChangeListener implements OnPageChangeListener {
		private float startOffset = 0.0f;
		private float endOffset = 0.0f;
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			endOffset = position*mCursorWidth + mCursorWidth*positionOffset;
			
			Animation  animation = new TranslateAnimation(startOffset, endOffset, 0, 0);
			animation.setFillAfter(true);
			animation.setDuration(0);
			mCursor.startAnimation(animation);
			
			startOffset = endOffset;
		}

		@Override
		public void onPageSelected(int arg0) {
			
		}
	}
}
