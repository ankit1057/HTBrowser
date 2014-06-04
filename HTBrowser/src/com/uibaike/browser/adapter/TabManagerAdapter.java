package com.uibaike.browser.adapter;

import com.uibaike.browser.BrowserApplication;
import com.uibaike.browser.R;
import com.uibaike.browser.activity.BrowserTabManagerActivity;
import com.uibaike.browser.launcher.CustomWebView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * tab管理适配器
 * @author wangcheng/494518071@qq.com
 *
 */
public class TabManagerAdapter extends BaseAdapter {
	private ViewFlipper mViewFlipper;
	private LayoutInflater mInfalter;
	private Context mContext;
	
	public TabManagerAdapter(Context context, ViewFlipper viewFlipper) {
		mContext = context;
		mViewFlipper = viewFlipper;
		mInfalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mViewFlipper.getChildCount();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		
		if (convertView == null) {
			holder = new Holder();
			convertView = (LinearLayout) mInfalter.inflate(R.layout.tab_manager_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.tab_title);
			holder.close = (ImageView) convertView.findViewById(R.id.tab_close_btn);
			holder.thumb = (ImageView) convertView.findViewById(R.id.tab_thumb);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		CustomWebView webView = (CustomWebView) mViewFlipper.getChildAt(position);
		holder.title.setText(webView.getTitle());
		Log.e("title,count", webView.getTitle()+","+mViewFlipper.getChildCount());
		holder.thumb.setImageBitmap(webView.captureWebViewVisibleSize());
		
		/*关闭对应WebView*/
		holder.close.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (position == 0) {
					CustomWebView customWebView = new CustomWebView(BrowserApplication.getInstance());
					customWebView.navigateToUrl(CustomWebView.URL_ABOUT_START);
					mViewFlipper.removeAllViews();
					mViewFlipper.addView(customWebView);
					((BrowserTabManagerActivity) mContext).finish();
				} else {
					mViewFlipper.removeViewAt(position);
					notifyDataSetChanged();
				}				
			}
		});
		
		/*点击缩略图展示对应的WebView*/
		holder.thumb.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mViewFlipper.setDisplayedChild(position);
				((BrowserTabManagerActivity) mContext).finish();
			}
		});
		return convertView;
	}
	
	private class Holder {
		TextView title;
		ImageView close;
		ImageView thumb;
	}
}
