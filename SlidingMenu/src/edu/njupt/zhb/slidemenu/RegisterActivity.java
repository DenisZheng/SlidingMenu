package edu.njupt.zhb.slidemenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.njupt.zhb.activity.BaseActivity;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.comm.MsgHandler;
import edu.njupt.zhb.comm.MyCountTimer;

public class RegisterActivity extends BaseActivity {

	private String incode = null;
	private TextView mobnum = null;
	private TextView pass1 = null;
	private TextView pass2 = null;
	private TextView regcode = null;
	private Button getcodebtn = null;
	private Button regbtn = null;

	private Handler handler;
	private Message msg;
	private MyCountTimer timeCount;

	@Override
	public void InitActivity(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// super.onCreate(savedInstanceState);
		this.setContentView(R.layout.reg_activity);
		handler = new MsgHandler(RegisterActivity.this);
		mobnum = (TextView) this.findViewById(R.id.mobilenum);
		pass1 = (TextView) this.findViewById(R.id.regpasswd);
		pass2 = (TextView) this.findViewById(R.id.nextpasswd);
		regcode = (TextView) this.findViewById(R.id.regcode);
		getcodebtn = (Button) this.findViewById(R.id.getcodebtn);
		timeCount = new MyCountTimer(getcodebtn, 0xfff30008, 0xff969696);// 传入了文字颜色值
		// timeCount.start();
		getcodebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(getCodeTask).start();
			}
		});
		regbtn = (Button) this.findViewById(R.id.regBtn);
		regbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(RegisterTask).start();
			}
		});
		

	}

	Runnable getCodeTask = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			incode = Config.GetPassCode();
			if (TextUtils.isEmpty(mobnum.getText())) {
				msg = handler.obtainMessage();
				msg.arg1 = 7;
				handler.sendMessage(msg);
				return;
			} else {

				int revCode = Config.GetGenCode(mobnum.getText().toString()
						.trim(), incode);
				if (revCode == 1) {
					revCode = 100;
				}
				msg = handler.obtainMessage();
				msg.arg1 = revCode;
				handler.sendMessage(msg);
				timeCount.start();

			}
		}

	};

	Runnable RegisterTask = new Runnable() {

		@Override
		public void run() {
			Looper.prepare();
			// TODO Auto-generated method stub
			if (TextUtils.isEmpty(mobnum.getText())
					|| TextUtils.isEmpty(pass1.getText())
					|| TextUtils.isEmpty(pass2.getText())
					|| TextUtils.isEmpty(regcode.getText())) {
				msg = handler.obtainMessage();
				msg.arg1 = 7;
				handler.sendMessage(msg);
				return;
			} else {
				if (!TextUtils.equals(pass1.getText(), pass2.getText())) {
					msg = handler.obtainMessage();
					msg.arg1 = 101;
					handler.sendMessage(msg);
					return;
				} else {
					int revCode = Config.RegUser(mobnum.getText().toString()
							.trim(), pass1.getText().toString().trim(), regcode
							.getText().toString().trim());
					msg = handler.obtainMessage();
					msg.arg1 = revCode;
					handler.sendMessage(msg);
					if (revCode > 0) {
						Intent intent = new Intent();
						intent.setClass(RegisterActivity.this,
								LoginActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("USERNAME", mobnum.getText().toString().trim());
						bundle.putString("PASSWD", pass1.getText().toString().trim());
						intent.putExtras(bundle);
						setResult(RESULT_OK, intent);
						finish();
					}
				}

			}
		}

	};

}
