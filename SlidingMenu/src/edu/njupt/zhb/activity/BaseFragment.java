package edu.njupt.zhb.activity;

import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.comm.IComm;
import edu.njupt.zhb.comm.RemoteComm;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;

@SuppressLint("NewApi")
public class BaseFragment extends Fragment {
	protected static boolean isRemote = false;
	protected static String serverIp ;
	protected static String hostIp ;
	protected ProgressDialog pDialog;
	/**
	 * 显示对话框
	 */
	protected void showDialog() {
		// 实例化
		pDialog = new ProgressDialog(this.getActivity());
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
	protected void closeDialog()
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
	
	public static IComm GetComm(){
		//if(!isRemote)
			return new CommUtil(serverIp,9559);
		//else
		//	return new RemoteComm(serverIp,hostIp);
	}
}
