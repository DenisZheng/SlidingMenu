package edu.njupt.zhb.bean;

import java.util.Map;

import edu.njupt.zhb.bean.BaseInfo;

public class SenceInfo extends BaseInfo {

	private String tableName="Scene";
	public String getTableName()
	{
			return tableName;
	}
	private String areaId;
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public void InitMap(DataModel mode) {
		// TODO Auto-generated method stub
		//this.exeType=map.get("EXE_TYPE").toString();
		//this.senceImg = map.get("SENCE_IMG").toString();
		
		
	}

}