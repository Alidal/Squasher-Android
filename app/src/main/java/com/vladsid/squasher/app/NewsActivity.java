package com.vladsid.squasher.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.vladsid.squasher.app.menu.*;
import com.vladsid.squasher.app.fragments.*;

import java.io.*;
import java.util.ArrayList;
import com.vladsid.squasher.app.news.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import static com.vladsid.squasher.app.FontOverrider.applyFont;


public class NewsActivity extends Activity {

	//news items
	private ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
	private ListView mNewsList;

	// slide menu items
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList <MenuItem> menuItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		//overriding system fonts
		applyFont(this, findViewById(R.id.news_main_layout), "fonts/MyriadPro-Light.ttf");

		//initialization of menu items
		initMenu();
		//initialization of news items
		if(newsItems.size() == 0)
			initNews();
	}

	/**
	 * INITIALIZATION FUNCTIONS
	 */

	public void initNews() {
		mNewsList = (ListView) findViewById(R.id.news_list);

		String url = "http://192.168.0.3/news";
		new JSONDownloaderTask().execute(url);
		NewsListAdapter mAdapter = new NewsListAdapter(this, newsItems);
		mNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = mNewsList.getItemAtPosition(position);
				NewsItem newsData = (NewsItem) o;

				Intent intent = new Intent(NewsActivity.this, NewsDetailsActivity.class);
				intent.putExtra("news", newsData);
				Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_LONG).show();
				startActivity(intent);
			}
		});
		mAdapter.notifyDataSetChanged();
		mNewsList.setAdapter(mAdapter);
	}

	private void initMenu(){
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.menu_items);
		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.menu_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList =   (ListView)     findViewById(R.id.list_slidermenu);

		menuItems = new ArrayList<MenuItem>();
		// adding nav drawer items to array
		// My Account
		menuItems.add(new MenuItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// News
		menuItems.add(new MenuItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Tournaments
		menuItems.add(new MenuItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Courts
		menuItems.add(new MenuItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Ratings
		menuItems.add(new MenuItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();
		// setting the nav drawer list adapter
		mDrawerList.setAdapter(new MenuListAdapter(getApplicationContext(), menuItems));

		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// display view for selected nav drawer item
				// update the main content by replacing fragments
				Fragment fragment = null;
				switch (position) {
					case 0:
						fragment = new AccountFragment();
						break;
					case 1:
						fragment = new NewsFragment();
						break;
					case 2:
						fragment = new TournamentsFragment();
						break;
					case 3:
						fragment = new CourtsFragment();
						break;
					case 4:
						fragment = new RatingsFragment();
						break;

					default:
						break;
				}

				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

					// update selected item and title, then close the drawer
					mDrawerList.setItemChecked(position, true);
					mDrawerList.setSelection(position);
					mDrawerLayout.closeDrawers();
				} else {
					// error in creating fragment
					Log.e("MainActivity", "Error in creating fragment");
				}
			}
		});
	}

	/**
	 * OTHER FUNCTIONS
	 */

	public void onSettingsClick(View v){
		Fragment fragment = new SettingsFragment();

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		mDrawerLayout.closeDrawers();
	}

	public void updateNewsList(ArrayList<NewsItem> list)
	{
		for(NewsItem x : list)
			newsItems.add(x);
	}

	/**
	 * JSON READER CLASS
	 */

	private class JSONDownloaderTask extends AsyncTask<String, Integer, Void> {
		private ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();
		@Override
		protected void onProgressUpdate(Integer... values) {
		}

		@Override
		protected void onPostExecute(Void result) {
			if (null != newsList) {
				updateNewsList(newsList);
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			String url = params[0];

			// getting JSON string from URL
			getJSONNewsFromUrl(url);
			return null;
		}


		public void getJSONNewsFromUrl(String url) {
			InputStream is = null;

			// Making HTTP request
			try {
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (is != null)
				parseJson(is);
		}

		public void parseJson(InputStream is) {
			try {
				JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					NewsItem item = new NewsItem();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equals("id")) {
							item.setId(reader.nextString());
						} else if (name.equals("title")) {
							item.setTitle(reader.nextString());
						} else if (name.equals("full_picture")) {
							item.setFullPictureUrl(reader.nextString());
						} else if (name.equals("list_picture")) {
							item.setListPictureUrl(reader.nextString());
						} else if (name.equals("text")) {
							item.setContent(reader.nextString());
						} else if (name.equals("source")) {
							item.setSource(reader.nextString());
						} else if (name.equals("date")) {
							item.setDate(reader.nextString());
						} else {
							reader.skipValue();
						}
					}
					newsList.add(item);
					reader.endObject();
				}
				reader.endArray();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
