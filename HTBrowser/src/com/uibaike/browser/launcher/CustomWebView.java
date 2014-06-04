package com.uibaike.browser.launcher;

import com.uibaike.browser.utils.ApplicationUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 自定义webview
 * @author wangcheng/494518071@qq.com
 *
 */
public class CustomWebView extends WebView {
	public static final String URL_ABOUT_START = "about:start";
		
	private Context mContext;
	private int mProgress = 0;
	
	public CustomWebView(Context context) {
		super(context);
		mContext = context;
		initSetting();
	}

	public CustomWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initSetting();
	}
	
	public int getProgress() {
		return mProgress;
	}
	
	public void setProgress(int progress) {
		mProgress = progress;
	}
	
	/**
	 * 跳转到指定url
	 * @param url
	 */
	public void navigateToUrl(String url) {
		if (URL_ABOUT_START.equals(url)) {
			//Log.e("text", ApplicationUtils.getTextFromAssets(mContext, "homepage/home.html"));
			loadDataWithBaseURL("file:///android_asset/homepage/", ApplicationUtils.getTextFromAssets(mContext, "homepage/home.html"), 
					"text/html", "UTF-8", URL_ABOUT_START);
		} else {
			loadUrl(url);
		}
	}
	
	/*
	 * 初始化设置
	 */
	private void initSetting() {
		WebSettings setting = getSettings();
		
		setting.setJavaScriptEnabled(true);
		setting.setLoadsImagesAutomatically(true);
		
		setting.setSupportMultipleWindows(true);
		setting.setAppCacheEnabled(true);
		setting.setDatabaseEnabled(true);
		setting.setDomStorageEnabled(true);
		setLongClickable(true);
		setDrawingCacheEnabled(true);
		
		setWebViewClient(new WebViewClient());
		setWebChromeClient(new WebChromeClient());
	}
	
	@Override
	public void goBack() {	
//		Log.e("url", getUrl());
//		WebBackForwardList list = copyBackForwardList();		
		super.goBack();	
//		Log.e("url", getUrl());
//		list.getCurrentItem().getUrl();
//		if (list.getCurrentIndex() == 1) { //如果回退的是主页需要重新加载一次
//			navigateToUrl(URL_ABOUT_START);
//			Log.e("url", getUrl());
//		}		
	}
	
	/**
	 * 截取当前WebView的可视部分
	 * @return
	 */
	public Bitmap captureWebViewVisibleSize() {
		return getDrawingCache();
	}
	
	/**
	 * 截取当前WebView的整个加载内容
	 * @param webView
	 * @return
	 */
	private Bitmap captureWebView(WebView webView){  
	    Picture snapShot = capturePicture();  	      
	    Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),snapShot.getHeight(), Bitmap.Config.ARGB_8888);  
	    Canvas canvas = new Canvas(bmp);  
	    snapShot.draw(canvas);  
	    return bmp;  
	}  
}
