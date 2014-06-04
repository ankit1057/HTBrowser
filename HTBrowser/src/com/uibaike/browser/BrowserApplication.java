package com.uibaike.browser;

import android.app.Application;
import android.widget.ViewFlipper;

/**
 * 应用全局类
 * @author wangcheng/494518071@qq.com
 *
 */
public class BrowserApplication extends Application {
	private static BrowserApplication instance;
	
	private ViewFlipper mTabList;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	public static BrowserApplication getInstance() {
		return instance;
	}
	
	public void setTabList(ViewFlipper viewFlipper) {
		mTabList = viewFlipper;
	}
	
	public ViewFlipper getTabList() {
		return mTabList;
	}
}
