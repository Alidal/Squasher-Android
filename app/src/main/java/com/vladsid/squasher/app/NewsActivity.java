package com.vladsid.squasher.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.vladsid.squasher.app.menu.MenuItem;
import com.vladsid.squasher.app.menu.MenuListAdapter;
import com.vladsid.squasher.app.fragments.*;
import java.util.ArrayList;
import static com.vladsid.squasher.app.FontOverrider.applyFont;


public class NewsActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList <MenuItem> menuItems;
	private MenuListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		//initialization of menu items
		initMenu();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
		                        long id) {
			// display view for selected nav drawer item
			displayView(position);
		}


		/**
		 * Displaying fragment view for selected nav drawer list item
		 */
		private void displayView(int position) {
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
	}

	private void initMenu(){
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		//overriding system fonts
		applyFont(this, findViewById(R.id.news_main_layout), "fonts/MyriadPro-Light.ttf");

		applyFont(this, findViewById(R.id.courts_layout), "fonts/MyriadPro-Light.ttf");
		applyFont(this, findViewById(R.id.ratings_layout), "fonts/MyriadPro-Light.ttf");
		applyFont(this, findViewById(R.id.tournaments_layout), "fonts/MyriadPro-Light.ttf");


		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

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
		adapter = new MenuListAdapter(getApplicationContext(), menuItems);
		mDrawerList.setAdapter(adapter);
	}

}
