package com.vladsid.squasher.app.news;

import android.graphics.Bitmap;
import android.graphics.Typeface;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.vladsid.squasher.app.*;
import com.vladsid.squasher.app.asyncTask.ImageDownloaderTask;

import static com.vladsid.squasher.app.MainActivity.getCroppedBitmap;

public class NewsListAdapter extends BaseAdapter {
	private ArrayList<NewsItem> listData;
	private LayoutInflater layoutInflater;
	private Typeface tf;

	public NewsListAdapter(Context context, ArrayList<NewsItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		tf = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Light.ttf");
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.news_item, null);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.news_title);
			holder.content = (TextView) convertView.findViewById(R.id.news_content);
			holder.date = (TextView) convertView.findViewById(R.id.news_date);
			holder.source = (TextView) convertView.findViewById(R.id.news_source);
			holder.img = (ImageView) convertView.findViewById(R.id.news_list_image);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		NewsItem newsItem = listData.get(position);

		holder.title.setText(newsItem.getTitle());
		holder.content.setText(newsItem.getContent());
		holder.date.setText(newsItem.getDate());
		holder.source.setText(newsItem.getSource());

		if (holder.img != null && holder.img.getDrawable() == null) {
			new ImageDownloaderTask(holder.img, "circle").execute("http://213.111.120.224/img/" + newsItem.getListPictureUrl());
		}

		//setting custom typeface
		holder.title.setTypeface(tf);
		holder.content.setTypeface(tf);
		holder.date.setTypeface(tf);
		holder.source.setTypeface(tf);

		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView content;
		TextView date;
		TextView source;
		ImageView img;
	}
}