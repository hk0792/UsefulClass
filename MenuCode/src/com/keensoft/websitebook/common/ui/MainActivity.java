package com.keensoft.websitebook.common.ui;

import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.keensoft.websitebook.R;
import com.keensoft.websitebook.adapter.CommonAdapter;
import com.keensoft.websitebook.adapter.ViewHolder;
import com.keensoft.websitebook.beans.FolderItem;
import com.keensoft.websitebook.db.FaiCodeDatabase;
import com.keensoft.websitebook.fragment.MainListFragment;
import com.keensoft.websitebook.fragment.MainListFragment.OnSelectedListener;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends ActionBarActivity implements
		OnSelectedListener {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private View left;

	private String[] mNaviItemText;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;

	private CommonAdapter<FolderItem> mAdapter;
	private List<FolderItem> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initDrawerLayout();
		selectNaviItem(0);
		UmengUpdateAgent.update(this);
	}

	@Override
	public void onSelect(String url) {
		// TODO Auto-generated method stub
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	private void initDrawerLayout() {
		mNaviItemText = getResources().getStringArray(R.array.navi_items);
		mDrawerTitle = getResources().getString(R.string.app_name);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		left = findViewById(R.id.id_left_menu);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		data = FaiCodeDatabase.getInstance(this).getFolderItemList();
		mAdapter = new CommonAdapter<FolderItem>(this, data,
				R.layout.view_navi_drawer_item) {

			@Override
			public void convert(ViewHolder helper, FolderItem file) {
				helper.setText(R.id.tv_navi_item_text, file.getName());
			}
		};

		mDrawerList.setAdapter(mAdapter);
		mDrawerList
				.setOnItemClickListener(new NaviDrawerListItemOnClickListner());

		initialDrawerListener();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	private void initialDrawerListener() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				// getSupportActionBar().setTitle(mDrawerTitle);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// getSupportActionBar().setTitle(mTitle);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private class NaviDrawerListItemOnClickListner implements
			OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectNaviItem(position);
		}
	}

	private void selectNaviItem(int position) {
		Fragment topLevelFrgmt = null;
		topLevelFrgmt = new MainListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("folderid", mAdapter.getItem(position).getId());
		topLevelFrgmt.setArguments(bundle);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.main_content, topLevelFrgmt);
		ft.commit();

		mDrawerList.setItemChecked(position, true);
		setTitle(mNaviItemText[position]);
		mDrawerLayout.closeDrawer(left);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		//
		return super.onOptionsItemSelected(item);
	}
}
