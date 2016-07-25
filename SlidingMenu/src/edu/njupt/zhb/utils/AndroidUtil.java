package edu.njupt.zhb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AndroidUtil {

	public static void Sleep(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			//e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return bitmap;
	}
	
	public static void SetString(Context ctx,String userName,String passWord,String checked){
	       
        SharedPreferences sp = ctx.getSharedPreferences("InCloud_USER_KEY", 0);
        Editor editor = sp.edit();
        editor.putString("userName", userName);
        editor.putString("passWord", passWord);
        editor.putString("checkUP", checked);
        editor.commit();
	}
	public static String GetCheckUserPwd(Context ctx){
	   SharedPreferences sp = ctx.getSharedPreferences("InCloud_USER_KEY", 0);
       String cup = sp.getString("checkUP","0");
       return cup;
	}
	public static void SetCheckUserPwd(Context ctx,String checked){
		 SharedPreferences sp = ctx.getSharedPreferences("InCloud_USER_KEY", 0);
	        //�������
	        Editor editor = sp.edit();
	        editor.putString("checkUP", checked);
	        editor.commit();
	}
	public static String GetUserName(Context ctx){
	       
		 SharedPreferences sp = ctx.getSharedPreferences("InCloud_USER_KEY", 0);
        String ip = sp.getString("userName","admin");
        return ip;
	}
	public static String GetPassWord(Context ctx){
	       
		 SharedPreferences sp = ctx.getSharedPreferences("InCloud_USER_KEY", 0);
       String ip = sp.getString("passWord","admin");
       return ip;
	}
	public static void SetIpString(Context ctx,String type,String ip){
	       
        SharedPreferences sp = ctx.getSharedPreferences("InCloud_SM_IP_"+type, 0);
        //�������
        Editor editor = sp.edit();
        editor.putString("IP", ip);
        
        editor.commit();
	}
	public static String GetIpString(Context ctx,String type){
	       
        SharedPreferences sp = ctx.getSharedPreferences("InCloud_SM_IP_"+type, 0);
        String ip = sp.getString("IP","172.16.5.241");
        return ip;
	}
	
	public static void AlertDialog(Context ctx,String txt)
	{
	    //Alert Dialog
	    new android.app.AlertDialog.Builder(ctx)
	    .setTitle("提示")
	    .setMessage(txt)
	    .setNegativeButton("确定", new DialogInterface.OnClickListener() {   
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            //do nothing - it will close on its own
	        }
	     })
	   .show();
	    
	}

}
