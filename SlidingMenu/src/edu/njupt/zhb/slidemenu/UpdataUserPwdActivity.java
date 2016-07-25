package edu.njupt.zhb.slidemenu;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.njupt.zhb.activity.BaseActivity;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.comm.Config;
import edu.njupt.zhb.utils.AndroidUtil;

public class UpdataUserPwdActivity extends BaseActivity {
	private TextView txtname;

	public void InitActivity(Bundle savedInstanceState) {
		this.setContentView(R.layout.updata_user_pwd_main);
		txtname = (TextView) findViewById(R.id.txtUserName);
		txtname.setText(MyApp.getAppInstance().getUserName());
		Button btnback = (Button) findViewById(R.id.btnback);
		btnback.setFocusable(true);
		btnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UpdataUserPwdActivity.this.finish();

			}
		});
		Button btnOk = (Button) findViewById(R.id.btnOK);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
				EditText et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
				if (et_old_pwd.getText().equals("")
						|| et_new_pwd.getText().equals("")) {
					AndroidUtil.AlertDialog(v.getContext(), "旧密码和新密码不能为空");
				} else {
					if (et_old_pwd.getText().toString()
							.equals(MyApp.getAppInstance().getPwd().toString())) {
						// AndroidUtil.SetString(v.getContext(),
						// MyApp.getAppInstance().getUserName().toString(),
						// et_new_pwd.getText().toString(), "1");
						// MyApp.getAppInstance().setPwd(et_new_pwd.getText().toString());
						int revCode = Config.ChangPwd(txtname.getText()
								.toString().trim(), et_old_pwd.getText()
								.toString().trim(), et_new_pwd.getText()
								.toString().trim());
						if (revCode == 105) {
							AndroidUtil.AlertDialog(v.getContext(), "修改成功");
						}
					} else {
						AndroidUtil.AlertDialog(v.getContext(), "旧密码不正确");
					}
				}
			}
		});
	}
}
