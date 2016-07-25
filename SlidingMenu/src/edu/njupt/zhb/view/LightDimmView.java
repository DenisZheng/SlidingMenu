package edu.njupt.zhb.view;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import edu.njupt.zhb.slidemenu.R;
import edu.njupt.zhb.bean.BaseInfo;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.ShowInfo;
import edu.njupt.zhb.utils.MsgUtil;

public class LightDimmView extends BaseView {

	private DataModel getShowInfo(){
		return (DataModel)this.GetInfo();
	}
	
	public LightDimmView(Context context, DataModel _info) {
		super(context,_info,R.layout.light_dimm_box);
	}

	

	private ImageView getImageView() {
		ImageView images = (ImageView) view.findViewById(R.id.light_img);
		images.postInvalidate();
		return images;
	}

	

	private String oper;
	private String code="03";

	public void InitView() {
		// TODO Auto-generated method stub
		//Log.i("aaaaa", this.getShowInfo().getData().toString());
//		Map maps= (Map)this.getShowInfo().getTagList();
//		Iterator its = maps.entrySet().iterator();
//	    while(its.hasNext()){
//	    	Map.Entry entrys=(Map.Entry)its.next();
//	    	Map map=(Map)entrys.getValue();
//	    	Iterator it=map.entrySet().iterator();
//	    	while(it.hasNext())
//	    	{	    	  
//	    		Map.Entry entry = (Map.Entry)it.next();
//	    	    if(entry.getKey().equals("03_TAG"))
//	    	    {
//	    	    	this.oper =entry.getValue().toString();
//	    	    }
//	    	}
//	    }
		TextView ligntName = (TextView) view.findViewById(R.id.light_dimm_id);

		ligntName.setText(getShowInfo().getOtherName());
//
//		
//		SeekBar bar = (SeekBar)view.findViewById(R.id.light_dimm_bar);
//		
//		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//			
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//				DecimalFormat df = new DecimalFormat("0000");
//				int val = seekBar.getProgress();
//				String value = df.format(val);
//				
//				//GetComm().SendTo(oper, code, value);
//				
//			}
//			
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//					boolean fromUser) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		// SetSta();

//		ImageView onbtn = (ImageView) view.findViewById(R.id.light_on_btn);
//		onbtn.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//WebServiceUtil.Send(oper, "1");
//				changeImg("1");
//				MsgUtil.ShowControlLoading(v.getContext());
//			}
//		});
//
//		ImageView offBtn = (ImageView) view.findViewById(R.id.light_off_btn);
//		offBtn.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//WebServiceUtil.Send(oper, "0");
//				changeImg("0");
//				MsgUtil.ShowControlLoading(v.getContext());
//			}
//		});

		
	}


//
	@Override
	public void GetSTA() {
		// TODO Auto-generated method stub
		this.Start();
	}
	
	private void changeValue(int value){
		SeekBar bar = (SeekBar)view.findViewById(R.id.light_dimm_bar);
		bar.setProgress(value);
	}



	public void SetSTA() {
//		String value =GetComm().Read(oper, code);
//		if(value.equals("")||value.equals(null))
//			value="0000";
//		changeValue(Integer.valueOf(value));
	}

}