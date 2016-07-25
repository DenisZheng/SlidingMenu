package edu.njupt.zhb.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;

import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.IComm;
import edu.njupt.zhb.comm.RemoteComm;

public abstract class BaseActivity extends Activity {
	protected static boolean isRemote = false;
	protected static String serverIp ;
	protected static String hostIp ;
	protected  ProgressDialog pDialog;
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	protected void onCreate(Bundle savedInstanceState) {
		// 4.0以上进行通讯协议是必须的加上这段代码(以便优化),这是针对4.0版本以上的作用ICE_CREAM_SANDWICH
		 String strVer=GetSystemVersion();
		 strVer=strVer.substring(0,3).trim();
		 float fv=Float.valueOf(strVer);
		 if(fv>2.3)
		 {
		   StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());  
		   StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());  
		 }	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		 this.InitActivity(savedInstanceState);
//		//this.setContentView(id);

        WifiManager wifi = (WifiManager) getSystemService(this.WIFI_SERVICE);
        WifiManager.MulticastLock lock = wifi.createMulticastLock("wifi test");
         lock.acquire();
         if (!wifi.isWifiEnabled()) {
        	    wifi.setWifiEnabled(true);//   wifi   ÿ   
        	   }
       

	}
	/**
	 * 显示对话框
	 */
	protected  void showDialog() {
		// 实例化
		pDialog = new ProgressDialog(this);
		// 设置进度条风格，风格为圆形，旋转的
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("请稍后......");
		// 设置ProgressDialog 的进度条是否不明确
		pDialog.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		pDialog.setCancelable(true);
		// 让ProgressDialog显示
		pDialog.show();
	}
	protected  void closeDialog()
	{
		Thread tmpThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					if(pDialog != null)
					{
						pDialog.dismiss();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		tmpThread.start();
	}
	public static String GetSystemVersion()
	{
	  return android.os.Build.VERSION.RELEASE;
	}

	public abstract void InitActivity(Bundle savedInstanceState);
	
	public static IComm GetComm(){
		//if(!isRemote)
			return new CommUtil(serverIp,9559);
		//else
		//	return new RemoteComm(serverIp,hostIp);
	}
}