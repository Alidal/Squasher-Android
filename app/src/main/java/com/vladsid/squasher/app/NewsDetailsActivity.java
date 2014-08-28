package com.vladsid.squasher.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.vladsid.squasher.app.asyncTask.ImageDownloaderTask;
import com.vladsid.squasher.app.news.NewsItem;

import static com.vladsid.squasher.app.FontOverrider.applyFont;

public class NewsDetailsActivity extends Activity {

	private NewsItem news;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_details);

		news = (NewsItem) this.getIntent().getSerializableExtra("news");
		applyFont(this, findViewById(R.id.news_details_layout), "fonts/MyriadPro-Light.ttf");

		if (null != news) {
			ImageView thumb = (ImageView) findViewById(R.id.newsFullPicture);
			new ImageDownloaderTask(thumb, "square").execute("http://213.111.120.224/img/" + news.getListPictureUrl());

			TextView title = (TextView) findViewById(R.id.newsFullTitle);
			title.setText(news.getTitle());
			TextView date = (TextView) findViewById(R.id.newsFullDate);
			date.setText(news.getDate());
			TextView source = (TextView) findViewById(R.id.newsFullSource);
			source.setText(news.getSource());
			TextView content = (TextView) findViewById(R.id.newsFullContent);
			content.setText(news.getContent());
		}
	}

	public void onBackButtonClick(View v){
		Intent intent = new Intent(NewsDetailsActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
}
