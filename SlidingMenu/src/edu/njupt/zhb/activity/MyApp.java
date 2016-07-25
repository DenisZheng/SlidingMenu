package edu.njupt.zhb.activity;

import java.util.List;

import edu.njupt.zhb.bean.AreasInfo;
import edu.njupt.zhb.comm.CommUtil;

public class MyApp {

	private MyApp() {
	}// 单例模式中，封闭创建实例接口

	public static MyApp instance = null;
	private List<AreasInfo> areasList;

	public List<AreasInfo> getAreasList() {
		return areasList;
	}

	public void setAreasList(List<AreasInfo> areasList) {
		this.areasList = areasList;
	}

	private CommUtil comm;

	public CommUtil getComm() {
		return comm;
	}

	public void setComm(CommUtil comm) {
		this.comm = comm;
	}

	private String pwd;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private String mtag;

	public String getMTag() {
		return mtag;
	}

	public void setMTag(String mtag) {
		this.mtag = mtag;
	}

	private String devCode;

	public String getDevCode() {
		return devCode;
	}

	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}

	private boolean isRunState;

	public boolean isRunState() {
		return isRunState;
	}

	public void setRunState(boolean isRunState) {
		this.isRunState = isRunState;
	}

	public static MyApp getAppInstance() {
		if (instance == null) {
			synchronized (MyApp.class) {
				if (instance == null)
					instance = new MyApp();
			}
		}
		return instance;
	}
}
