package com.keensoft.websitebook.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.keensoft.websitebook.R;
import com.keensoft.websitebook.adapter.CommonAdapter;
import com.keensoft.websitebook.adapter.ViewHolder;
import com.keensoft.websitebook.beans.FileItem;
import com.keensoft.websitebook.db.FaiCodeDatabase;

public class MainListFragment extends Fragment {

	public interface OnSelectedListener {
		void onSelect(String url);
	}

	private ListView list;
	private CommonAdapter<FileItem> mAdapter;

	private OnSelectedListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.fragment_top_level_demo, null);
		initWidgets(rootView);
		return rootView;
	}

	private void initWidgets(View rootView) {
		this.list = (ListView) rootView.findViewById(R.id.tv_top_level_list);
		Bundle bd = getArguments();

		list.setAdapter(mAdapter = new CommonAdapter<FileItem>(this
				.getActivity(), FaiCodeDatabase.getInstance(getActivity())
				.getFileItemList(bd.getInt("folderid")),
				R.layout.view_navi_drawer_item) {
			@Override
			public void convert(ViewHolder helper, FileItem item) {
				helper.setText(R.id.tv_navi_item_text, "" + item.getName());
				// helper.setText(R.id.tv_describe, item.getDesc());
				// helper.setText(R.id.tv_phone, item.getPhone());
				// helper.setText(R.id.tv_time, item.getTime());

				// helper.getView(R.id.tv_title).setOnClickListener(l)
			}

		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listener.onSelect(mAdapter.getItem(arg2).getPath());
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			listener = (OnSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnSelectedListener");
		}
	}

}
