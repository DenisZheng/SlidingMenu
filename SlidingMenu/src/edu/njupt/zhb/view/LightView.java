package edu.njupt.zhb.view;

import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import edu.njupt.zhb.slidemenu.R;
import edu.njupt.zhb.bean.BaseInfo;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.ShowInfo;
import edu.njupt.zhb.utils.MsgUtil;

public class LightView extends BaseView {

	private DataModel getShowInfo(){
		return (DataModel)this.GetInfo();
	}
	
	public LightView(Context context, DataModel _info) {
		super(context,_info,R.layout.light_box);
	}

	

	private ImageView getImageView() {
		ImageView images = (ImageView) view.findViewById(R.id.light_img);
		images.postInvalidate();
		return images;
	}

	

	private String oper;
	private String code ="01";
    private String exeType="T";
	public void InitView() {
		// TODO Auto-generated method stub
//		Log.i("LightView", this.getShowInfo().getTagList().toString());
		TextView ligntName = (TextView) view.findViewById(R.id.light_Id);
		ligntName.setText(getShowInfo().getOtherName());
//		Map maps= (Map)this.getShowInfo().getTagList();
//		Iterator its = maps.entrySet().iterator();
//	    while(its.hasNext()){
//	    	Map.Entry entrys=(Map.Entry)its.next();
//	    	Map map=(Map)entrys.getValue();
//	    	Iterator it=map.entrySet().iterator();
//	    	while(it.hasNext())
//	    	{
//	    		   Map.Entry entry = (Map.Entry)it.next();
//		    	    if(entry.getKey().equals("01_TAG"))
//		    	    {
//		    	    	this.oper =entry.getValue().toString();
//		    	    }
//		    	    else  if(entry.getKey().equals("EXE_TYPE")){
//		    	    	exeType=entry.getValue().toString();
//		    	    }
//	                else  if(entry.getKey().equals("UI_CLASS")){
//	                	code=entry.getValue().toString();
//		    	    }
//		    	    //System.out.println(entry.getKey()+"-------"+entry.getValue()); 
//	    		
//	    	}
//	    	 
//	    }

		

		// SetSta();

		ImageView onbtn = (ImageView) view.findViewById(R.id.light_on_btn);
		onbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//WebServiceUtil.Send(oper, "1");
				//GetComm().SendTo(oper, code, "0001");
				changeImg("1");
				MsgUtil.ShowControlLoading(v.getContext());
			}
		});

		ImageView offBtn = (ImageView) view.findViewById(R.id.light_off_btn);
		offBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//GetComm().SendTo(oper, code, "0000");
				changeImg("0");
				MsgUtil.ShowControlLoading(v.getContext());
			}
		});

		
	}



//	@Override
//	public void GetSTA() {
//		// TODO Auto-generated method stub
//		String value = WebServiceUtil.Read(on);
//		changeImg(value);
//	}
	
	private void changeImg(String value){
		if (value.equals("1")) {
			Bitmap icon = BitmapFactory.decodeResource(view.getResources(),
					R.drawable.icon_light_on);
			getImageView().setImageBitmap(icon);
		} else {
			Bitmap icon = BitmapFactory.decodeResource(view.getResources(),
					R.drawable.icon_light_off);
			getImageView().setImageBitmap(icon);
		}
	}


	public void GetSTA(){
		String value ="0000";// GetComm().Read(oper, code);
		if(value.equals("")||value.equals(null))
			value="0000";
		int val = Integer.valueOf(value);
		changeImg(String.valueOf(val));
	}

	public void SetSTA() {
		this.Start();
	}

}
