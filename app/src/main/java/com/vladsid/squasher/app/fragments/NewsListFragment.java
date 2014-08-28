package com.vladsid.squasher.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.vladsid.squasher.app.NewsDetailsActivity;
import com.vladsid.squasher.app.news.NewsItem;
import com.vladsid.squasher.app.news.NewsListAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NewsListFragment extends ListFragment {

	//news items
	private ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
	private ListView mNewsList;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mNewsList = getListView();

		String url = "http://213.111.120.224/news";
		new NewsDownloaderTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

		mNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = mNewsList.getItemAtPosition(position);
				NewsItem newsData = (NewsItem) o;

				Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
				intent.putExtra("news", newsData);
				startActivity(intent);
			}
		});
		BaseAdapter mAdapter = new NewsListAdapter(getActivity(), newsItems);
		mAdapter.notifyDataSetChanged();
		setListAdapter(mAdapter);
	}

	private void saveNewsToFile(String input) {
		try {
			// отрываем поток для записи
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getActivity().openFileOutput("topNews", Context.MODE_PRIVATE)));
			// пишем данные
			bw.write(input);
			// закрываем поток
			bw.close();
			Log.d("NewsSaver", "File has been written successfully");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseJson(InputStream is) {
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
					} else if (name.equals("article_picture")) {
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
				newsItems.add(item);
				reader.endObject();
			}
			reader.endArray();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * JSON READER CLASS
	 */

	private class NewsDownloaderTask extends AsyncTask<String, Void, Void> {
		@Override
		protected void onProgressUpdate(Void... values) {
		}

		@Override
		protected void onPostExecute(Void result) {
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
				String responseBody = EntityUtils.toString(httpEntity, HTTP.UTF_8);
				saveNewsToFile(responseBody);

				is = new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8));
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

	}
}
