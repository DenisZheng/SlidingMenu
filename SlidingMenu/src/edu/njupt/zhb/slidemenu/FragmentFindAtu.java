package edu.njupt.zhb.slidemenu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.AtuInfo;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.comm.LoadingDialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentFindAtu extends Fragment {

	public int top = 0;
	public View context;
	private LinearLayout mContainer;
	private TextView mKillVirusResult;
	private WifiManager.MulticastLock lock;
	private WifiManager manager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.findatu_activity, container,
				false);
		context = view;

		top = view.getHeight();
		manager = (WifiManager) context.getContext().getSystemService(
				Context.WIFI_SERVICE);
		lock = manager.createMulticastLock("test wifi");
		mContainer = (LinearLayout) context.findViewById(R.id.ll_container);
		mKillVirusResult = (TextView) context
				.findViewById(R.id.tv_kill_virus_result);
		scanAtu();
		return view;
	}

	/**
	 * 扫描查找网关，并动态显示在界面
	 */
	private void scanAtu() {

//		new AsyncTask<Object, Object, Object>() {
//			List<AtuInfo> apps = new ArrayList<AtuInfo>();
//			List<AtuInfo> atus = new ArrayList<AtuInfo>();
//			LoadingDialogFragment dialog = LoadingDialogFragment.newInstance();
//			@Override
//			protected void onPreExecute() {
//				mKillVirusResult.setText("正在查找中，请稍等...");
//				dialog.show(getFragmentManager(), "fragmentDialog");
//				try {
//					MyApp.getAppInstance().setComm(
//							new CommUtil(getBroadcastAddress(
//									context.getContext()).getHostAddress()
//									.toString()));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				super.onPreExecute();
//			}
//
//			@Override
//			protected void onPostExecute(Object result) {
//				dialog.dismiss();
//				atus = Config.loadArray(context.getContext(), Config.ATUS);
//				if (apps.size() > 0) {
//					mKillVirusResult.setTextColor(Color.RED);
//					mKillVirusResult.setText("扫描完成！发现网关" + apps.size() + "个");
//					for (final AtuInfo app : apps) {
//						int isEx = 0;
//						int i=0;
//						for (final AtuInfo atu : atus) {
//							i++;
//							if (app.getAtuId().equals(atu.getAtuId())) {
//								isEx = 1;
//								app.setAtuCode(atu.getAtuCode());
//								app.setAtuName(atu.getAtuName());
//								app.setAtuRoom(atu.getAtuRoom());
//								app.setAtuPwd(atu.getAtuPwd());
//								atu.setAtuIp(app.getAtuIp());
//								break;
//							}
//						}
//						if (isEx == 0) {
//							atus.add(app);
//						}
//						// ImageView appIcon = (ImageView) view
//						// .findViewById(R.id.IIcon1);
//						View view = View.inflate(context.getContext(),
//								R.layout.listview_item, null);
//						view.setClickable(true);
//						
//						TextView appName = (TextView) view
//								.findViewById(R.id.textView_item);
//						if ((app.getAtuName() == null || app.getAtuName()
//								.equals(""))
//								|| (app.getAtuRoom() == null || app
//										.getAtuRoom().equals(""))) {
//							appName.setText("未注册网关" + i);
//							view.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									LayoutInflater inflaterDl = LayoutInflater.from(context
//											.getContext());
//									LinearLayout layout = (LinearLayout) inflaterDl.inflate(
//											R.layout.bl_alert_layout, null);
//
//									// 对话框
//									final Dialog dialog = new AlertDialog.Builder(context
//											.getContext()).create();
//									dialog.show();
//									dialog.getWindow().setContentView(layout);
//									TextView tv = (TextView) layout
//											.findViewById(R.id.dialog_msg);
//									tv.setText("网关未注册，无法进行操作，请先注册");
//									// 取消按钮
//									Button btnCancel = (Button) layout
//											.findViewById(R.id.dialog_ok);
//									btnCancel.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View v) {
//											dialog.dismiss();
//										}
//									});
//								}
//							});
//							
//						} else {
//							appName.setText(app.getAtuRoom() + "-"
//									+ app.getAtuName());
//							view.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									Intent mIntent = new Intent(context
//											.getContext(), ControlActivity.class);
//									Bundle mBundle = new Bundle();
//									mBundle.putString("RoomName", app.getAtuRoom());
//									mBundle.putSerializable(Config.ATUS, app);
//									mIntent.putExtras(mBundle);
//									startActivity(mIntent);
//								}
//							});
//						}
//						mContainer.addView(view);
//					}
//
//				} else if (apps.size() == 0) {
//					mKillVirusResult.setText("扫描完成！没有发现网关设备！请配对网关！");
//
//				}
//				Config.saveArray(context.getContext(), Config.ATUS, atus);
//				super.onPostExecute(result);
//			}
//
//			@Override
//			protected Object doInBackground(Object... params) {
//				// TODO Auto-generated method stub
//				for (int i = 0; i < 10; i++) {
//					lock.acquire();
//					DatagramPacket packet = MyApp.getAppInstance().getComm()
//							.FindAtu();
//					lock.release();
//					if (packet != null) {
//						String revStr = new String(packet.getData()).trim();
//						if (!revStr.equals("FindAtu,")) {
//							Log.i("Incloud", "Received packet: " + revStr
//									+ "from server:"
//									+ packet.getAddress().getHostAddress());
//							String atuId = null;
//							if (revStr.length() > 12)
//								atuId = revStr.replace("AtuServer=", "")
//										.replace(",", "").replace(" ", "")
//										.substring(0, 12);
//							Boolean isEx = false;
//							if (apps.size() > 0) {
//								for (final AtuInfo app : apps) {
//									if (app.getAtuId().equals(atuId)) {
//										isEx = true;
//										app.setAtuIp(packet.getAddress()
//												.getHostAddress());
//										break;
//									}
//								}
//							}
//							if (!isEx) {
//								AtuInfo dev = new AtuInfo();
//								dev.setAtuId(atuId);
//								dev.setAtuIp(packet.getAddress()
//										.getHostAddress());
//								apps.add(dev);
//							}
//						}
//						try {
//							Thread.sleep(1 * 1000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//				return null;
//			}
//
//		}.execute();

	}

	static InetAddress getBroadcastAddress(Context context) throws IOException {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}
}
