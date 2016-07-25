package edu.njupt.zhb.bean;

public class AreasDevicesInfo extends BaseInfo {

    private String tableName="AreaDevice";
	public String getTableName()
	{
		return tableName;
	}

	private String devId;
	private String areaId;
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
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
