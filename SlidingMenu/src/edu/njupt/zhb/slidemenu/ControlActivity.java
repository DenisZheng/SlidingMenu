package edu.njupt.zhb.slidemenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.njupt.zhb.activity.BaseActivity;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.AreasInfo;
import edu.njupt.zhb.bean.AtuInfo;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.comm.AtuUdpInterface;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.comm.DaiKin;
import edu.njupt.zhb.comm.DaiKinStatus;
import edu.njupt.zhb.comm.LoadingDialogFragment;
import edu.njupt.zhb.comm.VerticalSeekBar;
import edu.njupt.zhb.comm.VerticalSeekBar.OnSeekBarChangeListener;
import edu.njupt.zhb.utils.AndroidUtil;

public class ControlActivity extends FragmentActivity {
	private static AtuInfo atu = new AtuInfo();
	private PlaceholderFragment phFragment = null;
	private static DaiKinStatus dkStatus = new DaiKinStatus();
	private static DaiKinStatus setStatus = new DaiKinStatus();
	private Timer mTimer = null;

	private boolean boolRunning = true;
	private boolean bFirstRun = true;
	private boolean bNeeRead = false;
	private static TextView textRoomTemp = null;
	private static TextView textpower = null;
	private static TextView textSetTemp = null;
	private static TextView textMode = null;
	private static TextView textFan = null;
	private static TextView textFix = null;
	private static LinearLayout powerlay = null;
	private static VerticalSeekBar vsk = null;
	private static boolean s_bNeedLock = false;
	private static boolean s_bNeedWrite = false;
	private static boolean bSlowCtr = false;
	private static String airName = null;
	private static String roomName = null;
	private String controlIp = null;
	private int controlPort = 9559;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_control);
		// super.onCreate(savedInstanceState);
		atu = (AtuInfo) getIntent().getSerializableExtra(Config.ATUS);
		if (atu.getIsRemote() == Config.LAN_CTR_MODE) {
			controlIp = atu.getAtuIp();
			controlPort = 9559;
		} else if (atu.getIsRemote() == Config.REMO_CTR_MODE) {
			controlIp = atu.getAtuRemoIp();
			controlPort = atu.getAtuRemoPort();
		} else if (atu.getIsRemote() == Config.SLOW_CTR_MODE) {
			bSlowCtr = true;
		}
		roomName = (String) getIntent().getStringExtra("RoomName");

		if (phFragment == null) {
			phFragment = new PlaceholderFragment();
		}
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.air_container, phFragment).commit();
		}
		setStatus.SetTemp = DaiKin.TEMP_MIN;
		dkStatus.SetTemp = DaiKin.TEMP_MIN;

		mTimer = new Timer(true);
		mTimer.schedule(task, 1000, 1000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		bFirstRun = true;
		setStatus.Timer = 0;
		bNeeRead = true;
		boolRunning = true;
	}

	@Override
	protected void onPause() {
		boolRunning = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	/************************************************************************************************/
	private class UdpAsyncTask extends AsyncTask<String, String, Integer> {
		private String strCommand = "";
		private int iReadWrite = 0; // Read
		LoadingDialogFragment dialog = LoadingDialogFragment
				.newInstance("努力加载中....");

		// List<DeviceInfo> devs = new ArrayList<DeviceInfo>();

		private int HexStringToInt(String str) {
			int i = 0;
			if (str == null) {
				return 0;
			}
			str = str.replace("\r", "0");
			str = str.replace("\n", "0");
			try {
				i = Integer.parseInt(str, 16);
			} catch (Exception ex) {
			}
			return i;
		}

		private boolean CheckSameStatus() {
			if (setStatus.SetTemp != dkStatus.SetTemp)
				return false;
			if (setStatus.Power != dkStatus.Power)
				return false;
			if (setStatus.RunMode != dkStatus.RunMode)
				return false;
			if (setStatus.FanScan != dkStatus.FanScan)
				return false;
			if (setStatus.FanSpeed != dkStatus.FanSpeed)
				return false;
			return true;
		}

		private UdpAsyncTask() {
			super();
		}

		private AtuUdpInterface myCallback = new AtuUdpInterface() {
			@Override
			public void ReceiveCallback(String ip, String strEcho) {
				if (strCommand == CommUtil.ATU_CMD_FIND) {

				} else if (strCommand == CommUtil.ATU_CMD_LOGIN) {
				} else if (strCommand == CommUtil.ATU_CMD_INCLOUD) {
					if (iReadWrite == 0) { // Read data
						String strValue[] = strEcho.split(",");
						if (strValue.length >= 17) {
							int i = 1;
							airName = strValue[0];
							dkStatus.SetTemp = HexStringToInt(strValue[i + 0]);
							dkStatus.Power = HexStringToInt(strValue[i + 6]);
							dkStatus.RunMode = HexStringToInt(strValue[i + 7]);
							dkStatus.FanSpeed = HexStringToInt(strValue[i + 8]) >> 4;
							dkStatus.FanScan = HexStringToInt(strValue[i + 8]) & 0x0F;
							dkStatus.RoomTemp = HexStringToInt(strValue[i + 1]);
							if (strValue[i + 16].contains("1")) {
								dkStatus.Locked = true;
							} else {
								dkStatus.Locked = false;
							}
							dkStatus.Alarm = "";
							try {
								char[] c = new char[2];
								c[0] = (char) HexStringToInt(strValue[i + 14]);
								c[1] = (char) HexStringToInt(strValue[i + 15]);
								if ((c[0] != 0) && (c[1] != 0)) {
									dkStatus.Alarm = new String(c);
								}
							} catch (Exception ex) {

							}
							setStatus.Alarm = dkStatus.Alarm;
							setStatus.Locked = dkStatus.Locked;
							setStatus.RoomTemp = dkStatus.RoomTemp;
							if (bFirstRun) {
								setStatus.SetTemp = dkStatus.SetTemp;
								setStatus.Power = dkStatus.Power;
								setStatus.RunMode = dkStatus.RunMode;
								setStatus.FanScan = dkStatus.FanScan;
								setStatus.FanSpeed = dkStatus.FanSpeed;
							}
						}
					}
				} else if (strCommand == CommUtil.ATU_CTR_SLOW) {
					if (iReadWrite == 0) { // Read data
						String strValue[] = strEcho.split(";");
						String Status[] = strValue[1].split("_");
						String staParm[] = Status[1].split(",");
						if (staParm.length >= 16) {
							int i = 0;
							// airName = strValue[0];
							dkStatus.SetTemp = HexStringToInt(staParm[i + 0]);
							dkStatus.RoomTemp = HexStringToInt(staParm[i + 1]);
							dkStatus.Power = HexStringToInt(staParm[i + 6]);
							dkStatus.RunMode = HexStringToInt(staParm[i + 7]);
							dkStatus.FanSpeed = HexStringToInt(staParm[i + 8]) >> 4;
							dkStatus.FanScan = HexStringToInt(staParm[i + 8]) & 0x0F;

							if (staParm[i + 15].contains("1")) {
								dkStatus.Locked = true;
							} else {
								dkStatus.Locked = false;
							}
							dkStatus.Alarm = "";
							try {
								char[] c = new char[2];
								c[0] = (char) HexStringToInt(staParm[i + 13]);
								c[1] = (char) HexStringToInt(staParm[i + 14]);
								if ((c[0] != 0) && (c[1] != 0)) {
									dkStatus.Alarm = new String(c);
								}
							} catch (Exception ex) {

							}
							setStatus.Alarm = dkStatus.Alarm;
							setStatus.Locked = dkStatus.Locked;
							setStatus.RoomTemp = dkStatus.RoomTemp;
							if (bFirstRun) {
								setStatus.SetTemp = dkStatus.SetTemp;
								setStatus.Power = dkStatus.Power;
								setStatus.RunMode = dkStatus.RunMode;
								setStatus.FanScan = dkStatus.FanScan;
								setStatus.FanSpeed = dkStatus.FanSpeed;
							}
						}
					}
				}
			}

		};

		@Override
		protected void onPreExecute() {
			if (iReadWrite == 0) {
				if (bFirstRun) {
					FragmentTransaction ft = getSupportFragmentManager()
							.beginTransaction();
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					dialog.show(ft, "fragmentDialog");
				}
			}
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... strings) {

			strCommand = strings[0];
			if (!bSlowCtr) {
				MyApp.getAppInstance().setComm(
						new CommUtil(controlIp, controlPort, myCallback));
				MyApp.getAppInstance().getComm().aut_ser = atu.getAtuId();
				MyApp.getAppInstance().getComm().login_id = "admin_"
						+ atu.getAtuPwd();
			}
			try {
				if (strCommand == CommUtil.ATU_CMD_FIND) {

				} else if (strCommand == CommUtil.ATU_CMD_LOGIN) {
					// return myClient.LoginAtuServer(MyApp.curServer);
				} else if (strCommand == CommUtil.ATU_CMD_INCLOUD) {
					if (strings[1] == DaiKin.CMD_GET_DATA) {
						iReadWrite = 0;
						return MyApp
								.getAppInstance()
								.getComm()
								.SendInCloudCommand(
										DaiKin.CMD_GET_DATA + roomName + ",");

					} else if (strings[1] == DaiKin.CMD_SET_DATA) {
						iReadWrite = 1;
						return MyApp
								.getAppInstance()
								.getComm()
								.SendInCloudCommand(
										DaiKin.CMD_SET_DATA + roomName + ","
												+ airName + "," + strings[2]);
					} else { // other command
						iReadWrite = 2;
						iReadWrite = 1;
						return MyApp.getAppInstance().getComm()
								.SendInCloudCommand(strings[1]);

					}

				} else if (strCommand == CommUtil.ATU_CTR_SLOW) {
					String mobilenum = AndroidUtil
							.GetUserName(ControlActivity.this);
					String passwd = AndroidUtil
							.GetPassWord(ControlActivity.this);
					if (strings[1] == DaiKin.CMD_GET_DATA) {
						iReadWrite = 0;
						return Config
								.RemControl(mobilenum, passwd, atu.getAtuId(),
										atu.getAtuPwd(), "", myCallback);

					} else if (strings[1] == DaiKin.CMD_SET_DATA) {
						iReadWrite = 1;
						return Config
								.RemControl(mobilenum, passwd, atu.getAtuId(),
										atu.getAtuPwd(), atu.getAtuCode() + "_"
												+ strings[2], myCallback);
					}
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
			Toast to = null;
			if (strCommand == CommUtil.ATU_CMD_FIND) {
			} else if (strCommand == CommUtil.ATU_CMD_LOGIN) {
				if (result > 0) { // Login success
					// MyApp.curServer.status = ServerStatus.LOGIN_IN;
					bNeeRead = true;
				} else {
					to = Toast.makeText(ControlActivity.this, "未登录",
							Toast.LENGTH_SHORT);
				}
			} else if (strCommand == CommUtil.ATU_CMD_INCLOUD) {
				if (result > 0) { // Command success
					if (iReadWrite == 0) { // read
						bNeeRead = false;
						if (bFirstRun) {
							dialog.dismiss();
							bFirstRun = false;
							SetRunMode();
						}
						phFragment.ShowStatus();
						if (CheckSameStatus()) {
							setStatus.Timer = 0;
						}
					} else if (iReadWrite == 1) { // Control success

						setStatus.Timer = 60;
					} else { // other command
						setStatus.Timer = 60;
					}
				} else if (result == -200) { // no login
					// MyApp.curServer.status = ServerStatus.IDLE;
				} else {
					if (iReadWrite == 0) {

					} else if (iReadWrite == 1) {

						// to = Toast.makeText(ControlActivity.this,
						// "失败" + result.toString(), Toast.LENGTH_SHORT);
					}
				}
			} else if (strCommand == CommUtil.ATU_CTR_SLOW) {
				if (result > 0) { // Command success
					if (iReadWrite == 0) { // read
						bNeeRead = false;
						if (bFirstRun) {
							dialog.dismiss();
							bFirstRun = false;
							SetRunMode();
						}
						phFragment.ShowStatus();
						if (CheckSameStatus()) {
							setStatus.Timer = 0;
						}
					} else if (iReadWrite == 1) { // Control success
						setStatus.Timer = 60;
					} else { // other command
						setStatus.Timer = 60;
					}
				} else if (result == -200) { // no login
					// MyApp.curServer.status = ServerStatus.IDLE;
				} else {
					if (iReadWrite == 0) {

					} else if (iReadWrite == 1) {

						// to = Toast.makeText(ControlActivity.this,
						// "失败" + result.toString(), Toast.LENGTH_SHORT);
					}
				}
			}
			if (to != null)
				to.show();
		}
	}

	TimerTask task = new TimerTask() { // 1 second interrupt
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	final android.os.Handler handler = new android.os.Handler() {
		int iTimer10 = 0;
		int iTimer5 = 0;
		int iTimer2 = 0;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // 1 sencond routine
				if (boolRunning == true) {
					iTimer2++;
					iTimer5++;
					iTimer10++;
					if (bNeeRead) {
						bNeeRead = false;
						iTimer10 = 0;
						UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
						if (bSlowCtr) {
							mUdpAsyncTask.execute(CommUtil.ATU_CTR_SLOW,
									DaiKin.CMD_GET_DATA);
						} else {
							mUdpAsyncTask.execute(CommUtil.ATU_CMD_INCLOUD,
									DaiKin.CMD_GET_DATA);
						}
					}
					if (s_bNeedLock) {
						s_bNeedLock = false;
					}
					if (setStatus.Timer > 0) {
						setStatus.Timer--;
					}
					if (iTimer2 >= 2) { // 2 second
						iTimer2 = 0;
						if ((CheckControl() == true) && (s_bNeedWrite == true)) {
							s_bNeedWrite = false;
							iTimer5 = 0;
							iTimer10 = 0;
							UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
							if (bSlowCtr) {
								mUdpAsyncTask.execute(CommUtil.ATU_CTR_SLOW,
										DaiKin.CMD_SET_DATA,
										DaiKin.BuildDataString(setStatus));
							} else {
								mUdpAsyncTask.execute(CommUtil.ATU_CMD_INCLOUD,
										DaiKin.CMD_SET_DATA,
										DaiKin.BuildDataString(setStatus));
							}
						}
					}
					if (iTimer5 >= 11) { // 10 second
						iTimer5 = 0;
						if (!bSlowCtr) {
							if (textRoomTemp != null) {
								textRoomTemp.setText(String
										.valueOf(dkStatus.RoomTemp));
							}
							if (setStatus.Timer > 0) { // I am wait echo,
														// refresh
														// data ever 5 second
								iTimer10 = 0;
								UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
								mUdpAsyncTask.execute(CommUtil.ATU_CMD_INCLOUD,
										DaiKin.CMD_GET_DATA);
							}
						}
					}
					if (iTimer10 >= 40) { // 40s
						if (!bSlowCtr) {
							iTimer10 = 0; // Refresh data ever 30 second
							bFirstRun = true; // refresh status
							UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
							mUdpAsyncTask.execute(CommUtil.ATU_CMD_INCLOUD,
									DaiKin.CMD_GET_DATA);
						}
					}
					if (iTimer10 >= 60) {
						iTimer10 = 0;
						bFirstRun = true;
						UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
						mUdpAsyncTask.execute(CommUtil.ATU_CTR_SLOW,
								DaiKin.CMD_GET_DATA);
					}
				} else {
					iTimer5 = 0;
				}
				return;
			case 2: // 300ms
				break;
			}
			super.handleMessage(msg);
		}
	};

	private boolean CheckControl() {
		if (dkStatus.SetTemp != setStatus.SetTemp)
			return true;
		if (dkStatus.Power != setStatus.Power)
			return true;
		if (dkStatus.RunMode != setStatus.RunMode)
			return true;
		if (dkStatus.FanScan != setStatus.FanScan)
			return true;
		if (dkStatus.FanSpeed != setStatus.FanSpeed)
			return true;
		return false;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private View rootView;
		private boolean bShowChanged = false;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.air_box, container, false);

			textRoomTemp = (TextView) rootView.findViewById(R.id.currTempval);
			textpower = (TextView) rootView.findViewById(R.id.TxtPower);
			textSetTemp = (TextView) rootView.findViewById(R.id.txtTempValue);
			textMode = (TextView) rootView.findViewById(R.id.txtCurrentMode);
			textFan = (TextView) rootView.findViewById(R.id.txtCurrentFan);
			textFix = (TextView) rootView.findViewById(R.id.txtCurrentFix);
			powerlay = (LinearLayout) rootView.findViewById(R.id.powerlay);
			vsk = (VerticalSeekBar) rootView.findViewById(R.id.mskb);

			powerlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (setStatus.Power == 1) {
						setStaVal(0);
					} else {
						setStaVal(1);
					}
				}
			});

			// Button onBtn = (Button) rootView.findViewById(R.id.btnPower);
			// onBtn.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// // GetComm().SendTo(sta, "01", "0001");
			// if (setStatus.Power == 1) {
			// setStaVal(0);
			// } else {
			// setStaVal(1);
			// }
			//
			// }
			// });

			Button upBtn = (Button) rootView.findViewById(R.id.btnUp);

			upBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setStatus.SetTemp = setStatus.SetTemp + 1;
					if (setStatus.SetTemp > setStatus.ColdTempMax)
						setStatus.SetTemp = setStatus.ColdTempMax;
					if (setStatus.SetTemp < setStatus.ColdTempMin)
						setStatus.SetTemp = setStatus.ColdTempMin;

					setSetTmpVal(setStatus.SetTemp);
				}
			});

			Button downBtn = (Button) rootView.findViewById(R.id.btnDown);

			downBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setStatus.SetTemp = setStatus.SetTemp - 1;
					if (setStatus.SetTemp > setStatus.ColdTempMax)
						setStatus.SetTemp = setStatus.ColdTempMax;
					if (setStatus.SetTemp < setStatus.ColdTempMin)
						setStatus.SetTemp = setStatus.ColdTempMin;
					setSetTmpVal(setStatus.SetTemp);
				}
			});

			Button zlBtn = (Button) rootView.findViewById(R.id.btnCold);
			zlBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setModeVal(DaiKin.RUN_MODE_COLD);
				}
			});
			Button zrBtn = (Button) rootView.findViewById(R.id.btnHot);
			zrBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setModeVal(DaiKin.RUN_MODE_HEAT);
				}
			});

			Button sfBtn = (Button) rootView.findViewById(R.id.btnFan);
			sfBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setModeVal(DaiKin.RUN_MODE_FAN);
				}
			});

			Button csBtn = (Button) rootView.findViewById(R.id.btnDehu);
			csBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setModeVal(DaiKin.RUN_MODE_DRY);
				}
			});

			Button wfBtn = (Button) rootView.findViewById(R.id.btnSlow);
			wfBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SetFanVal(DaiKin.FAN_SPEED_MIN);
				}
			});
			Button jfBtn = (Button) rootView.findViewById(R.id.btnCenter);
			jfBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SetFanVal(3);
				}
			});
			Button qfBtn = (Button) rootView.findViewById(R.id.btnStrong);
			qfBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SetFanVal(DaiKin.FAN_SPEED_MAX);
				}
			});

			Button fixBtn = (Button) rootView.findViewById(R.id.btnFix);
			fixBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (setStatus.FanScan == DaiKin.FAN_FIX) {
						setFixVal(DaiKin.FAN_SCAN);
					} else {
						setFixVal(DaiKin.FAN_FIX);
					}

				}
			});

			vsk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {
					// TODO Auto-generated method stub
					setStatus.SetTemp = VerticalSeekBar.getProgress() + 16;
					setSetTmpVal(setStatus.SetTemp);
				}

				@Override
				public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
					// TODO Auto-generated method stub
					textSetTemp.setText(String.valueOf(VerticalSeekBar
							.getProgress() + 16));
				}

				@Override
				public void onProgressChanged(VerticalSeekBar VerticalSeekBar,
						int progress, boolean fromUser) {
					// TODO Auto-generated method stub
					textSetTemp.setText(String.valueOf(VerticalSeekBar
							.getProgress() + 16));
				}
			});

			ShowStatus();
			return rootView;
		}

		@Override
		public void onResume() {
			super.onResume();
		}

		public void ShowStatus() {
			if ((dkStatus == null) || (rootView == null)) {
				return;
			}

			bShowChanged = true;

			if (setStatus.Power == 1) {
				textpower.setText("开");
				powerlay.setBackgroundResource(R.drawable.operation_page_indoor_data_show_bottom_bg_press);
			} else {
				textpower.setText("关");
				powerlay.setBackgroundResource(R.drawable.operation_page_indoor_data_show_bottom_bg);
			}
			SetTxtColor(textpower, setStatus.Power);
			if (textSetTemp != null) {
				vsk.setProgress(setStatus.SetTemp - 16);
				textSetTemp.setText(String.valueOf(setStatus.SetTemp));
			}

			if (textRoomTemp != null)
				textRoomTemp.setText(String.valueOf(setStatus.RoomTemp));

			if (setStatus.RunMode == DaiKin.RUN_MODE_COLD) {
				textMode.setText("制  冷");
			}
			if (setStatus.RunMode == DaiKin.RUN_MODE_HEAT) {
				textMode.setText("制  热");
			}
			if (setStatus.RunMode == DaiKin.RUN_MODE_FAN) {
				textMode.setText("送  风");
			}
			if (setStatus.RunMode == DaiKin.RUN_MODE_DRY) {
				textMode.setText("除  湿");
			}
			if (setStatus.RunMode == DaiKin.RUN_MODE_AUTO) {
				textMode.setText("自  动");
			}
			SetTxtColor(textMode, setStatus.RunMode);
			if (setStatus.FanSpeed == DaiKin.FAN_SPEED_MIN) {
				textFan.setText("微  风");
			}
			if (setStatus.FanSpeed > DaiKin.FAN_SPEED_MIN
					& setStatus.FanSpeed < DaiKin.FAN_SPEED_MAX) {
				textFan.setText("中  风");
			}
			if (setStatus.FanSpeed == DaiKin.FAN_SPEED_MAX) {
				textFan.setText("强  风");
			}
			SetTxtColor(textFan, setStatus.FanSpeed);
			if (setStatus.FanScan == DaiKin.FAN_FIX) {
				textFix.setText("定  风");
			}
			if (setStatus.FanScan == DaiKin.FAN_SCAN) {
				textFix.setText("扫  风");
			}
			SetTxtColor(textFix, setStatus.FanScan);
			bShowChanged = false;
		}

		/*******************************************************************************************************
		 * GUI initialize
		 */

		private void setStaVal(int i) {
			if (i == 1) {
				setStatus.Power = 1;
				textpower.setText("开");
				SetTxtColor(textpower, 1);
				powerlay.setBackgroundResource(R.drawable.operation_page_indoor_data_show_bottom_bg_press);
			} else {
				setStatus.Power = 0;
				textpower.setText("关");
				SetTxtColor(textpower, 0);
				powerlay.setBackgroundResource(R.drawable.operation_page_indoor_data_show_bottom_bg);
			}
			s_bNeedWrite = true;
		}

		private void setFixVal(int i) {
			if (i == DaiKin.FAN_SCAN) {
				setStatus.FanScan = DaiKin.FAN_SCAN;
				textFix.setText("扫  风");
				SetTxtColor(textFix, DaiKin.FAN_SCAN);
			} else {
				setStatus.FanScan = DaiKin.FAN_FIX;
				textFix.setText("定  风");
				SetTxtColor(textFix, DaiKin.FAN_FIX);
			}
			s_bNeedWrite = true;
		}

		private void setSetTmpVal(int i) {
			vsk.setProgress(i - 16);
			textSetTemp.setText(String.valueOf(i));
			s_bNeedWrite = true;
		}

		private void setModeVal(int i) {
			setStatus.RunMode = i;
			if (i == DaiKin.RUN_MODE_COLD) {
				textMode.setText("制  冷");
			}
			if (i == DaiKin.RUN_MODE_HEAT) {
				textMode.setText("制  热");
			}
			if (i == DaiKin.RUN_MODE_FAN) {
				textMode.setText("送  风");
			}
			if (i == DaiKin.RUN_MODE_DRY) {
				textMode.setText("除  湿");
			}
			SetTxtColor(textMode, i);
			s_bNeedWrite = true;
		}

		private void SetFanVal(int i) {
			setStatus.FanSpeed = i;
			if (i == DaiKin.FAN_SPEED_MIN) {
				textFan.setText("微  风");
			}
			if (i > DaiKin.FAN_SPEED_MIN & i < DaiKin.FAN_SPEED_MAX) {
				textFan.setText("中  风");
			}
			if (i == DaiKin.FAN_SPEED_MAX) {
				textFan.setText("强  风");
			}
			SetTxtColor(textFan, i);
			s_bNeedWrite = true;
		}

		private void SetTxtColor(TextView tv, int airVal) {
			if (airVal == 0) {
				tv.setTextColor(this.getResources().getColor(R.color.air_off));
			}
			// if(airVal==1){
			// tv.setTextColor(this.getResources().getColor(R.color.air_fix));
			// }
			if (airVal == DaiKin.FAN_SCAN) {
				tv.setTextColor(this.getResources().getColor(R.color.air_scan));
			}
			if (airVal == DaiKin.RUN_MODE_COLD) {
				tv.setTextColor(this.getResources().getColor(R.color.air_cold));
			}
			if (airVal == DaiKin.RUN_MODE_HEAT) {
				tv.setTextColor(this.getResources().getColor(R.color.air_hot));
			}
			if (airVal == DaiKin.RUN_MODE_FAN) {
				tv.setTextColor(this.getResources().getColor(R.color.air_fan));
			}
			if (airVal == DaiKin.RUN_MODE_DRY) {
				tv.setTextColor(this.getResources().getColor(R.color.air_dehu));
			}
			if (airVal == DaiKin.RUN_MODE_AUTO) {
				tv.setTextColor(this.getResources().getColor(R.color.air_off));
			}
			if (airVal == DaiKin.FAN_SPEED_MIN) {
				tv.setTextColor(this.getResources().getColor(R.color.air_slow));
			}
			if (airVal == 3) {
				tv.setTextColor(this.getResources()
						.getColor(R.color.air_center));
			}
			if (airVal == DaiKin.FAN_SPEED_MAX) {
				tv.setTextColor(this.getResources()
						.getColor(R.color.air_strong));
			}
		}
		/*******************************************************************************************************/
	}

	public void SetRunMode() {
		// TODO Auto-generated method stub
		if (dkStatus.Power == 1) {
			textpower.setText("开");
			powerlay.setBackgroundResource(R.drawable.operation_page_indoor_data_show_bottom_bg_press);
		} else {
			textpower.setText("关");
			powerlay.setBackgroundResource(R.drawable.operation_page_indoor_data_show_bottom_bg);
		}
		SetRunColor(textpower, dkStatus.Power);
		if (textSetTemp != null) {
			vsk.setProgress(dkStatus.SetTemp - 16);
			textSetTemp.setText(String.valueOf(dkStatus.SetTemp));
		}

		if (textRoomTemp != null)
			textRoomTemp.setText(String.valueOf(dkStatus.RoomTemp));

		if (dkStatus.RunMode == DaiKin.RUN_MODE_COLD) {
			textMode.setText("制  冷");
		}
		if (dkStatus.RunMode == DaiKin.RUN_MODE_HEAT) {
			textMode.setText("制  热");
		}
		if (dkStatus.RunMode == DaiKin.RUN_MODE_FAN) {
			textMode.setText("送  风");
		}
		if (dkStatus.RunMode == DaiKin.RUN_MODE_DRY) {
			textMode.setText("除  湿");
		}
		if (dkStatus.RunMode == DaiKin.RUN_MODE_AUTO) {
			textMode.setText("自  动");
		}
		SetRunColor(textMode, dkStatus.RunMode);
		if (dkStatus.FanSpeed == DaiKin.FAN_SPEED_MIN) {
			textFan.setText("微  风");
		}
		if (dkStatus.FanSpeed > DaiKin.FAN_SPEED_MIN
				& dkStatus.FanSpeed < DaiKin.FAN_SPEED_MAX) {
			textFan.setText("中  风");
		}
		if (dkStatus.FanSpeed == DaiKin.FAN_SPEED_MAX) {
			textFan.setText("强  风");
		}
		SetRunColor(textFan, dkStatus.FanSpeed);
		if (dkStatus.FanScan == DaiKin.FAN_FIX) {
			textFix.setText("定  风");
		}
		if (dkStatus.FanScan == DaiKin.FAN_SCAN) {
			textFix.setText("扫  风");
		}
		SetRunColor(textFix, dkStatus.FanScan);
		s_bNeedWrite = true;
	}

	private void SetRunColor(TextView tv, int airVal) {
		if (airVal == 0) {
			tv.setTextColor(this.getResources().getColor(R.color.air_off));
		}
		if (airVal == 1) {
			tv.setTextColor(this.getResources().getColor(R.color.air_fix));
		}
		if (airVal == DaiKin.FAN_SCAN) {
			tv.setTextColor(this.getResources().getColor(R.color.air_scan));
		}
		if (airVal == DaiKin.RUN_MODE_COLD) {
			tv.setTextColor(this.getResources().getColor(R.color.air_cold));
		}
		if (airVal == DaiKin.RUN_MODE_HEAT) {
			tv.setTextColor(this.getResources().getColor(R.color.air_hot));
		}
		if (airVal == DaiKin.RUN_MODE_FAN) {
			tv.setTextColor(this.getResources().getColor(R.color.air_fan));
		}
		if (airVal == DaiKin.RUN_MODE_DRY) {
			tv.setTextColor(this.getResources().getColor(R.color.air_dehu));
		}
		if (airVal == DaiKin.RUN_MODE_AUTO) {
			tv.setTextColor(this.getResources().getColor(R.color.air_off));
		}
		if (airVal == DaiKin.FAN_SPEED_MIN) {
			tv.setTextColor(this.getResources().getColor(R.color.air_slow));
		}
		if (airVal == 3) {
			tv.setTextColor(this.getResources().getColor(R.color.air_center));
		}
		if (airVal == DaiKin.FAN_SPEED_MAX) {
			tv.setTextColor(this.getResources().getColor(R.color.air_strong));
		}
	}
}
