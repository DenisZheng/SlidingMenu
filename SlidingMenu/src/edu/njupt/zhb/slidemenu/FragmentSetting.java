package edu.njupt.zhb.slidemenu;

import edu.njupt.zhb.slidemenu.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;

public class FragmentSetting extends Fragment{
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_setting, container, false);
    	LinearLayout updatapwd=(LinearLayout)view.findViewById(R.id.updatapwd);
    	updatapwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
	    		Bundle bundle=new Bundle();
	    		intent.setClass(getActivity(), UpdataUserPwdActivity.class);
	    		intent.putExtras(bundle);
	    		getActivity().startActivity(intent);
			}
		});
//    	LinearLayout changUser=(LinearLayout)view.findViewById(R.id.changUser);
//    	changUser.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent();
//	    		Bundle bundle=new Bundle();
//	    		intent.setClass(getActivity(), LoginActivity.class);
//	    		intent.putExtras(bundle);
//	    		getActivity().startActivity(intent);
//	    		getActivity().finish();
//			}
//		});
//    	LinearLayout backup=(LinearLayout)view.findViewById(R.id.backup);
//    	backup.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent();
//	    		Bundle bundle=new Bundle();
//	    		intent.setClass(getActivity(), BackUpActivity.class);
//	    		intent.putExtras(bundle);
//	    		getActivity().startActivity(intent);
//			}
//		});
    	return view;
    }
}
