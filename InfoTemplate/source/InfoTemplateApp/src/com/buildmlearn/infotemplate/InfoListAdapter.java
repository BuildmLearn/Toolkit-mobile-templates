package com.buildmlearn.infotemplate;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<InfoModel> mList;

	public InfoListAdapter(Context context) {
		mList = new ArrayList<InfoModel>();
		mContext = context;
	}

	public void setList(ArrayList<InfoModel> list) {
		mList = list;
		this.notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.app_list_item, parent,
					false);

			holder = new ViewHolder();
			holder.mTv_Info_Object = (TextView) convertView
					.findViewById(R.id.tv_info_object);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTv_Info_Object.setTag(R.id.tv_info_object);
		holder.mTv_Info_Object.setText(mList.get(position).getInfo_object());

		return convertView;
	}

	public class ViewHolder {
		private TextView mTv_Info_Object;
	}

}
