package edu.njupt.zhb.slidemenu;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TimerTask;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.devspark.progressfragment.ProgressFragment;

import edu.njupt.zhb.activity.BaseActivity;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.AtuInfo;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.bean.PublicData;
import edu.njupt.zhb.comm.AtuUdpInterface;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.comm.LoadingDialogFragment;
import edu.njupt.zhb.comm.MsgHandler;
import edu.njupt.zhb.slidemenu.R;
import edu.njupt.zhb.utils.AndroidUtil;
import edu.njupt.zhb.utils.ImageUtil;
import edu.njupt.zhb.view.IShow;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * s
 * 
 * @author Administrator
 * 
 */
public class FragmentDevices extends Fragment {
	private String flag = "0";
	public int top = 0;
	public static View context;
	private boolean restart = true;
	private Handler handler;
	private Message msg;
	private static AppAdapter mAdapter;
	private SwipeMenuListView mListView;
	private static List<AtuInfo> atus;
	private static boolean isRunning = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = inflater.inflate(R.layout.fragment_devices, container, false);
		// context = view;
		top = context.getHeight();
		atus = Config.loadArray(context.getContext(), Config.ATUS);
		handler = new MsgHandler(context.getContext());
		mListView = (SwipeMenuListView) context.findViewById(R.id.deviceslist);
		mAdapter = new AppAdapter();
		mListView.setAdapter(mAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(context
						.getContext().getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};

		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				AtuInfo item = atus.get(position);
				switch (index) {
				case 0:
					atus.remove(position);
					Config.saveArray(context.getContext(), Config.ATUS, atus);
					mAdapter.notifyDataSetChanged();
					break;
				}
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AtuInfo item = atus.get(position);
				if ((item.getAtuName() == null || item.getAtuName().equals(""))
						|| (item.getAtuRoom() == null || item.getAtuRoom()
								.equals(""))) {
					LayoutInflater inflaterDl = LayoutInflater.from(context
							.getContext());
					LinearLayout layout = (LinearLayout) inflaterDl.inflate(
							R.layout.bl_alert_layout, null);

					// 对话框
					final Dialog dialog = new AlertDialog.Builder(context
							.getContext()).create();
					dialog.show();
					dialog.getWindow().setContentView(layout);
					TextView tv = (TextView) layout
							.findViewById(R.id.dialog_msg);
					tv.setText("网关未注册，无法进行操作，请先注册");
					// 取消按钮
					Button btnCancel = (Button) layout
							.findViewById(R.id.dialog_ok);
					btnCancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				} else {
					if (item.getIsRemote() != Config.ERR_CTR_MODE) {
						Intent mIntent = new Intent(context.getContext(),
								ControlActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putString("RoomName", item.getAtuRoom());
						mBundle.putSerializable(Config.ATUS, item);
						mIntent.putExtras(mBundle);
						startActivity(mIntent);
					} else {
						LayoutInflater inflaterDl = LayoutInflater.from(context
								.getContext());
						LinearLayout layout = (LinearLayout) inflaterDl
								.inflate(R.layout.bl_alert_layout, null);
						// 对话框
						final Dialog dialog = new AlertDialog.Builder(context
								.getContext()).create();
						dialog.show();
						dialog.getWindow().setContentView(layout);
						TextView tv = (TextView) layout
								.findViewById(R.id.dialog_msg);
						tv.setText("设备不在线，无法控制");
						// 取消按钮
						Button btnCancel = (Button) layout
								.findViewById(R.id.dialog_ok);
						btnCancel.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
					}

				}
			}
		});
		InitAtuSta();
		return context;
	}

