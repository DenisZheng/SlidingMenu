package edu.njupt.zhb.activity;

import edu.njupt.zhb.slidemenu.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class LoadingActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.findatu_loading);
	
	//这里Handler的postDelayed方法，等待10000毫秒在执行run方法。
	//在Activity中我们经常需要使用Handler方法更新UI或者执行一些耗时事件，
	//并且Handler中post方法既可以执行耗时事件也可以做一些UI更新的事情，比较好用，推荐使用
	new Handler().postDelayed(new Runnable(){
		public void run(){	
			//等待10000毫秒后销毁此页面，并提示登陆成功
			LoadingActivity.this.finish();
			Toast.makeText(getApplicationContext(), "搜索完成", Toast.LENGTH_SHORT).show();
		}
	}, 10*1000);
   }
}