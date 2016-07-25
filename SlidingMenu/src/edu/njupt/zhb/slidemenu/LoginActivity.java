package edu.njupt.zhb.slidemenu;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import edu.njupt.zhb.activity.BaseActivity;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.AtuInfo;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.comm.MsgHandler;
import edu.njupt.zhb.utils.AndroidUtil;
import edu.njupt.zhb.utils.StringUtil;

public class LoginActivity extends BaseActivity {
	SharedPreferences mSP = null;
	boolean needCopy = false;
	private Handler handler;
	private Message msg;
	private TextView usrTxt;
	private TextView pwdTxt;

	@Override
	public void InitActivity(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.login_activity);
		CheckBox ckpd = (CheckBox) findViewById(R.id.checkpd);
		String checkUp = AndroidUtil.GetCheckUserPwd(this);
		handler = new MsgHandler(LoginActivity.this);
		usrTxt = (TextView) findViewById(R.id.usr_txt);
		pwdTxt = (TextView) findViewById(R.id.pwd_txt);
		Button btn = (Button) this.findViewById(R.id.loginBtn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CheckBox ckpd = (CheckBox) findViewById(R.id.checkpd);
				String usrName = usrTxt.getText().toString();
				String pwd = pwdTxt.getText().toString();
				List<AtuInfo> atus = Config.loadArray(LoginActivity.this,
						Config.ATUS);
				String atuId = "001204011DA0";
				if (atus.size() > 0) {
					atuId = atus.get(0).getAtuId().toString();
				}
				String logrev = Config.RemLogin(usrName, pwd, atuId);
				if (!logrev.contains("Error:")) {
					//showDialog();
					if (ckpd.isChecked()) {
						AndroidUtil.SetString(v.getContext(), usrName, pwd, "1");
					} else {
						AndroidUtil.SetString(v.getContext(), usrName, pwd, "0");
					}
					MyApp.getAppInstance().setUserName(usrName);
					MyApp.getAppInstance().setPwd(pwd);
					Intent intent = new Intent();
					//closeDialog();
					setResult(RESULT_OK, intent);
					LoginActivity.this.finish();
				} else {
					msg = handler.obtainMessage();
					msg.arg1 = -104;
					handler.sendMessage(msg);
					return;
				}

			}
		});

		Button regbtn = (Button) this.findViewById(R.id.goRegBtn);
		regbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				// intent.putExtras(bundle);
				startActivityForResult(intent, 1);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			Bundle b = data.getExtras(); // data为B中回传的Intent
			String logname = b.getString("USERNAME");// str即为回传的值
			String pwd = b.getString("PASSWD");
			usrTxt.setText(logname);
			pwdTxt.setText(pwd);
			break;
		default:
			break;
		}
	}
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event)
	// {
	// if (keyCode == KeyEvent.KEYCODE_BACK )
	// {
	// // 创建退出对话框
	// AlertDialog isExit = new AlertDialog.Builder(this).create();
	// // 设置对话框标题
	// isExit.setTitle("系统提示");
	// // 设置对话框消息
	// isExit.setMessage("确定要退出吗");
	// // 添加选择按钮并注册监听
	// isExit.setButton("确定", listener);
	// isExit.setButton2("取消", listener);
	// // 显示对话框
	// isExit.show();
	//
	// }
	//
	// return false;
	//
	// }
	// /**监听对话框里面的button点击事件*/
	// DialogInterface.OnClickListener listener = new
	// DialogInterface.OnClickListener()
	// {
	// public void onClick(DialogInterface dialog, int which)
	// {
	// switch (which)
	// {
	// case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
	// finish();
	// break;
	// case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
	// break;
	// default:
	// break;
	// }
	// }
	// };
}
