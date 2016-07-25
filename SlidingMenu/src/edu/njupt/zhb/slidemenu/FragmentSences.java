package edu.njupt.zhb.slidemenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.google.gson.Gson;

import edu.njupt.zhb.slidemenu.FragmentSences.SenceAdapter.ViewHolder;
import edu.njupt.zhb.utils.AndroidUtil;
import edu.njupt.zhb.utils.PageAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.AtuInfo;
import edu.njupt.zhb.comm.AtuUdpInterface;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.comm.DaiKin;
import edu.njupt.zhb.comm.DaiKinStatus;
import edu.njupt.zhb.comm.MsgHandler;
import edu.njupt.zhb.utils.ImageUtil;
import edu.njupt.zhb.view.MyListView;
import edu.njupt.zhb.view.MyListView.OnRefreshListener;

/**
 * ListView
 * 
 * @author Administrator
 * 
 */
public class FragmentSences extends Fragment {

	// public Context views;
	public static View context;
	private static List<AtuInfo> atus;
	private Handler thandler;
	private Message msg;
	private static SenceAdapter mAdapter;
	private SwipeMenuListView mListView;
	private int oldPostion = -1;
	private static DaiKinStatus setStatus = new DaiKinStatus();
	private static DaiKinStatus dkStatus = new DaiKinStatus();

	public static final String ATU_CMD_GETSCENES = "getScenes,";
	public static final String ATU_CMD_RDSCENE = "rdScene,";
	public static final String ATU_CMD_WRSCENE = "wrScene,";
	public static final String ATU_CMD_DELSCENE = "delScene,";
	public static final String CMD_SET_DATA = "wrRoom,";
	public static String SceneName = "一键开启";
	public String DataStr = "";

