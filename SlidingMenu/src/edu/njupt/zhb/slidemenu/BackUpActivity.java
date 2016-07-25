package edu.njupt.zhb.slidemenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import edu.njupt.zhb.activity.BaseActivity;

public class BackUpActivity extends BaseActivity{
	public void InitActivity(Bundle savedInstanceState) {
		this.setContentView(R.layout.backups_main);
		Button btnback=(Button)findViewById(R.id.btnback);
		btnback.setFocusable(true);
		btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BackUpActivity.this.finish();
			
			}
		});
		LinearLayout btnup=(LinearLayout)findViewById(R.id.btndatasup);
		btnup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		LinearLayout btndown=(LinearLayout)findViewById(R.id.btndatasdown);
		btndown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				
			}
		});
	}
}