package edu.njupt.zhb.slidemenu;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.bean.SenceInfo;
import edu.njupt.zhb.utils.ImageUtil;
import edu.njupt.zhb.view.MyListView;
import edu.njupt.zhb.view.MyListView.OnRefreshListener;
/**
 * ListView
 * @author Administrator
 *
 */
public class FragmentTimes extends Fragment{
	private ArrayList<DeviceInfo> data;
	private BaseAdapter adapter;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_times, container, false);
//		data = new ArrayList<DeviceInfo>();
//		data=sdata.query("");
//
//		final MyListView listView = (MyListView) view.findViewById(R.id.listView);
//		adapter = new BaseAdapter() {
//			public View getView(int position, View convertView, ViewGroup parent) {
//				DeviceInfo info=new DeviceInfo();
//				info=data.get(position);
//				 convertView=LayoutInflater.from(getActivity()).inflate(R.layout.times_item, null);
//				 ImageView img_ico=(ImageView)convertView.findViewById(R.id.times_icon);
//				 img_ico.setImageBitmap(ImageUtil.ImgIcon(convertView.getContext(),"icon_"+info.getUiCode().toLowerCase()+"_on"));
//				TextView textView = (TextView) convertView.findViewById(R.id.txtTimeName);
//				textView.setText(info.getName());
//				ImageButton btn=(ImageButton)convertView.findViewById(R.id.btnTimesSet);
//				btn.setTag(info);
//				btn.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						DeviceInfo info=(DeviceInfo)v.getTag();
//						Intent intent=new Intent();
//						Bundle bunlde=new Bundle();
//						bunlde.putString("devId", info.getId());
//						bunlde.putString("devType",info.getDevNum());
//						bunlde.putString("devUi", info.getUiCode());
//						bunlde.putString("OtherName", info.getOtherName());
//						intent.setClass(v.getContext(), TimesMainActivity.class);
//						intent.putExtras(bunlde);
//						getActivity().startActivity(intent);
//					}
//				});
//				return convertView;
//			}
//
//			public long getItemId(int position) {
//				return position;
//			}
//
//			public Object getItem(int position) {
//				return data.get(position);
//			}
//
//			public int getCount() {
//				return data.size();
//			}
//		};
//		listView.setAdapter(adapter);
//
//		listView.setonRefreshListener(new OnRefreshListener() {
//			public void onRefresh() {
//				new AsyncTask<Void, Void, Void>() {
//					protected Void doInBackground(Void... params) {
//						try {
//							Thread.sleep(1000);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						//data.addFirst("刷新后的内容");
//						return null;
//					}
//
//					@Override
//					protected void onPostExecute(Void result) {
//						adapter.notifyDataSetChanged();
//						listView.onRefreshComplete();
//					}
//
//				}.execute(null,null,null);
//			}
//		});
    	return view;
    }
}
