package edu.njupt.zhb.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

public class CustomToast { 
	public static final int LENGTH_MAX = -1; 
	private boolean mCanceled = true;
	private Handler mHandler; 
	private Context mContext; 
	private Toast mToast; 

	public CustomToast(Context context) { 
		this(context,new Handler()); 
	} 


	public CustomToast(Context context,Handler h) { 
		mContext = context; 
		mHandler = h; 
		mToast = Toast.makeText(mContext,"",Toast.LENGTH_SHORT); 
		mToast.setGravity(Gravity.BOTTOM, 0, 0); 
	} 

	public void show(int resId,int duration) { 
		mToast.setText(resId); 
		if(duration != LENGTH_MAX) { 
			mToast.setDuration(duration); 
			mToast.show(); 
		 } else if(mCanceled) { 
			 mToast.setDuration(Toast.LENGTH_LONG);
			 mCanceled = false;
			 showUntilCancel(); 
		 } 
	}
	
	/**
	 * @param text Ҫ��ʾ������
	 * @param duration ��ʾ��ʱ�䳤
	 * ���LENGTH_MAX�����ж�
	 * ���ƥ�䣬����ϵͳ��ʾ
	 * ���ƥ�䣬������ʾ��ֱ������hide()
	 */
	public void show(String text,int duration) { 
		mToast.setText(text); 
		if(duration != LENGTH_MAX) { 
			mToast.setDuration(duration); 
			mToast.show(); 
			} else { 
				if(mCanceled) { 
					mToast.setDuration(Toast.LENGTH_LONG); 
					mCanceled = false; 
					showUntilCancel();
				}
			} 
		} 

	/**
	 * ����Toast
	 */
	public void hide(){
		mToast.cancel();
		mCanceled = true;
	}
	
	public boolean isShowing() {
		return !mCanceled;
	}
	
	private void showUntilCancel() { 
		if(mCanceled) 
			return; 
		mToast.show();
		mHandler.postDelayed(new Runnable() {
			public void run() { 
				showUntilCancel(); 
			}
		},3000); 
	} 
} 
