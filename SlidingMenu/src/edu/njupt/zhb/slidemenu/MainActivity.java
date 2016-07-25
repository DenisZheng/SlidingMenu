package edu.njupt.zhb.slidemenu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.AreasInfo;
import edu.njupt.zhb.bean.AtuInfo;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.comm.AtuUdpInterface;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.comm.DaiKin;
import edu.njupt.zhb.comm.LoadingDialogFragment;
import edu.njupt.zhb.comm.UpdateManager;
import edu.njupt.zhb.slidemenu.R;
import edu.njupt.zhb.utils.AndroidUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import android.R.bool;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	protected SlidingMenu leftRightSlidingMenu;
	private ImageButton ivTitleBtnLeft;
	private ImageButton ivTitleBtnRight;
	private boolean bFirstRun = true;
	private boolean boolRunning = true;
	private TextView ivTitleName;
	public String title;
	private Fragment mContent;
	private Timer mTimer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLeftRightSlidingMenu();
		setContentView(R.layout.activity_main);
		initView();
		SetTitles("空调列表");
//		mTimer = new Timer(true);
//		mTimer.schedule(reftask, 1000, 1000);
		// 检查软件更新
		UpdateManager manager = new UpdateManager(MainActivity.this);
		manager.checkUpdate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		bFirstRun = true;
		boolRunning = true;
	}

	@Override
	protected void onPause() {
		// boolRunning = false;
		// bFirstRun = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	public void SetFindAtuRun(boolean isRun) {
		boolRunning = isRun;
	}

	public void SetTitles(String title) {
		ivTitleName.setText(title);
		// if(title.equals("区域")){
		// ivTitleBtnRight.setVisibility(View.VISIBLE);

		// }else{
		// ivTitleBtnRight.setVisibility(View.INVISIBLE);
		// }
	}

	private void initView() {
		ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		// ivTitleBtnRight =
		// (ImageButton)this.findViewById(R.id.ivTitleBtnRight);
		// ivTitleBtnRight.setOnClickListener(this);
		ivTitleName = (TextView) this.findViewById(R.id.ivTitleName);
		ivTitleName.setText(title);
	}

	private void initLeftRightSlidingMenu() {
		mContent = new FragmentDevices();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();
		setBehindContentView(R.layout.main_left_layout);
		FragmentTransaction leftFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment leftFrag = new LeftSlidingMenuFragment();
		leftFragementTransaction.replace(R.id.main_left_fragment, leftFrag);
		leftFragementTransaction.commit();
		// customize the SlidingMenu
		leftRightSlidingMenu = getSlidingMenu();
		leftRightSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
		leftRightSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置菜单宽度
		leftRightSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		// leftRightSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置手势模式
		leftRightSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置左菜单阴影图片
		leftRightSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		leftRightSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果

		// leftRightSlidingMenu.setSecondaryMenu(R.layout.main_right_layout);
		// FragmentTransaction rightFragementTransaction =
		// getSupportFragmentManager().beginTransaction();
		// Fragment rightFrag = new RightSlidingMenuFragment();
		// leftFragementTransaction.replace(R.id.main_right_fragment,
		// rightFrag);
		// rightFragementTransaction.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 创建退出对话框
			if (mContent instanceof FragmentPairAtu) {
				FragmentPairAtu.onBackKeyDown();
			}
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();

		}

		return false;

	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			leftRightSlidingMenu.showMenu();
			break;
		case R.id.ivTitleBtnRight:
			// leftRightSlidingMenu.showSecondaryMenu(true);
			// SetTitles("网关列表");
			break;
		default:
			break;
		}

	}

	TimerTask reftask = new TimerTask() { // 1 second interrupt
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};

	final android.os.Handler handler = new android.os.Handler() {
		int iTimer10 = 0;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // 1 sencond routine
				iTimer10++;
				if (boolRunning) {
					if (bFirstRun) {
						bFirstRun = false;
						FindAtuAsyncTask mUdpAsyncTask = new FindAtuAsyncTask();
						mUdpAsyncTask.execute(CommUtil.ATU_CMD_FIND,
								CommUtil.ATU_SERVER_ACK);
					}
					if (iTimer10 >= 30) { // 30s
						iTimer10 = 0; // Refresh data ever 30 second
						bFirstRun = true;
						FindAtuAsyncTask mUdpAsyncTask = new FindAtuAsyncTask();
						mUdpAsyncTask.execute(CommUtil.ATU_CMD_FIND,
								CommUtil.ATU_SERVER_ACK);
					}
				}
				return;
			case 2: // 300ms
				break;
			}
			super.handleMessage(msg);
		}
	};

	private class FindAtuAsyncTask extends AsyncTask<String, String, Integer> {
		private String strCommand = "";
		List<AtuInfo> apps = new ArrayList<AtuInfo>();
		List<AtuInfo> atus = new ArrayList<AtuInfo>();

		private FindAtuAsyncTask() {
			super();
		}

		private void SetAtuRemotInfo(String atuId, AtuInfo atu) {
			String userName = AndroidUtil.GetUserName(MainActivity.this);
			String pwd = AndroidUtil.GetPassWord(MainActivity.this);
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

		private AtuUdpInterface myCallback = new AtuUdpInterface() {
			@Override
			public void ReceiveCallback(String ip, String atuId) { // command
																	// success
																	// and
																	// with
																	// ack
				if (strCommand == CommUtil.ATU_CMD_FIND) {
					if (atus.size() > 0) {
						for (final AtuInfo app : atus) {
							if (app.getAtuId().equals(atuId)) {
								app.setAtuIp(ip);
								app.setIsRemote(Config.LAN_CTR_MODE);
								SetAtuRemotInfo(atuId, app);
								break;
							}
						}
					}
				} else if (strCommand == CommUtil.ATU_CMD_LOGIN) {
				} else if (strCommand == CommUtil.ATU_CMD_INCLOUD) {

				}
			}

		};

		@Override
		protected Integer doInBackground(String... strings) {

			strCommand = strings[0];

			try {
				if (strCommand == CommUtil.ATU_CMD_FIND) {
					atus = Config.loadArray(MainActivity.this, Config.ATUS);
					if (atus.size() > 0) {
						for (final AtuInfo atu : atus) {
							String boardip = getBroadcast();
							MyApp.getAppInstance().setComm(new CommUtil());
							Log.i("MainFindatu", "FindSingleAtu from :"
									+ boardip);
							MyApp.getAppInstance()
									.getComm()
									.FindSingleAtuCmd(atu.getAtuId(), boardip,
											9559, myCallback);
							AndroidUtil.Sleep(200);
						}
					}

				} else if (strCommand == CommUtil.ATU_CMD_LOGIN) {
				} else if (strCommand == CommUtil.ATU_CMD_INCLOUD) {

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;
		}

		@Override
		protected void onProgressUpdate(String... svInfo) {

		}

		@Override
		protected void onPostExecute(Integer result) {
			if (strCommand == CommUtil.ATU_CMD_FIND) {
//				atus = Config.loadArray(MainActivity.this, Config.ATUS);
//				if (apps.size() > 0) {
//					for (final AtuInfo app : apps) {
//						int isEx = 0;
//						int i = 0;
//						for (final AtuInfo atu : atus) {
//							i++;
//							if (app.getAtuId().equals(atu.getAtuId())) {
//								isEx = 1;
//								// app.setAtuCode(atu.getAtuCode());
//								// app.setAtuName(atu.getAtuName());
//								// app.setAtuRoom(atu.getAtuRoom());
//								// app.setAtuPwd(atu.getAtuPwd());
//								atu.setAtuRemoIp(app.getAtuRemoIp());
//								atu.setAtuRemoPort(app.getAtuRemoPort());
//								atu.setAtuIp(app.getAtuIp());
//								atu.setIsRemote(Config.LAN_CTR_MODE);
//								break;
//							}
//						}
//						if (isEx == 0) {
//							atus.add(app);
//						}
//					}
					Config.saveArray(MainActivity.this, Config.ATUS, atus);
					if (mContent instanceof FragmentDevices) {
						FragmentDevices.refListView();
					}
//				}

			} else if (strCommand == CommUtil.ATU_CMD_LOGIN) {

			} else if (strCommand == CommUtil.ATU_CMD_INCLOUD) {

			}
		}
	}

	// static InetAddress getBroadcastAddress(Context context) throws
	// IOException {
	// WifiManager wifi = (WifiManager) context
	// .getSystemService(Context.WIFI_SERVICE);
	// DhcpInfo dhcp = wifi.getDhcpInfo();
	// // handle null somehow
	//
	// int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
	// byte[] quads = new byte[4];
	// for (int k = 0; k < 4; k++)
	// quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	// return InetAddress.getByAddress(quads);
	// }

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

	/**
	 * 左侧菜单点击切换首页的内容
	 */

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

}
