package com.vladsid.squasher.app;

import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.vladsid.squasher.app.menu.*;
import com.vladsid.squasher.app.fragments.*;

import java.util.ArrayList;
import static com.vladsid.squasher.app.FontOverrider.applyFont;


public class MainActivity extends FragmentActivity {

	// slide menu items
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList <MenuItem> menuItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//overriding system fonts
		applyFont(this, findViewById(R.id.news_main_layout), "fonts/MyriadPro-Light.ttf");

		//initialization of menu items
		initMenu();
		mDrawerList.setItemChecked(1, true);

		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account);
		Object o = mDrawerList.getItemAtPosition(0);
		MenuItem menuData = (MenuItem) o;

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

	public void onSettingsClick(View v){
		Fragment fragment = new SettingsFragment();

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		mDrawerList.setItemChecked(mDrawerList.getCheckedItemPosition(), false);
		mDrawerLayout.closeDrawers();
	}


	public static Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),	bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

}
