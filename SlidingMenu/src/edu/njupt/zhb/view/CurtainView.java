package edu.njupt.zhb.view;

import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import edu.njupt.zhb.slidemenu.R;
import edu.njupt.zhb.bean.BaseInfo;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.SenceInfo;
import edu.njupt.zhb.bean.ShowInfo;
import edu.njupt.zhb.utils.MsgUtil;

public class CurtainView extends BaseView {

	public CurtainView(Context context, DataModel _info) {
		super(context, _info, R.layout.curtain_box);
		// TODO Auto-generated constructor stub
	}
	private DataModel getShowInfo(){
		return (DataModel)this.GetInfo();
	}
	private String oper;
	private String tag;
	private String code;
	public void InitView() {
		// TODO Auto-generated method stub
//		Map maps= (Map)this.getShowInfo().getTagList();
//		Iterator its = maps.entrySet().iterator();
//	    while(its.hasNext()){
//	    	Map.Entry entrys=(Map.Entry)its.next();
//	    	Map map=(Map)entrys.getValue();
//	    	Iterator it=map.entrySet().iterator();
//	    	while(it.hasNext())
//	    	{
//	    	    Map.Entry entry = (Map.Entry)it.next();
//	    	    if(entry.getKey().equals("04_TAG"))
//	    	    {
//	    	    	this.tag =entry.getValue().toString();
//	    	    	this.code = "04";
//	    	    }
//	    	}
//	    }
		TextView tv = (TextView)this.GetView().findViewById(R.id.curtainTxt);
		tv.setText(this.getShowInfo().getOtherName());
		TextView cvalue = (TextView)this.GetView().findViewById(R.id.cValue);
		cvalue.setText("50%");
		ImageView onBtn = this.getImageViewById(R.id.curtain_on_btn);
		onBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//GetComm().SendTo(tag,code,"0001");
				MsgUtil.ShowControlLoading(v.getContext());
			}
		});
		ImageView offBtn = this.getImageViewById(R.id.curtain_off_btn);
		offBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//WebServiceUtil.Send(oper, "0");
				//GetComm().SendTo(tag,code,"0002");
				MsgUtil.ShowControlLoading(v.getContext());
			}
		});
		ImageView stopBtn = this.getImageViewById(R.id.curtain_stop_btn);
		stopBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//WebServiceUtil.Send(oper, "2");
				//GetComm().SendTo(tag,code,"0000");
				MsgUtil.ShowControlLoading(v.getContext());
			}
		});
		
	}

	public void SetSTA() {
		// TODO Auto-generated method stub
		//this.Start(); 
	}
	protected void GetSTA() {
		// TODO Auto-generated method stub
		 
	}
}