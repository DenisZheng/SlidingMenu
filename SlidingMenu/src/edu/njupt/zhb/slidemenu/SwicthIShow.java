package edu.njupt.zhb.slidemenu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.njupt.zhb.activity.BaseActivity;
import edu.njupt.zhb.bean.SenceInfo;
import edu.njupt.zhb.view.AirView;
import edu.njupt.zhb.view.CurtainView;
import edu.njupt.zhb.view.FanView;
import edu.njupt.zhb.view.HotView;
import edu.njupt.zhb.view.LightDimmView;
import edu.njupt.zhb.view.LightView;
import edu.njupt.zhb.view.WindowView;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.bean.ShowInfo;
import edu.njupt.zhb.bean.BaseInfo;
import edu.njupt.zhb.comm.IComm;
import edu.njupt.zhb.view.IShow;

public class SwicthIShow {
	 public static IShow getView(Context txt,DataModel info,IComm comm) {
			IShow ss =null;
			if(info.getUiCode().equals("Light")){
				if(info.getDevNum().equals("1")){
				 ss= new LightView(txt,info);
				}
				else{
					ss= new LightDimmView(txt,info);
				}
			}
			else  if(info.getUiCode().equals("Curtain")){
			 	  ss= new CurtainView(txt,info);
			}
			else  if(info.getUiCode().equals("Window")){
			 	  ss= new WindowView(txt,info);
			}
			else  if(info.getUiCode().equals("Air")){
			 	  ss= new AirView(txt,info);
			}
			else  if(info.getUiCode().equals("Hot")){
			 	  ss= new HotView(txt,info);
			}
			else  if(info.getUiCode().equals("Fan")){
			 	  ss= new FanView(txt,info);
			}
				
			if(ss!=null)ss.SetComm(comm);
			return ss;
		}

	 
}
