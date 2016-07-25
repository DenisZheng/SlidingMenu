package edu.njupt.zhb.slidemenu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.AtuInfo;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.comm.AtuUdpInterface;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.comm.DaiKin;
import edu.njupt.zhb.comm.LoadingDialogFragment;
import edu.njupt.zhb.comm.MsgHandler;
import edu.njupt.zhb.utils.AndroidUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FragmentPairAtu extends Fragment {

	private TextView atuId;
	private TextView atuName;
	// private TextView atupwd;
	private TextView atuotherName;
	private TextView roomName;
	private Button pairbtn;
	private Button calbtn;
	private String atuIp;

	private String atuIdstr = null;
	private String atuNamestr = null;
	private String atupwdstr = null;
	private String atuIpstr = null;

	public int top = 0;
	public View context;
	// 实现Handler
	private Handler handler;
	// 消息
	private Message msg;

	private static Boolean isBack;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 安卓2.3以后访问网络增加内容

		View view = inflater.inflate(R.layout.pairatu_activity, container,
				false);
		context = view;
		top = view.getHeight();
		isBack = false;
		handler = new MsgHandler(context.getContext());
		atuId = (TextView) context.findViewById(R.id.atuId);
		atuName = (TextView) context.findViewById(R.id.atuName);
		// atupwd = (TextView) context.findViewById(R.id.atupwd);
		atuotherName = (TextView) context.findViewById(R.id.atuOthername);
		roomName = (TextView) context.findViewById(R.id.roomName);
		pairbtn = (Button) context.findViewById(R.id.pairnextBtn);
		;
		creatdialog();
		pairbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(networkTask).start();
			}
		});

		calbtn = (Button) context.findViewById(R.id.calBtn);
		calbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AtuInfo atu = new AtuInfo();
				atu.setAtuId(atuIdstr);
				atu.setAtuCode(atuNamestr);
				atu.setAtuName(atuotherName.getText().toString().trim());
				atu.setAtuRoom(roomName.getText().toString().trim());
				atu.setAtuPwd(atupwdstr);
				atu.setAtuIp(atuIpstr);
				android.support.v4.app.FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				FragmentPairStep2 demoFragment = new FragmentPairStep2();
				Bundle bundle = new Bundle();
				bundle.putSerializable("AtuInfo", atu);
				demoFragment.setArguments(bundle);
				ft.replace(R.id.content_frame, demoFragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.setCustomAnimations(R.anim.slide_in_right,
						R.anim.slide_out_left, R.anim.slide_in_left,
						R.anim.slide_out_right);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		MainActivity ra = (MainActivity) getActivity();
		ra.SetFindAtuRun(false);
		super.onResume();
	}

	@Override
	public void onPause() {
		MainActivity ra = (MainActivity) getActivity();
		ra.SetFindAtuRun(true);
		super.onPause();
	}

	@Override
	public void onDestroy() {
		MainActivity ra = (MainActivity) getActivity();
		ra.SetFindAtuRun(true);
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	Runnable networkTask = new Runnable() {

		@Override
		public void run() {
			// TODO
			MyApp.getAppInstance().setComm(new CommUtil(atuIp, 9559));
			MyApp.getAppInstance().getComm().aut_ser = atuIdstr;
			MyApp.getAppInstance().getComm().login_id = "admin_" + atupwdstr;

			if (TextUtils.isEmpty(atuotherName.getText())
					|| TextUtils.isEmpty(roomName.getText())) {
				msg = handler.obtainMessage();
				msg.arg1 = 1;
				handler.sendMessage(msg);
				return;
			} else {
				Integer i = MyApp
						.getAppInstance()
						.getComm()
						.SendInCloudCommand(
								CommUtil.ATU_CMD_NMROOM
										+ atuNamestr
										+ ","
										+ atuotherName.getText().toString()
												.trim() + ","
										+ roomName.getText().toString().trim()
										+ ",");
				if (i > 0) {
					String scenestr="一键开启:"+roomName.getText().toString().trim()
							+ ","+atuotherName.getText().toString()
							.trim() + ","+"1A,01,62,37,;";
					Integer j = MyApp
							.getAppInstance()
							.getComm()
							.SendInCloudCommand(
									CommUtil.ATU_CMD_WRSCENE
											+ scenestr);
					AtuInfo atu = new AtuInfo();
					atu.setAtuId(atuIdstr);
					atu.setAtuCode(atuNamestr);
					atu.setAtuName(atuotherName.getText().toString().trim());
					atu.setAtuRoom(roomName.getText().toString().trim());
					atu.setAtuPwd(atupwdstr);
					atu.setAtuIp(atuIpstr);
					int isEx = 0;
					List<AtuInfo> atus = new ArrayList<AtuInfo>();
					atus = Config.loadArray(context.getContext(), Config.ATUS);
					for (final AtuInfo dv : atus) {
						if (dv.getAtuId().equals(atu.getAtuId())) {
							isEx = 1;
							dv.setAtuCode(atu.getAtuCode());
							dv.setAtuName(atu.getAtuName());
							dv.setAtuRoom(atu.getAtuRoom());
							dv.setAtuPwd(atu.getAtuPwd());
							atu.setAtuIp(dv.getAtuIp());
							break;
						}
					}
					if (isEx == 0) {
						atus.add(atu);
					}
					Config.saveArray(context.getContext(), Config.ATUS, atus);
					android.support.v4.app.FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					// ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag(""));
					FragmentPairStep2 demoFragment = new FragmentPairStep2();
					Bundle bundle = new Bundle();
					bundle.putSerializable("AtuInfo", atu);
					demoFragment.setArguments(bundle);
					ft.replace(R.id.content_frame, demoFragment);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.setCustomAnimations(R.anim.slide_in_right,
							R.anim.slide_out_left, R.anim.slide_in_left,
							R.anim.slide_out_right);
					ft.addToBackStack(null);
					ft.commit();
				} else {
					msg = handler.obtainMessage();
					msg.arg1 = 2;
					handler.sendMessage(msg);
					return;
				}
			}
		}
	};

	private void pairAtu() {
		// TODO Auto-generated method stub
		new AsyncTask<Object, Object, Object>() {

			LoadingDialogFragment dialog = LoadingDialogFragment
					.newInstance("正在搜索设备....");
			String strCommand = null;
			String strRoomName = null;
			String strAtuName = null;
			//WifiManager manager;
			//WifiManager.MulticastLock lock;
			private AtuUdpInterface myCallback = new AtuUdpInterface() {

				@Override
				public void ReceiveCallback(String ip, String strEcho) {
					// TODO Auto-generated method stub
					if (strCommand == "getRooms,") {
						String strValue[] = strEcho.split(",");
						strRoomName = strValue[0];
					} else if (strCommand == "rdRoom,") {
						String strValue[] = strEcho.split(",");
						strAtuName = strValue[0];
					}
				}

			};

			private int GetAtuInfo() {
				MyApp.getAppInstance().setComm(
						new CommUtil(atuIpstr, 9559, myCallback));
				MyApp.getAppInstance().getComm().aut_ser = atuIdstr;
				MyApp.getAppInstance().getComm().login_id = "admin_"
						+ atupwdstr;
				try {
					strCommand = "getRooms,";
					MyApp.getAppInstance().getComm()
							.SendInCloudCommand(strCommand);
					if (strRoomName != null) {
						strCommand = "rdRoom,";
						return MyApp
								.getAppInstance()
								.getComm()
								.SendInCloudCommand(
										strCommand + strRoomName + ",");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return -1;
			}

			@Override
			protected void onPreExecute() {
				MainActivity ra = (MainActivity) getActivity();
				ra.SetFindAtuRun(false);
//				manager = (WifiManager) context.getContext().getSystemService(
//						Context.WIFI_SERVICE);
//				lock = manager.createMulticastLock("multicastLock");
				dialog.show(getFragmentManager(), "fragmentDialog");
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Object result) {
				dialog.dismiss();
				atuId.setText(atuIdstr);
				atuName.setText(atuNamestr);
				atuotherName.setText(strAtuName);
				roomName.setText(strRoomName);
				atuIp = atuIpstr;
				pairbtn.setClickable(true);
				pairbtn.setFocusable(true);
				super.onPostExecute(result);
			}

			@Override
			protected Object doInBackground(Object... params) {
				try {
					// Keep a socket open to listen to all the UDP trafic that
					// is destined for this port
					// DatagramSocket socket = null;
					// if (socket == null) {
					// socket = new DatagramSocket(9558);
					// socket.setBroadcast(true);
					// }
					byte[] recvBuf;
					DatagramSocket socket = new DatagramSocket(9558);
					socket.setBroadcast(true);
					Log.i("Incloud", "Ready to receive broadcast packets!");

					// Receive a packet

					DatagramPacket packet;
					recvBuf = new byte[1024];
					packet = new DatagramPacket(recvBuf, recvBuf.length);
					while (isBack == false) {
						//lock.acquire();
						socket.receive(packet);
						//lock.release();
						String data = new String(packet.getData()).trim();
						String revip = packet.getAddress().getHostAddress();
						
						Log.i("Incloud", "Packet received; data: " + data
								+ "  from: " + revip);
						if (data.contains("AtuServer,")) {
							String revStr = data.trim();
							atuIpstr = revip;
							String[] atuinfo = revStr.split(",");
							atuIdstr = atuinfo[1];
							atuNamestr = atuinfo[3];
							atupwdstr = atuinfo[4];
							if (GetAtuInfo() > 0) {
								AtuInfo atu = new AtuInfo();
								atu.setAtuId(atuIdstr);
								atu.setAtuCode(atuNamestr);
								atu.setAtuName(strAtuName);
								atu.setAtuRoom(strRoomName);
								atu.setAtuPwd(atupwdstr);
								atu.setAtuIp(atuIpstr);
								int isEx = 0;
								List<AtuInfo> atus = new ArrayList<AtuInfo>();
								atus = Config.loadArray(context.getContext(),
										Config.ATUS);
								for (final AtuInfo dv : atus) {
									if (dv.getAtuId().equals(atu.getAtuId())) {
										isEx = 1;
										dv.setAtuCode(atu.getAtuCode());
										dv.setAtuName(atu.getAtuName());
										dv.setAtuRoom(atu.getAtuRoom());
										dv.setAtuPwd(atu.getAtuPwd());
										atu.setAtuIp(dv.getAtuIp());
										break;
									}
								}
								if (isEx == 0) {
									atus.add(atu);
								}
								Config.saveArray(context.getContext(),
										Config.ATUS, atus);
							}
							break;
						}
						AndroidUtil.Sleep(100);
					}
					socket.close();
				} catch (IOException ex) {
					Log.i("Incloud", "Oops" + ex.getMessage());
				}

				return null;
			}

		}.execute();
	}

	private void creatdialog() {

		// startActivity(new Intent(
		// android.provider.Settings.ACTION_WIFI_SETTINGS));
		// pairAtu();
		LayoutInflater inflaterDl = LayoutInflater.from(context.getContext());
		LinearLayout layout = (LinearLayout) inflaterDl.inflate(
				R.layout.comm_alert_layout, null);

		// 对话框
		final Dialog dialog = new AlertDialog.Builder(context.getContext())
				.create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		TextView tv = (TextView) layout.findViewById(R.id.comm_dialog_msg);
		tv.setText("1.初次配对,请手动链接AIR100_AP热点\r\n2.网络环境没有改变则无须更换热点\r\n3.链接热点后返回,短按一次配对键\r\n");
		// 取消按钮
		Button btnCancel = (Button) layout.findViewById(R.id.dialog_no);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 确定按钮
		Button btnYes = (Button) layout.findViewById(R.id.dialog_yes);
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (IsSSIDEx()) {
					startActivity(new Intent(
							android.provider.Settings.ACTION_WIFI_SETTINGS));
				}
				dialog.dismiss();
				pairAtu();
			}
		});
	}

	public static boolean onBackKeyDown() {
		// TODO Auto-generated method stub
		// System.out.println("back already back");
		isBack = true;
		return true;
	}

	private Boolean IsSSIDEx() {
		List<ScanResult> list;
		WifiManager mWifiManager = (WifiManager) context.getContext()
				.getSystemService(Context.WIFI_SERVICE);
		list = mWifiManager.getScanResults();
		WifiInfo info = mWifiManager.getConnectionInfo();

		// 当前连接SSID
		String currentSSid = info.getSSID();
		currentSSid = currentSSid.replace("\"", "");
		if (!currentSSid.equals("AIR100_AP")) {
			for (int index = 0; index < list.size(); index++) {
				if (list.get(index).SSID.equals("AIR100_AP")) {
					return true;
				}
			}
		}
		// currentSSid = currentSSid.replace("\"", "");
		return false;
	}

	/**
	 * 左侧菜单点击切换首页的内容
	 */
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		MainActivity ra = (MainActivity) getActivity();
		ra.switchContent(fragment);

	}
}
