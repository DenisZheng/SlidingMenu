package edu.njupt.zhb.slidemenu;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.njupt.zhb.activity.BaseActivity;
import edu.njupt.zhb.bean.SenceInfo;
import edu.njupt.zhb.view.MyListView;
import edu.njupt.zhb.view.MyListView.OnRefreshListener;

public class SenceActivity  extends BaseActivity{
	private ArrayList<SenceInfo> data;
	private BaseAdapter adapter;
	public void InitActivity(Bundle savedInstanceState) {
		this.setContentView(R.layout.sence_main);
//		Button btnback=(Button)findViewById(R.id.btnback);
//		btnback.setFocusable(true);
//		btnback.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				SenceActivity.this.finish();
//				
//			}
//		});
//		   sdata=new SencesData(this);
//			data = new ArrayList<SenceInfo>();
//			data=sdata.query("");
//	        final int t=0;
//			final MyListView listView = (MyListView)findViewById(R.id.listView);
//			adapter = new BaseAdapter() {
//				public View getView(int position, View convertView, ViewGroup parent) {
//					SenceInfo info=new SenceInfo();
//					info=data.get(position);
//					 convertView=LayoutInflater.from(SenceActivity.this).inflate(R.layout.sence_item, null);
//					TextView textView = (TextView) convertView.findViewById(R.id.txtSencesName);
//					textView.setText(info.getName());
//					 ImageView img_ico=(ImageView)convertView.findViewById(R.id.sences_icon);
//					// img_ico.setImageBitmap(ImageUtil.ImgIcon(convertView.getContext(),info.getUiImg()));
//					return convertView;
//				}
//
//				public long getItemId(int position) {
//					return position;
//				}
//
//				public Object getItem(int position) {
//					return data.get(position);
//				}
//
//				public int getCount() {
//					return data.size();
//				}
//			};
//			listView.setAdapter(adapter);
//
//			listView.setonRefreshListener(new OnRefreshListener() {
//				public void onRefresh() {
//					new AsyncTask<Void, Void, Void>() {
//						protected Void doInBackground(Void... params) {
//							try {
//								Thread.sleep(1000);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							//data.addFirst("刷新后的内容");
//							return null;
//						}
//
//						@Override
//						protected void onPostExecute(Void result) {
//							adapter.notifyDataSetChanged();
//							listView.onRefreshComplete();
//						}
//
//					}.execute(null,null,null);
//				}
//			});
	}
}
