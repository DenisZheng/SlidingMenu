package edu.njupt.zhb.slidemenu;

import java.util.Calendar;

import com.wheel.StrericWheelAdapter;
import com.wheel.WheelView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.njupt.zhb.activity.BaseActivity;

public class TimesMainActivity  extends BaseActivity{
	private int minYear = 1970;  //最小年份
	private int fontSize = 13; 	 //字体大小
	private WheelView yearWheel,monthWheel,dayWheel,hourWheel,minuteWheel,secondWheel;
	public static String[] yearContent=null;
	public static String[] monthContent=null;
	public static String[] dayContent=null;
	public static String[] hourContent = null;
	public static String[] minuteContent=null;
	public static String[] secondContent=null;
	public String devId="";
	public String devUi="";
	public String devType="";
	public String devName="";
	private TextView time_tv;
	public LinearLayout line;
	public void InitActivity(Bundle savedInstanceState) {
		this.setContentView(R.layout.times_main);
		  time_tv = (TextView)findViewById(R.id.tv_time);
		  line=(LinearLayout)findViewById(R.id.time_dev);
		Bundle bundle = this.getIntent().getExtras();
		devId=bundle.getString("devId");
	    devType=bundle.getString("devType");
	    devUi=bundle.getString("devUi");
	    devName=bundle.getString("OtherName");
		Button btnback=(Button)findViewById(R.id.btnback);
		btnback.setFocusable(true);
		btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimesMainActivity.this.finish();
				
			}
		});
		getDevice();
		initContent();
		ImageView iv_add=(ImageView)findViewById(R.id.iv_add);
		iv_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
				closeDialog();
			}
		});
		ImageView iv_del=(ImageView)findViewById(R.id.iv_del);
		iv_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
				closeDialog();
			}
		});
	}
	public void getDevice()
	{
		View view=null;  
        TextView tname=null;
		if(devUi.toLowerCase().equals("light")){
			if(devType.equals("1"))
			   view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.time_light_box, null); 
			else
				view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.time_light_dimm_box, null); 
		}
		else if(devUi.toLowerCase().equals("air")){
			view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.time_air_box, null); 
		}
		else if(devUi.toLowerCase().equals("hot")){
			view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.time_hot_box, null); 
		}
		else if(devUi.toLowerCase().equals("window")){
			view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.time_window_box, null); 
		}
		else if(devUi.toLowerCase().equals("curtain")){
			view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.time_curtain_box, null); 
		}
		 tname=(TextView)view.findViewById(R.id.dev_name);
		 tname.setText(devName);
		line.addView(view);
	}
    public void initContent()
   	{
   	
   		hourContent = new String[24];
   		for(int i=0;i<24;i++)
   		{
   			hourContent[i]= String.valueOf(i);
   			if(hourContent[i].length()<2)
   	        {
   				hourContent[i] = "0"+hourContent[i];
   	        }
   		}
   			
   		minuteContent = new String[60];
   		for(int i=0;i<60;i++)
   		{
   			minuteContent[i]=String.valueOf(i);
   			if(minuteContent[i].length()<2)
   	        {
   				minuteContent[i] = "0"+minuteContent[i];
   	        }
   		}
   	}
    public void buttonClick(View v)
    {
    	int id = v.getId();
    	if(id==R.id.pick_bt)
    	{
    		View view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.time_picker, null); 
    			
    		Calendar calendar = Calendar.getInstance();
            int curHour = calendar.get(Calendar.HOUR_OF_DAY);
            int curMinute = calendar.get(Calendar.MINUTE);
     	    
    	    hourWheel = (WheelView)view.findViewById(R.id.hourwheel);
    	    minuteWheel = (WheelView)view.findViewById(R.id.minutewheel);
    	    
    	   
            AlertDialog.Builder builder = new AlertDialog.Builder(this);  
            builder.setView(view); 
	        
	        hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
	        hourWheel.setCurrentItem(curHour);
	        hourWheel.setCyclic(true);
	        hourWheel.setInterpolator(new AnticipateOvershootInterpolator());
	        
	        minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
	        minuteWheel.setCurrentItem(curMinute);
	        minuteWheel.setCyclic(true);
	        minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());
	        
			 
	        builder.setTitle("选取时间");  
	        builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {  

	        	@Override  
            	public void onClick(DialogInterface dialog, int which) {  
            	
	        		StringBuffer sb = new StringBuffer();  
	        		sb.append(hourWheel.getCurrentItemValue())  
	        		.append(":").append(minuteWheel.getCurrentItemValue());
	        		time_tv.setText(sb);
	        		dialog.cancel();  
            	}  
	        });  
       
	        builder.show();
    	}
    }
}
