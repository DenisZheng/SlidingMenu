package edu.njupt.zhb.slidemenu;

import java.util.List;

import edu.njupt.zhb.activity.AboutActivity;
import edu.njupt.zhb.activity.MyApp;
import edu.njupt.zhb.bean.AreasInfo;
import edu.njupt.zhb.comm.MsgHandler;
import edu.njupt.zhb.slidemenu.R;
import edu.njupt.zhb.utils.AndroidUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主要控制左边按钮点击事件
 * 
 * @author Administrator
 * 
 */
public class LeftSlidingMenuFragment extends Fragment implements
		OnClickListener {
	private View yixinBtnLayout;
	private View circleBtnLayout;
	private View settingBtnLayout;
	private View groupBtnLayout;
	private View listBtnLayout;
	private View timesBtnLayout;
	private String title;
	// private RoundedImageView roundedImageView;
	private Button logregbtn;
	private Button resetbtn;
	private TextView usrName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.main_left_fragment,
				container, false);
		yixinBtnLayout = view.findViewById(R.id.yixinBtnLayout);
		yixinBtnLayout.setOnClickListener(this);
		circleBtnLayout = view.findViewById(R.id.circleBtnLayout);
		circleBtnLayout.setOnClickListener(this);
		groupBtnLayout = view.findViewById(R.id.groupBtnLayout);
		groupBtnLayout.setOnClickListener(this);
		settingBtnLayout = view.findViewById(R.id.settingBtnLayout);
		settingBtnLayout.setOnClickListener(this);
		// roundedImageView = (RoundedImageView) view
		// .findViewById(R.id.headImageView);
		// roundedImageView.setOnClickListener(this);
		listBtnLayout = view.findViewById(R.id.listBtnLayout);
		listBtnLayout.setOnClickListener(this);
		timesBtnLayout = view.findViewById(R.id.timesBtnLayout);
		timesBtnLayout.setOnClickListener(this);
		usrName = (TextView) view.findViewById(R.id.nickNameTextView);
		logregbtn = (Button) view.findViewById(R.id.logregBtn);
		logregbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// intent.setClass(view.getContext(), RegisterActivity.class);
				intent.setClass(view.getContext(), LoginActivity.class);
				// intent.putExtras(bundle);
				startActivityForResult(intent, 1);
			}
		});

		resetbtn = (Button) view.findViewById(R.id.resetBtn);
		resetbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AndroidUtil.SetString(v.getContext(), "admin", "admin", "0");
				usrName.setText("你好，欢迎登录");
				logregbtn.setVisibility(View.VISIBLE);
				resetbtn.setVisibility(View.GONE);
			}
		});

		InitView();
		return view;
	}

	private void InitView() {
		// TODO Auto-generated method stub
		String userName = AndroidUtil.GetUserName(getActivity());
		if (userName.equals("admin")) {
			usrName.setText("你好，欢迎登录");
			logregbtn.setVisibility(View.VISIBLE);
			resetbtn.setVisibility(View.GONE);
		} else {
			usrName.setText(userName);
			logregbtn.setVisibility(View.GONE);
			resetbtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case Activity.RESULT_OK:
			InitView();
			groupBtnLayout.setSelected(true);
			yixinBtnLayout.setSelected(false);
			circleBtnLayout.setSelected(false);
			settingBtnLayout.setSelected(false);
			listBtnLayout.setSelected(false);
			timesBtnLayout.setSelected(false);
			switchFragment(new FragmentDevices());
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		switch (v.getId()) {
		case R.id.yixinBtnLayout:
			newContent = new FragmentPairAtu();
			title = "注册空调";
			yixinBtnLayout.setSelected(true);
			circleBtnLayout.setSelected(false);
			settingBtnLayout.setSelected(false);
			groupBtnLayout.setSelected(false);
			listBtnLayout.setSelected(false);
			timesBtnLayout.setSelected(false);
			break;
		case R.id.settingBtnLayout:
			newContent = new FragmentSetting();
			title = "设置";
			yixinBtnLayout.setSelected(false);
			circleBtnLayout.setSelected(false);
			settingBtnLayout.setSelected(true);
			groupBtnLayout.setSelected(false);
			listBtnLayout.setSelected(false);
			timesBtnLayout.setSelected(false);
			break;
		case R.id.groupBtnLayout:
			newContent = new FragmentDevices();
			title = "空调列表";
			groupBtnLayout.setSelected(true);
			yixinBtnLayout.setSelected(false);
			circleBtnLayout.setSelected(false);
			settingBtnLayout.setSelected(false);
			listBtnLayout.setSelected(false);
			timesBtnLayout.setSelected(false);
			break;
		case R.id.listBtnLayout:
			newContent = new FragmentSences();
			title = "场景";
			listBtnLayout.setSelected(true);
			groupBtnLayout.setSelected(false);
			yixinBtnLayout.setSelected(false);
			circleBtnLayout.setSelected(false);
			settingBtnLayout.setSelected(false);
			timesBtnLayout.setSelected(false);
			break;
		case R.id.timesBtnLayout:
			newContent = new FragmentTimes();
			title = "定时";
			timesBtnLayout.setSelected(true);
			listBtnLayout.setSelected(false);
			groupBtnLayout.setSelected(false);
			yixinBtnLayout.setSelected(false);
			circleBtnLayout.setSelected(false);
			settingBtnLayout.setSelected(false);

			break;
		case R.id.circleBtnLayout:
			newContent = new FragmentShare();
			title = "共享";
			yixinBtnLayout.setSelected(false);
			circleBtnLayout.setSelected(true);
			settingBtnLayout.setSelected(false);
			groupBtnLayout.setSelected(false);
			listBtnLayout.setSelected(false);
			timesBtnLayout.setSelected(false);
			break;
		default:
			break;
		}

		if (newContent != null)
			switchFragment(newContent);

	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		MainActivity ra = (MainActivity) getActivity();
		ra.SetTitles(title);
		ra.switchContent(fragment);

	}
}
