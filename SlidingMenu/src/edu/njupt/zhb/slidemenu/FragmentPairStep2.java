package edu.njupt.zhb.slidemenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.AtuInfo;
import edu.njupt.zhb.comm.AtuUdpInterface;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.comm.MsgHandler;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentPairStep2 extends Fragment {
	@SuppressWarnings("unused")
	private static class NetworkConfig {
		public static String Chipset = "RT3070";

		public static String NetworkType = "SoftAP";
		public final static String NETWORKTYPE_SOFTAP = "SoftAP";
		public final static String NETWORKTYPE_INFRA = "Infra";
		public final static String NETWORKTYPE_ADHOC = "Adhoc";

		public static String BootProtocol = "STATIC";
		public final static String BOOT_DHCP = "DHCP";
		public final static String BOOT_STATIC = "STATIC";

		public static String IPAddr = "192.168.100.1";
		public static String Gateway = "192.168.100.1";
		public static String SSID = "SkyEye_RT3070";
		public static String AuthMode = "OPEN";
		public final static String AUTH_OPEN = "OPEN";
		public final static String AUTH_SHARED = "SHARED";
		public final static String AUTH_WPAPSK = "WPAPSK";
		public final static String AUTH_WPA2PSK = "WPA2PSK";
		public final static String AUTH_WEPAUTO = "WEPAUTO";

		public static String EncryptType = "NONE";
		public final static String ENCRYPT_NONE = "NONE";
		public final static String ENCRYPT_WEP = "WEP";
		public final static String ENCRYPT_TKIP = "TKIP";
		public final static String ENCRYPT_AES = "AES";

		public static String AuthKey = "";

		public static String InternalPort = "";
		public static String ExternalPort = "";
		public static String Server = "";
		public static String Username = "";
		public static String Password = "";
		public static String Hostname = "";
	}

	private TextView ssid;
	private TextView wifipwd;
	private Button pairbtn;
	private Button cbnBtn;
	private String ssidstr = null;
	private String keymgmt = null;
	private String atuIp = null;
	private String atuId = null;
	private String atupwd = null;

	public int top = 0;
	public View context;
	// 实现Handler
	private Handler handler;
	// 消息
	private Message msg;
	private WifiManager wifiManager;
	private List<ScanResult> wifilist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pairatu_step2, container, false);
		context = view;
		top = view.getHeight();
		MainActivity ra = (MainActivity) getActivity();
		ra.SetFindAtuRun(false);
		handler = new MsgHandler(context.getContext());
		AtuInfo ai = (AtuInfo) getArguments().getSerializable("AtuInfo");
		atuId = ai.getAtuId().toString();
		atupwd = ai.getAtuPwd().toString();
		atuIp = ai.getAtuIp().toString();
		ssid = (TextView) context.findViewById(R.id.ssid);
		wifipwd = (TextView) context.findViewById(R.id.wifipwd);
		// getSSIDKey();
		NetworkConfig.BootProtocol = NetworkConfig.BOOT_DHCP;
		NetworkConfig.NetworkType = NetworkConfig.NETWORKTYPE_ADHOC;
		NetworkConfig.SSID = ssidstr;
		NetworkConfig.AuthMode = keymgmt;
		// ssid.setText(ssidstr);
		ssid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				init();
				creatselectdialog();
			}
		});
		pairbtn = (Button) context.findViewById(R.id.pairBtn);
		pairbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(networkTask).start();
			}
		});

		cbnBtn = (Button) context.findViewById(R.id.cbnBtn);
		cbnBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity ra = (MainActivity) getActivity();
				ra.SetFindAtuRun(true);
				ra.SetTitles("空调列表");
				android.support.v4.app.FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				FragmentDevices demoFragment = new FragmentDevices();
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
			Looper.prepare();
			// TODO
			MyApp.getAppInstance().setComm(new CommUtil(atuIp, 9559));
			MyApp.getAppInstance().getComm().aut_ser = atuId;
			MyApp.getAppInstance().getComm().login_id = "admin_" + atupwd;
			if (TextUtils.isEmpty(wifipwd.getText())) {
				msg = handler.obtainMessage();
				msg.arg1 = 3;
				handler.sendMessage(msg);
				return;
			} else {
				Integer i = MyApp
						.getAppInstance()
						.getComm()
						.SetNetworkCommand(
								CommUtil.ATU_CMD_WPA + "\r\n" + "ssid=\""
										+ ssid.getText().toString().trim()
										+ "\"" + "\r\n" + "psk=\""
										+ wifipwd.getText().toString().trim()
										+ "\"" + "\r\n"
										+ "key_mgmt=WPA-PSK\r\n"
										+ "priority=96\r\n}\r\n");

				if (i > 0) {
					Integer j = MyApp
							.getAppInstance()
							.getComm()
							.SetNetworkCommand(
									CommUtil.ATU_CMD_NETWORK
											+ "\r\n"
											+ "DEVICE ra0\r\n"
											+ "CHIPSET RT3070\r\n"
											+ "BOOTPROTO "
											+ NetworkConfig.BootProtocol
											+ "\r\n"
											+ "IPADDR "
											+ NetworkConfig.IPAddr
											+ "\r\n"
											+ "GATEWAY "
											+ NetworkConfig.Gateway
											+ "\r\n"
											+ "NETWORK_TYPE "
											+ NetworkConfig.NetworkType
											+ "\r\n"
											+ "SSID "
											+ ssid.getText().toString().trim()
											+ "\r\n"
											+ "AUTH_MODE "
											+ NetworkConfig.AuthMode
											+ "\r\n"
											+ "ENCRYPT_TYPE "
											+ NetworkConfig.EncryptType
											+ "\r\n"
											+ "AUTH_KEY "
											+ wifipwd.getText().toString()
													.trim() + "\r\n"
											+ "WPS_TRIG_KEY POWER\r\n");
					if (j > 0) {
						msg = handler.obtainMessage();
						msg.arg1 = 4;
						handler.sendMessage(msg);
						android.support.v4.app.FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						MainActivity ra = (MainActivity) getActivity();
						ra.SetFindAtuRun(true);
						ra.SetTitles("空调列表");
						FragmentDevices demoFragment = new FragmentDevices();
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
				} else {
					msg = handler.obtainMessage();
					msg.arg1 = 2;
					handler.sendMessage(msg);
					return;
				}
			}
		}
		// }
		// }
	};

	private void init() {
		wifiManager = (WifiManager) context.getContext().getSystemService(
				Context.WIFI_SERVICE);
		wifilist = wifiManager.getScanResults();
		// Collections.sort(wifilist);

	}

	private void creatselectdialog() {

		LayoutInflater inflaterDl = LayoutInflater.from(context.getContext());
		LinearLayout layout = (LinearLayout) inflaterDl.inflate(
				R.layout.alert_dialog_menu_layout, null);

		// 对话框
		final Dialog dialog = new AlertDialog.Builder(context.getContext())
				.create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		ListView listView = (ListView) layout.findViewById(R.id.content_list);
		if (wifilist == null) {
			Toast.makeText(context.getContext(), "wifi未打开！", Toast.LENGTH_LONG)
					.show();
		} else {
			listView.setAdapter(new WifiAdapter(context.getContext(), wifilist));
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ScanResult scanResult = wifilist.get(position);
				ssid.setText(scanResult.SSID);
				dialog.dismiss();
			}
		});
		TextView tv = (TextView) layout.findViewById(R.id.cancel_text);

		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	private void getSSIDKey() {

		WifiManager mWifiManager = (WifiManager) context.getContext()
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = mWifiManager.getConnectionInfo();

		// 当前连接SSID
		String currentSSid = info.getSSID();

		currentSSid = currentSSid.replace("\"", "");
		// ssidstr,keymgmt
		// 得到配置好的网络连接
		ssidstr = currentSSid;
		List<WifiConfiguration> wifiConfigList = mWifiManager
				.getConfiguredNetworks();

		for (WifiConfiguration wifiConfiguration : wifiConfigList) {
			// 配置过的SSID
			String configSSid = wifiConfiguration.SSID;
			configSSid = configSSid.replace("\"", "");

			// 比较networkId，防止配置网络保存相同的SSID
			if (currentSSid.equals(configSSid)
					&& info.getNetworkId() == wifiConfiguration.networkId) {

				keymgmt = getSecurity(wifiConfiguration);
				break;
			}
		}

	}

	/**
	 * These values are matched in string arrays -- changes must be kept in sync
	 */
	static String getSecurity(WifiConfiguration config) {
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
			return NetworkConfig.AUTH_WPAPSK;
		}
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP)
				|| config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
			return NetworkConfig.AUTH_SHARED;
		}
		return (config.wepKeys[0] != null) ? NetworkConfig.AUTH_WEPAUTO
				: NetworkConfig.AUTH_OPEN;
	}

	class WifiAdapter extends BaseAdapter {

		LayoutInflater inflater;
		List<ScanResult> list;

		public WifiAdapter(Context context, List<ScanResult> list) {
			// TODO Auto-generated constructor stub
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			view = inflater
					.inflate(R.layout.a1_select_device_item_layout, null);
			ScanResult scanResult = list.get(position);
			TextView textView = (TextView) view.findViewById(R.id.item_name);
			textView.setText(scanResult.SSID);
			ImageView imageView = (ImageView) view.findViewById(R.id.item_icon);
			// 判断信号强度，显示对应的指示图标
			// if (Math.abs(scanResult.level) > 100) {
			// imageView.setImageDrawable(getResources().getDrawable(R.drawable.connect_ap_1));
			// } else if (Math.abs(scanResult.level) > 80) {
			// imageView.setImageDrawable(getResources().getDrawable(R.drawable.connect_ap_1));
			// } else if (Math.abs(scanResult.level) > 70) {
			// imageView.setImageDrawable(getResources().getDrawable(R.drawable.connect_ap_1));
			// } else if (Math.abs(scanResult.level) > 60) {
			// imageView.setImageDrawable(getResources().getDrawable(R.drawable.connect_ap_2));
			// } else if (Math.abs(scanResult.level) > 50) {
			// imageView.setImageDrawable(getResources().getDrawable(R.drawable.connect_ap_2));
			// } else {
			// imageView.setImageDrawable(getResources().getDrawable(R.drawable.connect_ap_3));
			// }
			if (Math.abs(scanResult.level) < 50) {
				imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.connect_ap_3));
				// imageView.setImageDrawable(getResources().getDrawable(R.drawable.connect_ap_1));
			} else if (Math.abs(scanResult.level) < 60
					&& Math.abs(scanResult.level) > 50) {
				imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.connect_ap_2));
				// imageView.setImageDrawable(getResources().getDrawable(R.drawable.connect_ap_1));
			} else if (Math.abs(scanResult.level) > 60
					&& Math.abs(scanResult.level) < 70) {
				imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.connect_ap_2));
			} else if (Math.abs(scanResult.level) > 70
					&& Math.abs(scanResult.level) < 80) {
				imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.connect_ap_1));
			} else if (Math.abs(scanResult.level) > 80
					&& Math.abs(scanResult.level) < 90) {
				imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.connect_ap_1));
			} else {
				imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.connect_ap_1));
			}
			return view;
		}

	}

}
