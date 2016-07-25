package edu.njupt.zhb.view;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import  edu.njupt.zhb.slidemenu.R;
import  edu.njupt.zhb.bean.BaseInfo;
import edu.njupt.zhb.bean.DataModel;
import  edu.njupt.zhb.bean.ShowInfo;

public class HotView extends BaseView {
	private DataModel getShowInfo(){
		return (DataModel)this.GetInfo();
	}
	public HotView(Context context, DataModel _info) {
		super(context, _info, R.layout.hots_box);
		// TODO Auto-generated constructor stub
	}

	private String sta ;
	private String st ;
	private String ct ;
	private String mode ;
	private String fan ;
	public void InitView() {
		// TODO Auto-generated method stub
//		Log.i("AirView", this.getShowInfo().getTagList().toString());
//		Map maps= (Map)this.getShowInfo().getTagList();
//		Iterator its = maps.entrySet().iterator();
//	    while(its.hasNext()){
//	    	Map.Entry entrys=(Map.Entry)its.next();
//	    	Map map=(Map)entrys.getValue();
//	    	Iterator it=map.entrySet().iterator();
//	    	while(it.hasNext())
//	    	{
//	    	    Map.Entry entry = (Map.Entry)it.next();
//	    	    if(entry.getKey().equals("01_TAG"))
//	    	    {
//	    	    	this.sta =entry.getValue().toString();
//	    	    }
//	    	    else  if(entry.getKey().equals("02_TAG"))
//	    	    {
//	    	    	this.st =entry.getValue().toString();
//	    	    }
//	    	}
//	    }
		
//		ImageView onBtn = this.GetImageView(R.id.hots_on_btn);
//		onBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				GetComm().SendTo(sta, "01", "0001");
//				setStaVal(1);
//			}
//		});
//		ImageView offBtn = this.GetImageView(R.id.hots_off_btn);
//		offBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				GetComm().SendTo(sta, "01", "0000");
//				setStaVal(0);
//			}
//		});
//		
//		ImageView upBtn = this.GetImageView(R.id.hots_up_btn);
//		
//		upBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				setTmp =setTmp+100;
//				if(setTmp>3100)setTmp=3100;
//				if(setTmp<1600)setTmp=1600;
//				DecimalFormat df = new DecimalFormat("0000");
//				GetComm().SendTo(st, "02", df.format(setTmp));
//				setSetTmpVal(setTmp/100);
//			}
//		});
//		
//		ImageView downBtn = this.GetImageView(R.id.hots_down_btn);
//		
//		downBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				setTmp =setTmp-100;
//				if(setTmp>3100)setTmp=3100;
//				if(setTmp<1600)setTmp=1600;
//				DecimalFormat df = new DecimalFormat("0000");
//				GetComm().SendTo(st, "02",df.format(setTmp));
//				setSetTmpVal(setTmp/100);
//			}
//		});

		
		
		
	}
	public ImageView GetImageView(int imageId){
		ImageView staBtn = (ImageView)view.findViewById(imageId);
		return staBtn;
				
	}
//	private void setStaVal(int i){
//		TextView tv = (TextView)view.findViewById(R.id.hot_sta_id);
//		if(i==1)tv.setText("��");
//		else
//			tv.setText("��");
//	}
//	private void setSetTmpVal(int i){
//		TextView tv = (TextView)view.findViewById(R.id.hot_sttmp_id);
//		tv.setText(String.valueOf(i));
//	}
	
	
	
	
	
	
	
	int setTmp =24;
	public void SetSTA() {
		// TODO Auto-generated method stub
		//GetSTA();
		Start(); 
        
          
		
	}
	protected void GetSTA() {
		// TODO Auto-generated method stub
//		String r_sta = GetComm().Read(sta, "01");
//		if(r_sta.equals("")||r_sta.equals(null))r_sta="0000";
//		setStaVal(Integer.valueOf(r_sta));
//		
//		String r_st = GetComm().Read(st, "02");
//		if(r_st.equals("")||r_st.equals(null))r_st="2400";
//		setTmp = Integer.valueOf(r_st);
//		setSetTmpVal(setTmp/100);
	}

}