//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		if (atus.size() > 0) {
//			// Setup content view
//			setContentView(context);
//			// Setup text for empty content
//			setEmptyText(R.string.empty);
//		} else {
//			setContentView(context);
//			setEmptyText(R.string.empty);
//			setContentShown(true);
//		}
//		InitAtuSta();
//	}

	public void InitAtuSta() {
		if (atus.size() > 0) {
			int position = 0;
			for (AtuInfo atu : atus) {
				MyThread myThread = new MyThread(atu, position);
				myThread.start();
				position++;
			}
		}
	}

	@Override
	public void onResume() {
		isRunning = true;
		InitAtuSta();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		getActivity().registerReceiver(myNetReceiver, mFilter);
		super.onResume();
	}

	@Override
	public void onPause() {
		isRunning = false;
		super.onPause();
	}

	@Override
	public void onDestroy() {

		handler.removeCallbacksAndMessages(null);
		thandler.removeCallbacksAndMessages(null);
		getActivity().unregisterReceiver(myNetReceiver);
		super.onDestroy();
	}

	class MyThread extends Thread {
		private AtuInfo atu;
		private int position;

		public MyThread(AtuInfo atu, int position) {
			this.atu = atu;
			this.position = position;
		}

		private AtuUdpInterface runCallback = new AtuUdpInterface() {
			@Override
			public void ReceiveCallback(String ip, String strEcho) {
				atu.setAtuId(strEcho);
				atu.setAtuIp(ip);
			}
		};

		private void SetAtuRemotInfo(String atuId, AtuInfo atu) {
			String userName = AndroidUtil.GetUserName(context.getContext());
			String pwd = AndroidUtil.GetPassWord(context.getContext());
			if (!userName.equals("admin")) {
				String logrev = Config.RemLogin(userName, pwd, atuId);
				if (!logrev.contains("Error:")) {
					String revStr[] = logrev.split(";");
					String remnetwork = revStr[0];
					String remIp = remnetwork.split(":")[0].trim();
					int remPort = Integer.parseInt(remnetwork.split(":")[1]
							.trim());
					atu.setAtuRemoIp(remIp);
					atu.setAtuRemoPort(remPort);
				}
			}
		}

		private void UpdateAtus(AtuInfo atu) {
			for (AtuInfo ai : atus) {
				if (ai.getAtuId().equals(atu.getAtuId())) {
					ai.setAtuRemoIp(atu.getAtuRemoIp());
					ai.setAtuRemoPort(atu.getAtuRemoPort());
					ai.setAtuIp(atu.getAtuIp());
					ai.setIsRemote(atu.getIsRemote());
					break;
				}
			}
			Config.saveArray(context.getContext(), Config.ATUS, atus);
		}

		@Override
		public void run() {
			Looper.prepare();
			boolean firstRun = true;
			while (true) {
				if (isRunning) {
					if (atu.getAtuName() != null
							&& atu.getAtuName().length() != 0) {
						if (firstRun) {
							try {
								String boardip = getBroadcast();
								MyApp.getAppInstance().setComm(new CommUtil());
								int i = MyApp
										.getAppInstance()
										.getComm()
										.FindSingleAtuCmd(atu.getAtuId(),
												boardip, 9559, runCallback);
								if (i <= 0) {
									SetAtuRemotInfo(atu.getAtuId(), atu);
									String remoteIp = atu.getAtuRemoIp();
									int remotePort = atu.getAtuRemoPort();
									MyApp.getAppInstance().setComm(
											new CommUtil());
									int j = MyApp
											.getAppInstance()
											.getComm()
											.FindAtuCmd(remoteIp, remotePort,
													null);
									if (j <= 0) {
										String userName = AndroidUtil
												.GetUserName(context
														.getContext());
										String pwd = AndroidUtil
												.GetPassWord(context
														.getContext());
										int rev = Config.RemControl(userName,
												pwd, atu.getAtuId(),
												atu.getAtuPwd(), "", null);
										if (rev > 0) {
											atu.setIsRemote(Config.SLOW_CTR_MODE);

										} else {
											atu.setIsRemote(Config.ERR_CTR_MODE);

										}
									} else {
										atu.setIsRemote(Config.REMO_CTR_MODE);

									}
								} else {
									atu.setIsRemote(Config.LAN_CTR_MODE);
									UpdateAtus(atu);
									Message message = new Message();
									message.what = 2;
									Bundle bundle = new Bundle();
									bundle.putString("position",
											String.valueOf(position));
									message.setData(bundle);
									thandler.sendMessage(message);
									break;
								}
								UpdateAtus(atu);
								Message message = new Message();
								message.what = 2;
								Bundle bundle = new Bundle();
								bundle.putString("position",
										String.valueOf(position));
								message.setData(bundle);
								thandler.sendMessage(message);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						// Config.saveArray(context.getContext(), Config.ATUS,
						// tatus);
						// Message message = new Message();
						// message.what = 1;
						// thandler.sendMessage(message);
						AndroidUtil.Sleep(30000);
					}

				} else {
					break;
				}

			}
		}
	}

	private Handler thandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				refListView();
				break;
			case 2:
				String position = msg.getData().getString("position");
				int Iindex = Integer.valueOf(position);
				View view = mListView.getChildAt(Iindex
						- mListView.getFirstVisiblePosition());
				mListView.getAdapter().getView(Iindex, view, mListView);
//				if (Iindex == 0) {
//					setContentShown(true);
//				}
				break;
			}
		}
	};

	public static void refListView() {
		atus = Config.loadArray(context.getContext(), Config.ATUS);
		mAdapter.notifyDataSetChanged();
	}

	public static boolean setIsRunning(boolean bl) {
		// TODO Auto-generated method stub
		// System.out.println("back already back");
		isRunning = bl;
		return true;
	}

	private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
		private ConnectivityManager connectivityManager;

		private NetworkInfo info;

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			Bundle b = intent.getExtras();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				// System.out.println("网络状态已经改变");
				// connectivityManager = (ConnectivityManager) context
				// .getSystemService(Context.CONNECTIVITY_SERVICE);
				// info = connectivityManager.getActiveNetworkInfo();
				// if (info != null && info.isAvailable()) {
				// System.out.println("网络连接");
				//
				// } else {
				// System.out.println("网络断开");
				// }
				NetworkInfo netInfo = (NetworkInfo) b
						.get(ConnectivityManager.EXTRA_NETWORK_INFO);
				// NetworkInfo.State state = netInfo.getState();
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetInfo = connectivityManager
						.getActiveNetworkInfo();
				NetworkInfo.State state = activeNetInfo.getState();
				if ((state == NetworkInfo.State.CONNECTED)
						&& (activeNetInfo != null)
						&& (activeNetInfo.getType() != netInfo.getType())) {
					System.out.println("网络状态已经改变");
					isRunning = false;
					AndroidUtil.Sleep(500);
					isRunning = true;
					InitAtuSta();
				}

			}
		}

	};

	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return atus.size();
		}

		@Override
		public AtuInfo getItem(int position) {
			return atus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context.getContext()
						.getApplicationContext(), R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			AtuInfo item = getItem(position);
			holder.iv_icon.setImageResource(R.drawable.icon_air_on);
			if ((item.getAtuName() == null || item.getAtuName().equals(""))
					|| (item.getAtuRoom() == null || item.getAtuRoom().equals(
							""))) {
				holder.tv_name.setText("未注册网关-" + position);
			} else {
				if (item.getIsRemote() != Config.ERR_CTR_MODE) {
					if (item.getIsRemote() == Config.LAN_CTR_MODE) {
						holder.icon_cloud.setVisibility(View.GONE);
					} else if (item.getIsRemote() == Config.REMO_CTR_MODE) {
						holder.icon_cloud
								.setImageResource(R.drawable.icon_cloud);
						holder.icon_cloud.setVisibility(View.VISIBLE);
					} else if (item.getIsRemote() == Config.SLOW_CTR_MODE) {
						holder.icon_cloud.setImageResource(R.drawable.icon_low);
						holder.icon_cloud.setVisibility(View.VISIBLE);
					}
				} else {
					holder.icon_cloud.setImageResource(R.drawable.offline);
					holder.icon_cloud.setVisibility(View.VISIBLE);
				}
				holder.tv_name.setText(item.getAtuRoom() + "-"
						+ item.getAtuName());
			}
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_name;
			ImageView icon_cloud;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				icon_cloud = (ImageView) view.findViewById(R.id.icon_cloud);
				view.setTag(this);
			}
		}
	}

	@SuppressLint("NewApi")
	public static String getBroadcast() throws SocketException {
		System.setProperty("java.net.preferIPv4Stack", "true");
		for (Enumeration<NetworkInterface> niEnum = NetworkInterface
				.getNetworkInterfaces(); niEnum.hasMoreElements();) {
			NetworkInterface ni = niEnum.nextElement();
			if (!ni.isLoopback()) {
				for (InterfaceAddress interfaceAddress : ni
						.getInterfaceAddresses()) {
					if (interfaceAddress.getBroadcast() != null) {
						return interfaceAddress.getBroadcast().toString()
								.substring(1);
					}
				}
			}
		}
		return null;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
