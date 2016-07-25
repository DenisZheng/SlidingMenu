package edu.njupt.zhb.slidemenu;

import java.util.ArrayList;

import edu.njupt.zhb.slidemenu.SwicthIShow;

import edu.njupt.zhb.activity.BaseActivity;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.bean.PublicData;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.slidemenu.R;
import edu.njupt.zhb.utils.ImageUtil;
import edu.njupt.zhb.view.IShow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 易信页面
 * @author Administrator
 *
 */
public class FragmentDefaultMain extends Fragment  implements OnClickListener{
	public ArrayList<DeviceInfo> dlist;
	public ArrayList<DeviceInfo> devlist;
	public TextView title;
	private String flag="0";
	public int top=0;
	public View context;
	private boolean restart = true;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_areas, container, false);
    	context=view;
    	top=view.getHeight();
    	getAreasDevice();
    	return view;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity().getApplicationContext(), "很好！", Toast.LENGTH_SHORT).show();
	}
	public void getAreasDevice()
	{
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
);
		
		LinearLayout bottomLay=(LinearLayout)context.findViewById(R.id.dtypeslist);
		bottomLay.removeAllViews();
		bottomLay.setOrientation(LinearLayout.HORIZONTAL);
		bottomLay.setGravity(Gravity.BOTTOM);
	    ImageView devDefault=new ImageView(context.getContext());
		int i=0;
		dlist=new ArrayList<DeviceInfo>();
	    dlist=MyApp.getAppInstance().getComm().getRoomsDeviceData(MyApp.getAppInstance().getMTag());
	    String code="";
	    for(DeviceInfo dev :MyApp.getAppInstance().getComm().devlist)
	    {//dev.getUiCode().toLowerCase()ImageUtil.drawText(this, R.drawable.bm01,  25.0f, 0,20,"模式");
	    	code=dev.getUiCode().toLowerCase();
	    	String typename=PublicData.getDeviceTypeName(code);
	    	Bitmap icon =ImageUtil.drawText(context.getContext(),code,28.0f,0,10,typename);
	    	Bitmap resizedBitmap = Bitmap.createScaledBitmap(icon , 150, 120, true);
			dev.setImage(icon); 
			
			Bitmap icon_b =ImageUtil.drawText(context.getContext(),code+"_b",28.0f,0,10,typename);
	    	Bitmap resizedBitmap_b = Bitmap.createScaledBitmap(icon_b , 150, 120, true);
			dev.setImage_b(icon_b); 
			
			ImageView image = new ImageView(context.getContext());
			//image.setImageBitmap(icon);
			if(i==0)
			{
				MyApp.getAppInstance().setDevCode(dev.getUiCode());
			    image.setImageDrawable(new BitmapDrawable(resizedBitmap_b));
			}
			else
				image.setImageDrawable(new BitmapDrawable(resizedBitmap));	
			LayoutParams lp=new LayoutParams(150, 135);
			image.setLayoutParams(lp);
			image.setTag(dev);;
			image.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					//defaultDevView(v);
					ImageView image = (ImageView) v;
					changeTypeImg();
					 DeviceInfo dev=(DeviceInfo)v.getTag();
					 Bitmap icon = (Bitmap)dev.getImage_b();
					 image.setImageBitmap(icon);
					 MyApp.getAppInstance().setDevCode(dev.getUiCode());
					 getDeviceDetail();
				}
			});
			bottomLay.addView(image);
			image.setPadding(image.getPaddingLeft()+10,image.getPaddingTop(),image.getPaddingRight(),image.getPaddingBottom());
			if(i==0)
			{
				flag="1";
				devDefault=image;
				
			}
			i++;
	    }
	    getDeviceDetail();
	    
	}
	public void changeTypeImg()
	{
		 LinearLayout bottomLay=(LinearLayout)context.findViewById(R.id.dtypeslist);
		for (int i = 0; i < bottomLay.getChildCount(); i++) {
			View view = bottomLay.getChildAt(i);
			if (view instanceof ImageView) {
				ImageView image = (ImageView) view;
				 DeviceInfo dev=(DeviceInfo)image.getTag();
				 Bitmap icon = (Bitmap)dev.getImage();
				image.setImageBitmap(icon);
			}
		}
	}
	boolean isrun = false;
	public MyApp appState;
	IShow curView;
	ArrayList<IShow> IShowList;
	ArrayList<DataModel> dmlist;
	public void getDeviceDetail()
	{
		restart = false;
		StopThread();
		IShowList = new ArrayList();
		dmlist = new ArrayList();
		if (curView != null)
			curView.Stop();
		LinearLayout bottomLay=(LinearLayout)context.findViewById(R.id.scId);
		bottomLay.removeAllViews();
		 for(DeviceInfo data :dlist){
			 if(MyApp.getAppInstance().getDevCode().equals(data.getUiCode())){
			 DataModel sdata = new DataModel();
				sdata.setDevId(data.getDevId());
				sdata.setDevNum(data.getDevNum());
				sdata.setName(data.getName());
				sdata.setOtherName(data.getOtherName());
				sdata.setState(data.getState());
				sdata.setRevIp(data.getRevIp());
				sdata.setUiCode(data.getUiCode());
				dmlist.add(sdata);
				IShow tmpView = SwicthIShow
						.getView(context.getContext(), sdata, BaseActivity.GetComm());
				if (tmpView != null) {
					try {
						tmpView.InitView();
						//tmpView.GetSTA();
						curView = tmpView;
						IShowList.add(tmpView);
					} catch (Exception e) {

					}
					bottomLay.addView(tmpView.GetView());
				} 
			 }
		 }
	}
	private void StopThread() {
		appState.getAppInstance().setRunState(false);
		isrun = false;
	}
	public void getAreasSence(View v)
	{
		
	}
}
