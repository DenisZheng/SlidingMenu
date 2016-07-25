package edu.njupt.zhb.bean;

import java.util.Map;

import edu.njupt.zhb.bean.BaseInfo;

public class SenceDetailInfo extends BaseInfo {

	private String tableName="SceneDetail";
	public String getTableName()
	{
			return tableName;
	}
	private String senceId;
	public String getSenceId() {
		return senceId;
	}
	public void setSenceId(String senceId) {
		this.senceId = senceId;
	}
	private String devId;
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	//statusOpen
	private String statusOpen;
	public String getStatusOpen() {
		return statusOpen;
	}
	public void setStatusOpen(String statusOpen) {
		this.statusOpen = statusOpen;
	}
	private String statusTemp;
	public String getStatusTemp() {
		return statusTemp;
	}
	public void setStatusTemp(String statusTemp) {
		this.statusTemp = statusTemp;
	}
	private String statusVolume;
	public String getStatusVolume() {
		return statusVolume;
	}
	public void setStatusVolume(String statusVolume) {
		this.statusVolume = statusVolume;
	}
	private String statusMode;
	public String getStatusMode() {
		return statusMode;
	}
	public void setStatusMode(String statusMode) {
		this.statusMode = statusMode;
	}
	@Override
	public void InitMap(DataModel mode) {
		// TODO Auto-generated method stub
		//this.exeType=map.get("EXE_TYPE").toString();
		//this.senceImg = map.get("SENCE_IMG").toString();
		
		
	}

}