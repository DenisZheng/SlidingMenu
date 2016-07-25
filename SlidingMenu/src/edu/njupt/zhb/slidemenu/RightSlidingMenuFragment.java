package edu.njupt.zhb.slidemenu;



import java.io.UnsupportedEncodingException;
import java.util.List;


import edu.njupt.zhb.bean.AreasInfo;
import edu.njupt.zhb.comm.CommUtil;
import edu.njupt.zhb.slidemenu.AreasActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.njupt.zhb.activity.MyApp;

/**
 * 主要控制右边按钮点击事件
 * @author Administrator
 *
 */
public class RightSlidingMenuFragment extends Fragment implements OnClickListener{
	private View nearbyBtnLayout;
//	private View circleBtnLayout;
//	private View settingBtnLayout;
//	private View groupBtnLayout;
	public String areaId;
	public LinearLayout list;
	public View views;
	public LayoutInflater inflaters;
	public TextView titles;
     @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		
    }
    public void SetTextViews(TextView titles)
    {
    	titles=titles;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	 View view = inflater.inflate(R.layout.main_rigth_fragment, container,false);
    	 inflaters=inflater;
    	 views=view;
    	 getAreasData();
    	return view;
    }
   public void getAreasData()
   {
	  
		 List<AreasInfo> datas = MyApp.getAppInstance().getAreasList();
    	 int i=0;
    	 list = (LinearLayout)views.findViewById(R.id.areaslist);  
    	 for (AreasInfo data : datas) {  
  
    		 final String aid=data.getId();
    		 final String aname=data.getName();
       	     View areaItem = inflaters.inflate(R.layout.area_list_item, null);
        	 TextView titlename=(TextView)areaItem.findViewById(R.id.toolbox_title);
        	 try {
				titlename.setText(new String(aname.getBytes(),"utf8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 Button btnset=(Button)areaItem.findViewById(R.id.btnset);
        	 btnset.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("aid", aid);
					bundle.putString("aname",aname);
					intent.setClass(getActivity(), AreasActivity.class);
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
				}
			});
        	 areaItem.setTag(data);
       	     areaItem.setOnClickListener(this);
       	     if(i==0){
       		  MyApp.getAppInstance().setMTag(data.getId());
       	      areaItem.setSelected(true);
       	     }
       	     list.addView(areaItem);
       	     i++;
    	 }
    	 Button btnAdd=(Button)views.findViewById(R.id.btnAdd);
    	 btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("aid", "");
				bundle.putString("aname", "");
				intent.setClass(getActivity(), AreasActivity.class);
				intent.putExtras(bundle);
			 getActivity().startActivity(intent);
			}
		}); 
   }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { 
    	switch (resultCode) {
    	case 1:
    		// 子窗口ChildActivity的回传数据 
    		if (data != null) {
    			Bundle bundle = data.getExtras(); 
    			if (bundle != null) { 
    				//处理代码在此地 
    				 getAreasData();
    				} 
    		}
    		break;
    	default: //其它窗口的回传数据
    		break;
    	}
    }
	@Override
	public void onClick(View v) {
		Fragment newContent = new FragmentDefaultMain();
		if (newContent != null)
		{
			AreasInfo info=(AreasInfo)v.getTag();
			MyApp.getAppInstance().setMTag(info.getId());
			for (int i = 0; i < list.getChildCount(); i++) {
				View view = list.getChildAt(i);
				if(view.getTag()==v.getTag()){
					 view.setSelected(true);
				}else{
					 view.setSelected(false);
				}
			}

			switchFragment(newContent);
			
		}
		
	}
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			
		}
		
	};
	
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragment);
		
	}
}
