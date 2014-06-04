package com.uibaike.browser.activity;

import com.uibaike.browser.BrowserApplication;
import com.uibaike.browser.R;
import com.uibaike.browser.launcher.CustomWebView;
import com.uibaike.browser.launcher.Launcher;
import com.uibaike.browser.launcher.menu.PopupMenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebView.HitTestResult;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * 主界面
 * @author wangcheng/494518071@qq.com
 *
 */
public class MainActivity extends Activity {
	private Launcher mLauncher;
	private ViewFlipper mViewFlipper;
	private PopupMenu mPopupMenu;	
	
	private AutoCompleteTextView mInputUrl;
	private ImageView mSearchIv;
	private ImageView mStopSearchIv;
	private ProgressBar mWebViewPb;
	
	private ImageButton mPopupMenuBtn;
	private FrameLayout mTabNumBtn;
	private TextView mTabNumTv;
	private ImageButton mPrePageIb;
	private ImageButton mNextPageIb;	
	private ImageButton mHomeIb;
	
	private ImageView mPageCount;
	
	private CustomWebView mCurrentWebView;
	private boolean mIsWebViewHome = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		//隐藏软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		mLauncher = (Launcher) findViewById(R.id.launcher);
		mLauncher.setOnPageChangeListener(new MainPageChangeListener());		
		mViewFlipper = (ViewFlipper) mLauncher.getPageList().get(0);
		mTabNumBtn = (FrameLayout) findViewById(R.id.tab_num_btn);
		mTabNumTv = (TextView) findViewById(R.id.tab_num_main);
		mPrePageIb = (ImageButton) findViewById(R.id.pre_page);
		mNextPageIb = (ImageButton) findViewById(R.id.next_page);
		mHomeIb = (ImageButton) findViewById(R.id.home_btn);
		mInputUrl = (AutoCompleteTextView) findViewById(R.id.input_url);
		mSearchIv = (ImageView) findViewById(R.id.search_btn);
		mStopSearchIv = (ImageView) findViewById(R.id.stop_search_btn);
		mWebViewPb = (ProgressBar) findViewById(R.id.WebViewProgress);
		mPageCount = (ImageView) findViewById(R.id.page_count);
		
