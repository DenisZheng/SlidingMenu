package edu.njupt.zhb.view;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.njupt.zhb.slidemenu.R;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.BaseInfo;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.ShowInfo;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.DaiKin;
import edu.njupt.zhb.comm.DaiKinStatus;

public class AirView extends BaseView {
	private DataModel getShowInfo() {
		return (DataModel) this.GetInfo();
	}

	public AirView(Context context, DataModel _info) {
		super(context, _info, R.layout.air_box);
		// TODO Auto-generated constructor stub
	}

	private DaiKinStatus dkStatus = new DaiKinStatus();
	private static DaiKinStatus setStatus = new DaiKinStatus();
	private Timer mTimer = null;
	private Timer timer300ms = null;
	private TimerTask tmTask300ms = null;
	private boolean boolRunning = true;
	// private static Button btApply = null;
	private boolean bFirstRun = true;
	private boolean bNeeRead = false;
	private static TextView textAlarm = null;
	private static TextView textRoomTemp = null;
	private static TextView textpower = null;
	private static TextView textSetTemp = null;
	private static TextView textMode = null;
	private static TextView textFan = null;
	// private static LinearLayout linearLayoutWaring = null;
	private static boolean s_bNeedLock = false;
	private static boolean s_bNeedWrite = false;
    
	private static String atuIp = null;
	private static String atuId = null;
	private static String atuPwd = null;
	private static String airName = null;
	private static String roomName = null;

