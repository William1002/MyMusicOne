package com.example.mymusicone.view;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mymusicone.R;

public class MyAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Map<String, Object>> dataList;

	MyAdapter(Context ctx) {
		/*通过 LayoutInflater 的 from()方法可以构建出一个 LayoutInflater对象，
		然后调用 inflate()方法就可以动态加载一个布局文件，inflate()方法接收两个参数，第
		一个参数是要加载的布局文件的 id，这里我们传入 R.layout.title，第二个参数是给加载好
		的布局再添加一个父布局*/
		inflater = LayoutInflater.from(ctx);
		dataList = MusicInfo.GetMusicData(ctx);
	}

	// 总数
	@Override
	public int getCount() {
		return dataList.size();
	}

	// 按照索引获取值
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// 检索菜单项编号
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder = null;// 优化listview
		if (convertView == null) {
			view = inflater.inflate(R.layout.music_item, parent, false);
			holder = new ViewHolder();
			holder.tvName1 = (TextView) view.findViewById(R.id.tv_name1);
			holder.tvName2 = (TextView) view.findViewById(R.id.tv_name2);
			holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(holder);// 将ViewHolder存储在View中
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		holder.tvName1.setText("" + dataList.get(position).get("TITLE"));
		holder.tvName2.setText("" + dataList.get(position).get("ARTIST"));
		int min = (int) ((((Integer) dataList.get(position).get("DURATION")) / 1000) / 60);

		int sec = (((Integer) dataList.get(position).get("DURATION")) / 1000) % 60;
		if (sec < 10) {
			holder.tvTime.setText(min + ":0" + sec);
		} else {
			holder.tvTime.setText(min + ":" + sec);
		}

		return view;
	}

	// 优化listview而创建
	private static class ViewHolder {
		TextView tvName1, tvName2, tvTime;

	}

}
