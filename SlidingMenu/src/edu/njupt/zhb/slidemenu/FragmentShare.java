package edu.njupt.zhb.slidemenu;

import edu.njupt.zhb.comm.ShareClient;
import edu.njupt.zhb.comm.ShareSever;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentShare extends Fragment {

	public View context;
	public int top = 0;
	private TextView iptxt;
	private Button connbtn;
	private Button sharbtn;
	private EditText searchip;
	private String IPAddress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_share, container, false);
		context = view;
		top = view.getHeight();
		IPAddress = getlocalip();
		iptxt = (TextView) context.findViewById(R.id.shareip);
		iptxt.setText("本地WIFI-IP地址:" + IPAddress);
		searchip = (EditText) context.findViewById(R.id.searchip);
		connbtn = (Button) context.findViewById(R.id.connbtn);
		connbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(searchip.getText())) {
					new ShareSever(searchip.getText().toString().trim(),
							context.getContext(), getFragmentManager()).start();
				} else {
					return;
				}
			}
		});
		sharbtn = (Button) context.findViewById(R.id.sharbtn);
		sharbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(searchip.getText())) {
					new ShareClient(searchip.getText().toString().trim(),
							context.getContext(), getFragmentManager()).start();
				} else {
					return;
				}
			}
		});
		return view;
	}

	private String getlocalip() {
		WifiManager wifiManager = (WifiManager) context.getContext()
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		// Log.d(Tag, "int ip "+ipAddress);
		if (ipAddress == 0)
			return null;
		return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
				+ (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
	}

}
