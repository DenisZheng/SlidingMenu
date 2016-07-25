package edu.njupt.zhb.slidemenu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.bean.AreasInfo;
import edu.njupt.zhb.bean.AreasDevicesInfo;
import edu.njupt.zhb.bean.AtuInfo;
import edu.njupt.zhb.activity.BaseActivity;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.DeviceInfo;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AreasActivity extends BaseActivity {
	private AtuInfo atu = new AtuInfo();
	private LinearLayout mContainer;

	public void InitActivity(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// Bundle bundle = this.getIntent().getExtras();
		// info=new AreasInfo();
		// aid=bundle.getString("aid");
		// aname=bundle.getString("aname");
		this.setContentView(R.layout.room_main);
		mContainer = (LinearLayout) this.findViewById(R.id.roomlist);
		atu = (AtuInfo) getIntent().getSerializableExtra(Config.ATUS);
		getAtuRooms();
		// adata=new AreasData(this);
		// ddata=new DevicesData(this);
		// addata=new AreasDevicesData(this);
		// datalist = ddata.query("");
		//
		// EditText editAreaName=(EditText)findViewById(R.id.area_name);
		//
		// Handler handler = new Handler();
		// handler.postDelayed(adapThread, 100);//延时绑定数据
		// if(aid.equals("")){
		// tag="add";
		// editAreaName.setText("");
		// tmpadlist=null;
		// }else{
		// tag="updata";
		// editAreaName.setText(aname);
		// tmpadlist=addata.queryAreaDevicesData(aid);
		// }
		// info.setId(aid);
		// Button btnback=(Button)findViewById(R.id.btnback);
		// btnback.setFocusable(true);
		// btnback.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.putExtra("result", "ok");// 把返回数据存入
		// AreasActivity.this.setResult(1, intent);// 设置回传数据。
		// AreasActivity.this.finish();
		//
		// }
		// });
		// Button btnOK=(Button)findViewById(R.id.btnOK);
		// btnOK.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// showDialog();
		// EditText editAreaName=(EditText)findViewById(R.id.area_name);
		// info.setName(editAreaName.getText().toString());
		// info.setOtherName(editAreaName.getText().toString());
		// OperData(info,tag);
		// closeDialog();
		// }
		// });
	}

	private void getAtuRooms() {

		new AsyncTask<Object, Object, Object>() {
			List<AreasInfo> rooms = new ArrayList<AreasInfo>();

			@Override
			protected void onPreExecute() {
				MyApp.getAppInstance().setComm(new CommUtil(atu.getAtuIp(),9559));
				MyApp.getAppInstance().getComm().aut_ser = atu.getAtuId();
				MyApp.getAppInstance().getComm().login_id = "admin_"
						+ atu.getAtuPwd();
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Object result) {

				if (rooms != null && rooms.size() > 0) {

					for (final AreasInfo room : rooms) {

						// ImageView appIcon = (ImageView) view
						// .findViewById(R.id.IIcon1);
						View view = View.inflate(AreasActivity.this,
								R.layout.area_list_item, null);
						view.setClickable(true);
						view.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent mIntent = new Intent(AreasActivity.this,
										ControlActivity.class);
								Bundle mBundle = new Bundle();
								mBundle.putString("RoomName", room.getName());
								mBundle.putSerializable(Config.ATUS, atu);
								mIntent.putExtras(mBundle);
								startActivity(mIntent);
							}
						});
						TextView appName = (TextView) view
								.findViewById(R.id.toolbox_title);
						// TextView
						// appDesc=(TextView)view.findViewById(R.id.tv_app_virus_desc);

						// appIcon.setBackground(app.getmAppVirusIcon());
						appName.setText(room.getName());
						// appDesc.setText(app.getmAPPVirusDesc());

						mContainer.addView(view);
					}

				}

				super.onPostExecute(result);
			}

			@Override
			protected Object doInBackground(Object... params) {
				rooms = MyApp.getAppInstance().getComm().getRoomsData();
				return null;
			}

		}.execute();

	}
}
