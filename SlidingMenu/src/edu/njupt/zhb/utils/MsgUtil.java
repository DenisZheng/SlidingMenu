package edu.njupt.zhb.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

public class MsgUtil {
	 
	public static void ShowMessage(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		//execToast(toast);
	}
	/**
	 *�˷�������TimerTask��Toast��ʾһ�������ʾһ�Ρ�
	*/
	public static void execToast(final Toast toast) {
	                 Timer timer = new Timer();
	                 timer.schedule(new TimerTask() {

	                         @Override
	                         public void run() {
	                                 //�����̷߳�����������ܻ���ʾ��������
	                                initToast(toast);
	                         }

	                 }, 1000);
	         }

	 public static void initToast(Toast toast) {
	         toast.show();
	 } 
	public static void ShowControlLoading(Context context) {
		Toast toast = Toast.makeText(context, "���ͨѶ����ȴ�...", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	public static boolean flag=true;
	public static ProgressDialog pd;  
	public static void ShowMessageData(Context context)
	{
		if(pd==null) 
		   pd = ProgressDialog.show(context, "����", "�����У����Ժ󡭡�"); 
		  /* ����һ�����̣߳������߳���ִ�к�ʱ�ķ��� */  
		 new Thread(new Runnable() {  
			  @Override  
              public void run() {  
				  while(flag){
					  try {  
			              Thread.sleep(5000);  
			         } catch (InterruptedException e) {  
			               // TODO Auto-generated catch block  
			             e.printStackTrace();  
			         }  
                }   
			  }
	 
         }).start();  

	}
	public static Handler handler = new Handler() { 
		@Override  
		public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���  
			if(pd!=null) 
			 pd.dismiss();// �ر�ProgressDialog  
			 pd=null;
             flag=false;
		}
	}; 
}
