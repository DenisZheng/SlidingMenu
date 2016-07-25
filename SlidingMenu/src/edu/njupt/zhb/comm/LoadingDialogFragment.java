package edu.njupt.zhb.comm;

import edu.njupt.zhb.slidemenu.R;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LoadingDialogFragment extends DialogFragment {
	private View v;
	Context mContext;
	String ltxt;

	public LoadingDialogFragment() {
		mContext = getActivity();
	}

	public LoadingDialogFragment(String lltxt) {
		mContext = getActivity();
		ltxt = lltxt;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.findatu_loading, container, false);
		TextView loadtxt = (TextView) v.findViewById(R.id.loadtxt);
		loadtxt.setText(ltxt);
		return v;
	}

	
	
	public static LoadingDialogFragment newInstance() {
		LoadingDialogFragment f = new LoadingDialogFragment();
		return f;
	}
	
	public static LoadingDialogFragment newInstance(String lltxt) {
		LoadingDialogFragment f = new LoadingDialogFragment(lltxt);
		return f;
	}

}
