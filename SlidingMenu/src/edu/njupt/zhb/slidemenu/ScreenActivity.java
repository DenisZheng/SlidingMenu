package edu.njupt.zhb.slidemenu;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.njupt.zhb.activity.BaseActivity;

public class ScreenActivity extends BaseActivity{
	public void InitActivity(Bundle savedInstanceState) {
		Bundle bundle = this.getIntent().getExtras();
		this.setContentView(R.layout.screen_main);
		Button btnback=(Button)findViewById(R.id.btnback);
		btnback.setFocusable(true);
		btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScreenActivity.this.finish();
				
			}
		});
	}
}