	private boolean bIsScenes = false;
	private boolean bIsSData = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_sences, container, false);
		context = view;
		atus = Config.loadArray(context.getContext(), Config.ATUS);
		thandler = new MsgHandler(context.getContext());

		mListView = (SwipeMenuListView) context.findViewById(R.id.senceslist);
		mAdapter = new SenceAdapter();
		mListView.setAdapter(mAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem editItem = new SwipeMenuItem(context.getContext()
						.getApplicationContext());
				// set item background
				editItem.setBackground(new ColorDrawable(Color.GREEN));
				// set item width
				editItem.setWidth(dp2px(90));
				// set a icon
				editItem.setIcon(R.drawable.edit_selector);
				// add to menu
				menu.addMenuItem(editItem);
			}
		};

		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				AtuInfo item = atus.get(position);
				Gson gson = new Gson();
				String res = gson.toJson(item);
				switch (index) {
				case 0:
					if (oldPostion == position) {
						if (item.expand) {
							oldPostion = -1;
						}
						item.expand = !item.expand;
						UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
						mUdpAsyncTask.execute(ATU_CMD_RDSCENE, res,String.valueOf(position));
					} else {
						oldPostion = position;
						item.expand = true;
						item.expand = !item.expand;
						UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
						mUdpAsyncTask.execute(ATU_CMD_RDSCENE, res,String.valueOf(position));

					}
					int totalHeight = 0;
					for (int i = 0; i < mAdapter.getCount(); i++) {
						View viewItem = mAdapter.getView(i, null, mListView);// 这个很重要，那个展开的item的measureHeight比其他的大
						viewItem.measure(0, 0);
						totalHeight += viewItem.getMeasuredHeight();
					}

					ViewGroup.LayoutParams params = mListView.getLayoutParams();
					params.height = totalHeight
							+ (mListView.getDividerHeight() * (mListView
									.getCount() - 1));
					mListView.setLayoutParams(params);

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
				RunThread myThread = new RunThread(item);
				myThread.start();
			}

		});
		return view;
	}

	class RunThread extends Thread {
		private AtuInfo atu;
		private String strCommand;
		private String revStr;

		public RunThread(AtuInfo atu) {
			this.atu = atu;
		}

		private AtuUdpInterface runCallback = new AtuUdpInterface() {
			@Override
			public void ReceiveCallback(String ip, String strEcho) {
				if (strCommand == ATU_CMD_GETSCENES) {
					if (!strEcho.equals("")) {
						// bIsScenes = true;
						SceneName = strEcho.split(",")[0].trim();
					} else {
						// bIsScenes = false;
					}
				} else if (strCommand == ATU_CMD_RDSCENE) {
					revStr = strEcho.split(";")[0].trim();

				} else if (strCommand == ATU_CMD_WRSCENE) {

				} else if (strCommand == ATU_CMD_DELSCENE) {

				} else if (strCommand == CMD_SET_DATA) {

				}
			}

		};

		@Override
		public void run() {
			if (atu.getIsRemote() != Config.SLOW_CTR_MODE) {
				String itemip = "";
				int itemport = 0;
				if (atu.getIsRemote() == 1) {
					itemip = atu.getAtuRemoIp();
					itemport = atu.getAtuRemoPort();
				} else if (atu.getIsRemote() == 0) {
					itemip = atu.getAtuIp();
					itemport = 9559;
				}
				MyApp.getAppInstance().setComm(
						new CommUtil(itemip, itemport, runCallback));
				MyApp.getAppInstance().getComm().aut_ser = atu.getAtuId();
				MyApp.getAppInstance().getComm().login_id = "admin_"
						+ atu.getAtuPwd();
//				strCommand = ATU_CMD_GETSCENES;
//				int i = MyApp.getAppInstance().getComm()
//						.SendInCloudCommand(ATU_CMD_GETSCENES);
//				if (i > 0) {
					strCommand = ATU_CMD_RDSCENE;
					int j = MyApp
							.getAppInstance()
							.getComm()
							.SendInCloudCommand(
									ATU_CMD_RDSCENE + SceneName + ",");
					if (j > 0) {
						strCommand = CMD_SET_DATA;
						int k = MyApp.getAppInstance().getComm()
								.SendInCloudCommand(CMD_SET_DATA + revStr);
						if (k > 0) {
							msg = thandler.obtainMessage();
							msg.arg1 = 100;
							thandler.sendMessage(msg);
						}
					}
				} else {
					msg = thandler.obtainMessage();
					msg.arg1 = -107;
					thandler.sendMessage(msg);
					return;
				}
		}
	}

	class SenceAdapter extends BaseAdapter {

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
						.getApplicationContext(), R.layout.sence_item, null);
				new ViewHolder(convertView);
			}
			final ViewHolder holder = (ViewHolder) convertView.getTag();
			final AtuInfo item = getItem(position);
			Gson gson = new Gson();
			final String res = gson.toJson(item);
			holder.txtSencesName.setText(item.getAtuRoom() + SceneName);
			if (item.expand) {
				holder.settingitem.setVisibility(View.VISIBLE);
//				UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
//				mUdpAsyncTask.execute(ATU_CMD_RDSCENE, res,String.valueOf(position));
			} else {
				holder.settingitem.setVisibility(View.GONE);
			}

			if (dkStatus.Power == 1) {
				holder.rdon.setChecked(true);
				holder.rdoff.setChecked(false);
			} else {
				holder.rdon.setChecked(false);
				holder.rdoff.setChecked(true);
			}
			if (holder.tv_self != null) {
				holder.seekbar_self.setProgress(dkStatus.SetTemp - 16);
				holder.tv_self.setText(String.valueOf(dkStatus.SetTemp));
			}

			if (dkStatus.RunMode == DaiKin.RUN_MODE_COLD) {
				holder.rdcold.setChecked(true);
				holder.rdheat.setChecked(false);
				holder.rddehu.setChecked(false);
				holder.rdfan.setChecked(false);
			}
			if (dkStatus.RunMode == DaiKin.RUN_MODE_HEAT) {
				holder.rdcold.setChecked(false);
				holder.rdheat.setChecked(true);
				holder.rddehu.setChecked(false);
				holder.rdfan.setChecked(false);
			}
			if (dkStatus.RunMode == DaiKin.RUN_MODE_FAN) {
				holder.rdcold.setChecked(false);
				holder.rdheat.setChecked(false);
				holder.rddehu.setChecked(false);
				holder.rdfan.setChecked(true);
			}
			if (dkStatus.RunMode == DaiKin.RUN_MODE_DRY) {
				holder.rdcold.setChecked(false);
				holder.rdheat.setChecked(false);
				holder.rddehu.setChecked(true);
				holder.rdfan.setChecked(false);
			}

			if (dkStatus.FanSpeed == DaiKin.FAN_SPEED_MIN) {
				holder.rdlow.setChecked(true);
				holder.rdcenter.setChecked(false);
				holder.rdstrong.setChecked(false);
			}
			if (dkStatus.FanSpeed > DaiKin.FAN_SPEED_MIN
					& dkStatus.FanSpeed < DaiKin.FAN_SPEED_MAX) {
				holder.rdlow.setChecked(false);
				holder.rdcenter.setChecked(true);
				holder.rdstrong.setChecked(false);
			}
			if (dkStatus.FanSpeed == DaiKin.FAN_SPEED_MAX) {
				holder.rdlow.setChecked(false);
				holder.rdcenter.setChecked(false);
				holder.rdstrong.setChecked(true);
			}

			if (dkStatus.FanScan == DaiKin.FAN_FIX) {
				holder.rdfix.setChecked(true);
				holder.rdscan.setChecked(false);
			}
			if (dkStatus.FanScan == DaiKin.FAN_SCAN) {
				holder.rdfix.setChecked(false);
				holder.rdscan.setChecked(true);
			}
			final int index=position;
			holder.radonoff
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub
							if (checkedId == holder.rdon.getId()) {
								setStatus.Power = 1;
							} else if (checkedId == holder.rdoff.getId()) {
								setStatus.Power = 0;
							}
						}
					});
			holder.radmode
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub
							if (checkedId == holder.rdcold.getId()) {
								setStatus.RunMode = DaiKin.RUN_MODE_COLD;
							} else if (checkedId == holder.rdheat.getId()) {
								setStatus.RunMode = DaiKin.RUN_MODE_HEAT;
							} else if (checkedId == holder.rddehu.getId()) {
								setStatus.RunMode = DaiKin.RUN_MODE_DRY;
							} else if (checkedId == holder.rdfan.getId()) {
								setStatus.RunMode = DaiKin.RUN_MODE_FAN;
							}
						}
					});

			holder.radfan
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub
							if (checkedId == holder.rdlow.getId()) {
								setStatus.FanSpeed = DaiKin.FAN_SPEED_MIN;
							} else if (checkedId == holder.rdcenter.getId()) {
								setStatus.FanSpeed = 3;
							} else if (checkedId == holder.rdstrong.getId()) {
								setStatus.FanSpeed = DaiKin.FAN_SPEED_MAX;
							}
						}
					});

			holder.radfix
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub
							if (checkedId == holder.rdscan.getId()) {
								setStatus.FanScan = DaiKin.FAN_SCAN;
							} else if (checkedId == holder.rdfix.getId()) {
								setStatus.FanScan = DaiKin.FAN_FIX;
							}
						}
					});

			holder.seekbar_self
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							setStatus.SetTemp = seekBar.getProgress() + 16;
							holder.tv_self.setText(String.valueOf(seekBar
									.getProgress() + 16));
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							holder.tv_self.setText(String.valueOf(seekBar
									.getProgress() + 16));
						}

						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							// TODO Auto-generated method stub
							holder.tv_self.setText(String.valueOf(seekBar
									.getProgress() + 16));
						}
					});
			holder.upsence.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DataStr = DaiKin.BuildDataString(setStatus);
					UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
					mUdpAsyncTask.execute(ATU_CMD_WRSCENE, res,String.valueOf(index));
					holder.settingitem.setVisibility(View.GONE);
					item.expand = false;
				}
			});

			holder.calsence.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					holder.settingitem.setVisibility(View.GONE);
					item.expand = false;
				}
			});
			return convertView;
		}

		class ViewHolder {
			ImageView sences_icon;
			TextView txtSencesName;
			LinearLayout settingitem;
			RadioGroup radonoff;
			RadioGroup radmode;
			RadioGroup radfan;
			RadioGroup radfix;
			SeekBar seekbar_self;
			TextView tv_self;
			RadioButton rdon;
			RadioButton rdoff;

			RadioButton rdcold;
			RadioButton rdheat;
			RadioButton rddehu;
			RadioButton rdfan;

			RadioButton rdlow;
			RadioButton rdcenter;
			RadioButton rdstrong;

			RadioButton rdscan;
			RadioButton rdfix;

			Button upsence;
			Button calsence;

			public ViewHolder(View view) {
				sences_icon = (ImageView) view.findViewById(R.id.sences_icon);
				txtSencesName = (TextView) view
						.findViewById(R.id.txtSencesName);
				settingitem = (LinearLayout) view
						.findViewById(R.id.settingitem);

				radonoff = (RadioGroup) view.findViewById(R.id.radonoff);
				radmode = (RadioGroup) view.findViewById(R.id.radmode);
				radfan = (RadioGroup) view.findViewById(R.id.radfan);
				radfix = (RadioGroup) view.findViewById(R.id.radfix);

				seekbar_self = (SeekBar) view.findViewById(R.id.seekbar_self);
				tv_self = (TextView) view.findViewById(R.id.tv_self);

				rdon = (RadioButton) view.findViewById(R.id.rdon);
				rdoff = (RadioButton) view.findViewById(R.id.rdoff);

				rdcold = (RadioButton) view.findViewById(R.id.rdcold);
				rdheat = (RadioButton) view.findViewById(R.id.rdheat);
				rddehu = (RadioButton) view.findViewById(R.id.rddehu);
				rdfan = (RadioButton) view.findViewById(R.id.rdfan);

				rdlow = (RadioButton) view.findViewById(R.id.rdlow);
				rdcenter = (RadioButton) view.findViewById(R.id.rdcenter);
				rdstrong = (RadioButton) view.findViewById(R.id.rdstrong);

				rdscan = (RadioButton) view.findViewById(R.id.rdscan);
				rdfix = (RadioButton) view.findViewById(R.id.rdfix);

				upsence = (Button) view.findViewById(R.id.upsence);
				calsence = (Button) view.findViewById(R.id.calsence);
				view.setTag(this);
			}
		}
	}

	/************************************************************************************************/
	private class UdpAsyncTask extends AsyncTask<String, String, Integer> {
		private String strCommand = "";
		private String atuStr = "";
		private String position = "";
		private String itemip = "";
		private int itemport = 0;
		private AtuInfo atu;

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

		// private boolean CheckSameStatus() {
		// if (setStatus.SetTemp != dkStatus.SetTemp)
		// return false;
		// if (setStatus.Power != dkStatus.Power)
		// return false;
		// if (setStatus.RunMode != dkStatus.RunMode)
		// return false;
		// if (setStatus.FanScan != dkStatus.FanScan)
		// return false;
		// if (setStatus.FanSpeed != dkStatus.FanSpeed)
		// return false;
		// return true;
		// }

		private UdpAsyncTask() {
			super();
		}

		private AtuUdpInterface myCallback = new AtuUdpInterface() {
			@Override
			public void ReceiveCallback(String ip, String strEcho) {
				if (strCommand == ATU_CMD_GETSCENES) {
					if (!strEcho.equals("")) {
						SceneName = strEcho.split(",")[0].trim();
					} else {
						// bIsScenes = false;
					}
				} else if (strCommand == ATU_CMD_RDSCENE) {
					if (!strEcho.equals("")) {
						DataStr = strEcho.split(";")[0].trim();
						String strValue[] = DataStr.split(",");
						dkStatus.SetTemp = HexStringToInt(strValue[2]);
						dkStatus.Power = HexStringToInt(strValue[3]);
						dkStatus.RunMode = HexStringToInt(strValue[4]);
						dkStatus.FanSpeed = HexStringToInt(strValue[5]) >> 4;
						dkStatus.FanScan = HexStringToInt(strValue[5]) & 0x0F;

					}

				} else if (strCommand == ATU_CMD_WRSCENE) {

				} else if (strCommand == ATU_CMD_DELSCENE) {

				} else if (strCommand == CMD_SET_DATA) {

				}
			}

		};

		@Override
		protected Integer doInBackground(String... strings) {

			strCommand = strings[0];
			atuStr = strings[1];
			Gson gson = new Gson();
			atu = gson.fromJson(atuStr, AtuInfo.class);
			if (atu.getIsRemote() == 1) {
				itemip = atu.getAtuRemoIp();
				itemport = atu.getAtuRemoPort();
			} else if (atu.getIsRemote() == 0) {
				itemip = atu.getAtuIp();
				itemport = 9559;
			}
			MyApp.getAppInstance().setComm(
					new CommUtil(itemip, itemport, myCallback));
			MyApp.getAppInstance().getComm().aut_ser = atu.getAtuId();
			MyApp.getAppInstance().getComm().login_id = "admin_"
					+ atu.getAtuPwd();
			try {
				if (strCommand == ATU_CMD_GETSCENES) {
//					position = strings[2];
//					return MyApp.getAppInstance().getComm()
//							.SendInCloudCommand(ATU_CMD_GETSCENES);

				} else if (strCommand == ATU_CMD_RDSCENE) {
					position = strings[2];
					return MyApp
							.getAppInstance()
							.getComm()
							.SendInCloudCommand(
									ATU_CMD_RDSCENE + SceneName + ",");

				} else if (strCommand == ATU_CMD_WRSCENE) {

					return MyApp
							.getAppInstance()
							.getComm()
							.SendInCloudCommand(
									ATU_CMD_WRSCENE + SceneName + ":"
											+ atu.getAtuRoom() + ","
											+ atu.getAtuName() + "," + DaiKin.BuildDataString(setStatus)
											+ ";");

				} else if (strCommand == ATU_CMD_DELSCENE) {
					return MyApp
							.getAppInstance()
							.getComm()
							.SendInCloudCommand(
									ATU_CMD_DELSCENE + SceneName + ",");

				} else if (strCommand == CMD_SET_DATA) {
					return MyApp
							.getAppInstance()
							.getComm()
							.SendInCloudCommand(
									CMD_SET_DATA + atu.getAtuRoom() + ","
											+ atu.getAtuName() + "," + DaiKin.BuildDataString(setStatus));
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
			if (strCommand == ATU_CMD_GETSCENES) {
				if (result > 0) {
					bIsScenes = true;
//					Message message = new Message();
//					message.what = 1;
//					Bundle bundle = new Bundle();
//					bundle.putString("atu", atuStr);
//					bundle.putString("position", position);
//					message.setData(bundle);
//					handler.sendMessage(message);
				} else {
					bIsScenes = false;
				}
			} else if (strCommand == ATU_CMD_RDSCENE) {
				if (result > 0) {
					bIsSData = true;
					Message message = new Message();
					message.what = 2;
					Bundle bundle = new Bundle();
					bundle.putString("position", position);
					message.setData(bundle);
					handler.sendMessage(message);
				} else {
					bIsSData = false;
				}

			} else if (strCommand == ATU_CMD_WRSCENE) {
				if (result > 0) {
					to = Toast.makeText(getActivity(), "保存成功",
							Toast.LENGTH_SHORT);
				}

			} else if (strCommand == ATU_CMD_DELSCENE) {

			} else if (strCommand == CMD_SET_DATA) {
				if (result > 0) {
					to = Toast.makeText(getActivity(), "发送成功",
							Toast.LENGTH_SHORT);
				}
			}
			if (to != null)
				to.show();
		}
	}

	final android.os.Handler handler = new android.os.Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // 1 sencond routine
				String res = msg.getData().getString("atu");
				String index = msg.getData().getString("position");
				UdpAsyncTask mUdpAsyncTask = new UdpAsyncTask();
				mUdpAsyncTask.execute(ATU_CMD_RDSCENE, res, index);
				break;
			case 2:
				String position = msg.getData().getString("position");
				int Iindex = Integer.valueOf(position);
				View view = mListView.getChildAt(Iindex
						- mListView.getFirstVisiblePosition());
				mListView.getAdapter().getView(Iindex, view, mListView);
				break;
			}
		}
	};

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