		/*搜索*/
		mSearchIv.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				mCurrentWebView.navigateToUrl(mInputUrl.getText().toString());
				v.setVisibility(View.GONE);
				mStopSearchIv.setVisibility(View.VISIBLE);
			}
		});
		
		/*中断搜索*/
		mStopSearchIv.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mCurrentWebView.stopLoading();
				v.setVisibility(View.GONE);
				mSearchIv.setVisibility(View.VISIBLE);
			}
		});
		
		/*上一页*/
		mPrePageIb.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (mCurrentWebView.canGoBack()) {
					mCurrentWebView.goBack();
					mInputUrl.setText(mCurrentWebView.getUrl());
				}				
			}
		});
		
		/*下一页*/
		mNextPageIb.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (mCurrentWebView.canGoForward()) {
					mCurrentWebView.goForward();
					mInputUrl.setText(mCurrentWebView.getUrl());
				}				
			}
		});
		
		/*弹出菜单按钮*/
		mPopupMenuBtn = (ImageButton) findViewById(R.id.popup_menu_btn);
		mPopupMenuBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (mPopupMenu == null) {
					mPopupMenu = new PopupMenu(MainActivity.this);
				}
				if (mPopupMenu.isShowing()) {
					mPopupMenu.dismiss();
				} else {
					mPopupMenu.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 60);
				}			
			}
		});
		
		/*主页*/
		mHomeIb.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mCurrentWebView.clearHistory();
				mCurrentWebView.navigateToUrl(CustomWebView.URL_ABOUT_START);
			}
		});
		
		/*多窗口tab按钮*/
		mTabNumBtn.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				mCurrentWebView.setWebChromeClient(null);
				mCurrentWebView.setWebViewClient(null);
				Intent intent = new Intent(MainActivity.this, BrowserTabManagerActivity.class);
				startActivityForResult(intent, 0x00);
			}
		});
		
		//增加主页Tab
		mCurrentWebView = new CustomWebView(this);
		mCurrentWebView.navigateToUrl(CustomWebView.URL_ABOUT_START);
		mViewFlipper.addView(mCurrentWebView);	
		
		//设置浏览器tab到全局数据
		BrowserApplication.getInstance().setTabList(mViewFlipper);
		
		//设置当前WebView
		mWebViewPb.setProgress(0);
		setCurrentWebView();
	}
	
	private void setCurrentWebView() {
		mWebViewPb.setProgress(mCurrentWebView.getProgress());
		mCurrentWebView.setWebViewClient(new MyWebViewClient());
		mCurrentWebView.setWebChromeClient(new MyWebChromeClient());
		
//		if (mCurrentWebView.getUrl() == null 
//				|| "file:///android_asset/startpage/".equals(mCurrentWebView.getUrl())) {
//			mIsWebViewHome = true;
//			mPageCount.setVisibility(View.VISIBLE);
//			mLauncher.setEnabled(true);
//		} else {
//			mIsWebViewHome = false;
//			mPageCount.setVisibility(View.GONE);
//			mLauncher.setEnabled(false);
//		}
		mCurrentWebView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				HitTestResult result = ((WebView) v).getHitTestResult();
				
				int resultType = result.getType();
				if ((resultType == HitTestResult.ANCHOR_TYPE) ||
						(resultType == HitTestResult.IMAGE_ANCHOR_TYPE) ||
						(resultType == HitTestResult.SRC_ANCHOR_TYPE) ||
						(resultType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE)) {
					Toast.makeText(MainActivity.this, "上下文菜单", Toast.LENGTH_LONG).show();								
				} else if (resultType == HitTestResult.IMAGE_TYPE) {					
				} else if (resultType == HitTestResult.EMAIL_TYPE) {
									
				}
			}
    		
    	});  	
	}
	
	/*
	 * lanucher视图切换监听
	 */
	private class MainPageChangeListener implements OnPageChangeListener {
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			mPageCount.setImageLevel(arg0);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu"); //加一个空菜单
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (mPopupMenu == null) {
			mPopupMenu = new PopupMenu(MainActivity.this);
		}
		if (mPopupMenu.isShowing()) {
			mPopupMenu.dismiss();
		} else {
			mPopupMenu.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 60);
		}	
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x00) {
			mTabNumTv.setText(mViewFlipper.getChildCount()+"");
			mCurrentWebView = (CustomWebView) mViewFlipper.getCurrentView();
			setCurrentWebView();
		}
	}
	
	/*
	 * 
	 */
	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {			
			mSearchIv.setVisibility(View.GONE);
			mStopSearchIv.setVisibility(View.VISIBLE);
			/*
			 * 对于主页使用WebView.loadDataWithBaseURL时的url是file:///android_asset/homepage/，
			 * 但是当前进/后退历史记录时url是about:start
			 */
			if (CustomWebView.URL_ABOUT_START.equals(url)) { //前进后退需要重新load
				mCurrentWebView.navigateToUrl(CustomWebView.URL_ABOUT_START);
			} 
			if ("file:///android_asset/homepage/".equals(url)) { //第一次加载主页需要转换url现实
				mInputUrl.setText(CustomWebView.URL_ABOUT_START);
			} else {
				mInputUrl.setText(url);
			}
			
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mWebViewPb.setProgress(0);
			mSearchIv.setVisibility(View.VISIBLE);
			mStopSearchIv.setVisibility(View.GONE);
		}
	}
	
	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {			
			super.onProgressChanged(view, newProgress);
			mCurrentWebView.setProgress(newProgress);
			mWebViewPb.setProgress(newProgress);
		}		
	}	
}
