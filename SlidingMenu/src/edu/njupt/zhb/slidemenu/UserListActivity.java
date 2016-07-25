package edu.njupt.zhb.slidemenu;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.njupt.zhb.activity.BaseActivity;

public class UserListActivity  extends BaseActivity{
	public void InitActivity(Bundle savedInstanceState) {
		this.setContentView(R.layout.change_user_main);
		Button btnback=(Button)findViewById(R.id.btnback);
		btnback.setFocusable(true);
		btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserListActivity.this.finish();
				
			}
		});
	}
}
