package com.vladsid.squasher.app.menu;

public class MenuItem {

	private String title;
	private int icon;
	private String count = "0";
	// boolean to set visibility of the counter
	private boolean isCounterVisible = false;

	public MenuItem(){}

	public MenuItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}

	public MenuItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	public String getTitle(){
		return this.title;
	}

	public int getIcon(){
		return this.icon;
	}

	public String getCount(){
		return this.count;
	}

	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}

	public void setCount(String count){
		this.count = count;
	}

	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}
