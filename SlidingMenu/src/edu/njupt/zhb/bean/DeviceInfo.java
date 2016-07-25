package edu.njupt.zhb.bean;

import java.util.Map;

public class DeviceInfo extends BaseInfo {

	/*
	       "id": "k041", 
            "name": "k041", 
            "otherName": "���", 
            "devId": "k04",
            "devNum": "1",
            "revIp": "172.16.15.249",
            "state": "001",
            "uiCode": "k",
            "uiImg": "",
            "areaId":"1"
    */
	private String devId;
	private String devNum;
	private String revIp;
	private String state;
	private String uiCode;
	private String areaId;
	private String isAdmin;
	public String getLogPwd() {
		return logPwd;
	}
	public void setLogPwd(String logPwd) {
		this.logPwd = logPwd;
	}

	private String logPwd;
	private String tableName="Devices";
	public String getTableName()
	{
			return tableName;
	}
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getUiCode() {
		return uiCode;
	}

	public void setUiCode(String uiCode) {
		this.uiCode = uiCode;
	}
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	public String getRevIp() {
		return revIp;
	}

	public void setRevIp(String revIp) {
		this.revIp = revIp;
	}
	public String getDevNum() {
		return devNum;
	}

	public void setDevNum(String devNum) {
		this.devNum = devNum;
	}
	@Override
	public void InitMap(DataModel mode) {
		// TODO Auto-generated method stub
		this.devId = mode.getId();
		super.Init(mode, 1,devId);
	//	this.devName =mode.getOtherName();
	//	this.exeType=map.get("EXE_TYPE").toString();
	//	this.devImg = map.get("Dev_IMG").toString();
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}
}