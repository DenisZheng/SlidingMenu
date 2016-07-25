/**
 * 
 */
/**
 * @author Administrator
 *
 */
package edu.njupt.zhb.view;


import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.njupt.zhb.bean.BaseInfo;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.comm.IComm;


public abstract class BaseView implements IShow {
	
	private boolean run = false;
	private Handler handler = new Handler();
	
	public void Stop(){
		run = false;
		handler.removeCallbacks(task);
	}
	private Runnable task = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			if (run) {
				GetSTA();
				//handler.postDelayed(this, 5000);
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}
	};
	public void Start(){
		run = true;  
        handler.post(task);
	}
	public IComm GetComm(){
		return _comm;
	}
	protected  void GetSTA(){
		
	}
	private IComm _comm;
	public void SetComm(IComm comm){
		_comm = comm;
	}
	
	public BaseView(Context context, DataModel _info,int layoutId) {
		info = _info;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(layoutId, null);
	}

	public void SetInfo(DataModel _info){
		this.info = _info;
	}

	public DataModel GetInfo() {
		return info;
	}

	public View GetView() {
		view.setTag(this);
		return view;
	}

	View view;

//	public void Read() {
//		final Handler mHandler = new Handler() {
//			public void handleMessage(Message msg) {
//				GetSTA();
//				super.handleMessage(msg);
//			}
//		};
//
//		new Thread(new Runnable() {
//
//			public void run() {
//				// TODO Auto-generated method stub
//				// while (true) {
//				Message msg = mHandler.obtainMessage();
//				msg.what = 1;
//				msg.sendToTarget();
//
//				// SetSta();
//				// AndrodiUtil.Sleep(1000*10);
//				// }
//			}
//		}).start();
//	}

//	public abstract void GetSTA();

	private DataModel info;
	
	
	public ImageView getImageViewById(int id){
		ImageView images = (ImageView) view.findViewById(id);
		images.postInvalidate();
		return images;
	}
	public TextView getTextViewById(int id){
		TextView tv = (TextView) view.findViewById(id);
		return tv;
	}
}
