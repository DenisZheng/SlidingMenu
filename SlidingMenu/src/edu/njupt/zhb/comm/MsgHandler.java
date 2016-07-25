package edu.njupt.zhb.comm;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MsgHandler extends Handler {
	private Context context;

	public MsgHandler(Context context) {
		this.context = new WeakReference<Context>(context).get();
	}

	@Override
	public void handleMessage(Message msg) {

		switch (msg.arg1) {
		case 1:
			showInfo("网关名称，房间名称不能空白!");
			break;
		case 2:
			showInfo("配置失败，请重试！");
			break;
		case 3:
			showInfo("wifi密码不能为空");
			break;
		case 4:
			showInfo("配置成功，请切换到您的WIFI环境。");
			break;
		case 5:
			showInfo("用户账户是必填项！");
			break;
		case 6:
			showInfo("用户口令是必填项！");
			break;
		case 7:
			showInfo("请填写手机号码");
			break;
		case 0:
			showInfo("此手机号码已经注册");
			break;
		case -1:
			showInfo("没有该用户账户");
			break;
		case -2:
			showInfo("接口密钥不正确");
			break;
		case -3:
			showInfo("短信数量不足");
			break;
		case -4:
			showInfo("手机号格式不正确");
			break;
		case -6:
			showInfo("IP限制");
			break;
		case -11:
			showInfo("该用户被禁用");
			break;
		case -14:
			showInfo("短信内容出现非法字符");
			break;
		case -21:
			showInfo("MD5接口密钥加密不正确");
			break;
		case -41:
			showInfo("手机号码为空");
			break;
		case -42:
			showInfo("短信内容为空");
			break;
		case -51:
			showInfo("短信签名格式不正确");
			break;
		case -100:
			showInfo("请等待60s再重试");
			break;
		case -101:
			showInfo("手机号码错误");
			break;
		case -102:
			showInfo("校码码错误");
			break;
		case -103:
			showInfo("注册失败");
			break;
		case 100:
			showInfo("发送成功");
			break;
		case 101:
			showInfo("密码不一致，请重输入");
			break;
		case 102:
			showInfo("注册成功");
			break;
		case -104:
			showInfo("登陆失败");
			break;
		case -105:
			showInfo("本地控制失败，正在切换远程控制");
			break;
		case -106:
			showInfo("通讯失败，请检查网络");
			break;
		case -107:
			showInfo("当前无场景，请重新设置");
			break;
		case 103:
			showInfo("切换远程控制成功");
			break;
		default:
			break;
		}
		super.handleMessage(msg);
	}

	/**
	 * 显示提示信息
	 * 
	 * @param info
	 */
	public void showInfo(String info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}
}
