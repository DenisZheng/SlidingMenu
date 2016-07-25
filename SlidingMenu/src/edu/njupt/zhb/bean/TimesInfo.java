package edu.njupt.zhb.bean;

import java.util.Map;

import edu.njupt.zhb.bean.BaseInfo;

public class TimesInfo extends BaseInfo {

	private String tableName="Times";
	public String getTableName()
	{
			return tableName;
	}
	private String devId;
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	@Override
	public void InitMap(DataModel mode) {
		// TODO Auto-generated method stub
		//this.exeType=map.get("EXE_TYPE").toString();
		//this.senceImg = map.get("SENCE_IMG").toString();
		
		
	}

}