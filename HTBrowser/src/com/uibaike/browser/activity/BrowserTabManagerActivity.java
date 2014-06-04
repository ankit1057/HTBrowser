package com.uibaike.browser.activity;

import com.uibaike.browser.BrowserApplication;
import com.uibaike.browser.R;
import com.uibaike.browser.adapter.TabManagerAdapter;
import com.uibaike.browser.launcher.CustomWebView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * 浏览器多标签管理
 * @author wangcheng/494518071@qq.com
 *
 */
public class BrowserTabManagerActivity extends Activity {
	private ListView mTabLv;
	private TextView mNewTabTv;
	private TextView mClearTabTv;
	private TextView mTabNumTv;
	
	private ViewFlipper mViewFlipper;
	private TabManagerAdapter mTabManagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_manager);
		
		mViewFlipper = BrowserApplication.getInstance().getTabList();
		
		mTabLv = (ListView) findViewById(R.id.tab_list);
		mNewTabTv = (TextView) findViewById(R.id.new_tab);
		mClearTabTv = (TextView) findViewById(R.id.clear_tab);
		mTabNumTv = (TextView) findViewById(R.id.tab_num);
		
		//设置缩略图列表
		mTabManagerAdapter = new TabManagerAdapter(this, mViewFlipper);
		mTabLv.setAdapter(mTabManagerAdapter);
		
		//设置下方tab数
		mTabNumTv.setText(mViewFlipper.getChildCount()+"");
		mTabNumTv.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mNewTabTv.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				CustomWebView customWebView = new CustomWebView(BrowserApplication.getInstance());
				customWebView.navigateToUrl(CustomWebView.URL_ABOUT_START);
				mViewFlipper.addView(customWebView);
				mViewFlipper.setDisplayedChild(mViewFlipper.getChildCount()-1);
				mTabNumTv.setText(mViewFlipper.getChildCount()+"");
				finish();
			}
		});
		
		mClearTabTv.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				CustomWebView customWebView = new CustomWebView(BrowserApplication.getInstance());
				customWebView.navigateToUrl(CustomWebView.URL_ABOUT_START);
				mViewFlipper.removeAllViews();
				mViewFlipper.addView(customWebView);
				finish();
			}
		});	
	}
	
}
