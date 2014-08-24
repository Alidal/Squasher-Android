package com.vladsid.squasher.app.news;

import android.graphics.Bitmap;
import java.io.Serializable;

public class NewsItem  implements Serializable {
	private String id;
	private String title;
	private String listPictureUrl;
	private Bitmap listPicture;
	private String fullPictureUrl;
	private Bitmap fullPicture;
	private String content;
	private String date;
	private String source;

	public NewsItem(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getListPictureUrl() {
		return listPictureUrl;
	}

	public void setListPictureUrl(String listPictureUrl) {
		this.listPictureUrl = listPictureUrl;
	}

	public Bitmap getListPicture() {
		return listPicture;
	}

	public void setListPicture(Bitmap listPicture) {
		this.listPicture = listPicture;
	}

	public String getFullPictureUrl() {
		return fullPictureUrl;
	}

	public void setFullPictureUrl(String fullPictureUrl) {
		this.fullPictureUrl = fullPictureUrl;
	}

	public Bitmap getFullPicture() {
		return fullPicture;
	}

	public void setFullPicture(Bitmap fullPicture) {
		this.fullPicture = fullPicture;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}