	public void InitView() {
		// TODO Auto-generated method stub
		// 大金空调,12, 24,00,00,00,00,01,62,51,00,00,00,00,00,00,00,;
		String statStr = this.getShowInfo().getState();
		String strValue[] = statStr.split(",");
		dkStatus.SetTemp = HexStringToInt(strValue[1]);
		dkStatus.Power = HexStringToInt(strValue[7]);
		dkStatus.RunMode = HexStringToInt(strValue[8]);
		dkStatus.FanSpeed = HexStringToInt(strValue[9]) >> 4;
		dkStatus.FanScan = HexStringToInt(strValue[9]) & 0x0F;
		dkStatus.RoomTemp = HexStringToInt(strValue[2]);

		atuIp = this.getShowInfo().getRevIp();
		atuId = this.getShowInfo().getKeys();
		atuPwd = this.getShowInfo().getDevNum();
		airName = this.getShowInfo().getName();
		roomName = this.getShowInfo().getOtherName();

		textRoomTemp = (TextView) view.findViewById(R.id.currTempval);
		textpower = (TextView) view.findViewById(R.id.TxtPower);
		textSetTemp = (TextView) view.findViewById(R.id.txtTempValue);
		textMode = this.getTextViewById(R.id.txtCurrentMode);
		textFan = this.getTextViewById(R.id.txtCurrentFan);


		mTimer = new Timer(true);
		mTimer.schedule(task, 1000, 1000);

		ImageView onBtn = this.GetImageView(R.id.btnPower);
		onBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GetComm().SendTo(sta, "01", "0001");
				if (dkStatus.Power == 1) {
					setStaVal(0);
				} else {
					setStaVal(1);
				}

			}
		});

		ImageView upBtn = this.GetImageView(R.id.btnUp);

		upBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setStatus.SetTemp = dkStatus.SetTemp;
				setStatus.SetTemp = setStatus.SetTemp + 1;
				if (setStatus.SetTemp > setStatus.ColdTempMax)
					setStatus.SetTemp = setStatus.ColdTempMax;
				if (setStatus.SetTemp < setStatus.ColdTempMin)
					setStatus.SetTemp = setStatus.ColdTempMin;
				// DecimalFormat df = new DecimalFormat("0000");
				// GetComm().SendTo(st, "02",df.format(setTmp));
				setSetTmpVal(setStatus.SetTemp);
			}
		});

		ImageView downBtn = this.GetImageView(R.id.btnDown);

		downBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setStatus.SetTemp = dkStatus.SetTemp - 1;
				if (setStatus.SetTemp > setStatus.ColdTempMax)
					setStatus.SetTemp = setStatus.ColdTempMax;
				if (setStatus.SetTemp < setStatus.ColdTempMin)
					setStatus.SetTemp = setStatus.ColdTempMin;
				// DecimalFormat df = new DecimalFormat("0000");
				// GetComm().SendTo(st, "02",df.format(setTmp));
				setSetTmpVal(setStatus.SetTemp);
			}
		});

		ImageView zlBtn = this.GetImageView(R.id.btnCold);
		zlBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GetComm().SendTo(mode, "03","0001");
				setModeVal(DaiKin.RUN_MODE_COLD);
			}
		});
		ImageView zrBtn = this.GetImageView(R.id.btnHot);
		zrBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GetComm().SendTo(mode, "03","0002");
				setModeVal(DaiKin.RUN_MODE_HEAT);
			}
		});

		ImageView sfBtn = this.GetImageView(R.id.btnFan);
		sfBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GetComm().SendTo(mode, "03","0003");
				setModeVal(DaiKin.RUN_MODE_FAN);
			}
		});

		ImageView csBtn = this.GetImageView(R.id.btnDehu);
		csBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GetComm().SendTo(mode, "03","0004");
				setModeVal(DaiKin.RUN_MODE_DRY);
			}
		});

		ImageView wfBtn = this.GetImageView(R.id.btnSlow);
		wfBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GetComm().SendTo(fan, "04","0001");
				SetFanVal(DaiKin.FAN_SPEED_MIN);
			}
		});
		ImageView jfBtn = this.GetImageView(R.id.btnCenter);
		jfBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GetComm().SendTo(fan, "04","0002");
				SetFanVal(3);
			}
		});
		ImageView qfBtn = this.GetImageView(R.id.btnStrong);
		qfBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GetComm().SendTo(fan, "04","0003");
				SetFanVal(DaiKin.FAN_SPEED_MAX);
			}
		});

	}

	public ImageView GetImageView(int imageId) {
		ImageView staBtn = (ImageView) view.findViewById(imageId);
		return staBtn;

	}

	private void setStaVal(int i) {
		s_bNeedWrite=true;
		if (i == 1) {
			setStatus.Power = 1;
			textpower.setText("开");
		} else {
			setStatus.Power = 0;
			textpower.setText("关");
		}
	}

	private void setSetTmpVal(int i) {
		s_bNeedWrite=true;
		textSetTemp.setText(String.valueOf(i));
	}

	private void setModeVal(int i) {
		setStatus.RunMode = i;
		s_bNeedWrite=true;
		if (i == DaiKin.RUN_MODE_COLD) {
			textMode.setText("制冷模式");
		}
		if (i == DaiKin.RUN_MODE_HEAT) {
			textMode.setText("制热模式");
		}
		if (i == DaiKin.RUN_MODE_FAN) {
			textMode.setText("送风模式");
		}
		if (i == DaiKin.RUN_MODE_DRY) {
			textMode.setText("除湿模式");
		}
	}

	private void SetFanVal(int i) {
		s_bNeedWrite=true;
		setStatus.FanSpeed = i;
		setStatus.FanScan = dkStatus.FanScan;
		if (i == DaiKin.FAN_SPEED_MIN) {
			textFan.setText("微风");
		}
		if (i > DaiKin.FAN_SPEED_MIN & i < DaiKin.FAN_SPEED_MAX) {
			textFan.setText("中风");
		}
		if (i == DaiKin.FAN_SPEED_MAX) {
			textFan.setText("强风");
		}
	}

	public void SetSTA() {
		// TODO Auto-generated method stub
		// GetSTA();
		Start();

	}

	protected void GetSTA() {
		// TODO Auto-generated method stub
		// String r_sta = GetComm().Read(sta, "01");
		// if(r_sta.equals("")||r_sta.equals(null))r_sta="0000";
		// setStaVal(Integer.valueOf(r_sta));
		//
		// String r_st = GetComm().Read(st, "02");
		// if(r_st.equals("")||r_st.equals(null))r_st="2400";
		// setTmp = Integer.valueOf(r_st);
		// setSetTmpVal(setTmp/100);
		// String r_m = GetComm().Read(mode, "03");
		// if(r_m.equals("")||r_m.equals(null))r_m="0001";
		// setModeVal(Integer.valueOf(r_m));
		//
		// String r_f = GetComm().Read(fan, "04");
		// if(r_f.equals("")||r_f.equals(null))r_f="0001";
		// SetFanVal(Integer.valueOf(r_f));
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
					if (bNeeRead) { // want read after login
						// if(MyApp.curServer.status == ServerStatus.LOGIN_IN) {
						// iTimer10 = 0;
						// UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
						// mUdpAsyncTask.execute(AtuUdpClient.ATU_CMD_INCLOUD,
						// DaiKin.CMD_GET_DATA);
						// }
						// 读取状态
					}
					if (s_bNeedLock) {
						// s_bNeedLock = false;
						// if(MyApp.IsAdminLogin()) {
						// if (setStatus.Locked == true) {
						// UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
						// mUdpAsyncTask.execute(AtuUdpClient.ATU_CMD_INCLOUD,
						// "Lock,1");
						// } else {
						// UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
						// mUdpAsyncTask.execute(AtuUdpClient.ATU_CMD_INCLOUD,
						// "Lock,0");
						// }
						// }
					}
					// if(MyApp.curServer.status == ServerStatus.IDLE) {
					// iTimer2 = 0;
					// iTimer5 = 0;
					// iTimer10 = 0;
					// UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
					// mUdpAsyncTask.execute(AtuUdpClient.ATU_CMD_LOGIN);
					// }
					if (setStatus.Timer > 0) {
						setStatus.Timer--;
					}
					if (iTimer2 >= 2) { // 2 second
						iTimer2 = 0;
						// 与状态
						if ((CheckControl() == true) && (s_bNeedWrite == true)) {
							s_bNeedWrite = false;
							iTimer5 = 0;
							iTimer10 = 0;
							MyApp.getAppInstance().setComm(new CommUtil(atuIp,9559));
							MyApp.getAppInstance().getComm().aut_ser = atuId;
							MyApp.getAppInstance().getComm().login_id = "admin_"
									+ atuPwd;
							MyApp.getAppInstance()
									.getComm()
									.SendInCloudCommand(
											roomName
													+ ","
													+ airName
													+ ","
													+ DaiKin.BuildDataString(setStatus));

						}

					}
					if (iTimer5 >= 5) { // 5 second
						iTimer5 = 0;
						// if(textAlarm != null){
						// if(MyApp.CheckValueString(dkStatus.Alarm) == true){
						// textAlarm.setText(dkStatus.Alarm);
						// linearLayoutWaring.setVisibility(View.VISIBLE);
						// }else{
						// textAlarm.setText("");
						// linearLayoutWaring.setVisibility(View.INVISIBLE);
						// }
						// }
						if (textRoomTemp != null) {
							textRoomTemp.setText(dkStatus.RoomTemp);
						}

						if (setStatus.Timer > 0) { // I am wait echo, refresh
													// data ever 5 second
							iTimer10 = 0;
							// UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
							// mUdpAsyncTask.execute(AtuUdpClient.ATU_CMD_INCLOUD,
							// DaiKin.CMD_GET_DATA);
						}
					}
					if (iTimer10 >= 30) { // 30s
						iTimer10 = 0; // Refresh data ever 30 second
						bFirstRun = true; // refresh status
						// UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
						// mUdpAsyncTask.execute(AtuUdpClient.ATU_CMD_INCLOUD,
						// DaiKin.CMD_GET_DATA);
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
}
