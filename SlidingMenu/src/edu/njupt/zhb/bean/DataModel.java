package edu.njupt.zhb.bean;

import java.util.ArrayList;
import java.util.List;


public class DataModel {
	
	private String Id;
	private String DevId;
	private String revIp;
	private String State;
	private String Name;
	private String OtherName;
	private String UiImg;
	private String UiCode;
	private String areaId;
	private String DevNum;
	private String keys;
	private String sType;
	private ArrayList<DataModel> dataModel;
	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}
	public String getDevId() {
		return DevId;
	}

	public void setDevId(String DevId) {
		this.DevId = DevId;
	}
	public String getRevIp() {
		return revIp;
	}

	public void setRevIp(String revIp) {
		this.revIp = revIp;
	}
	public String getState() {
		return State;
	}

	public void setState(String State) {
		this.State = State;
	}
	public String getDevNum() {
		return DevNum;
	}
	public void setDevNum(String DevNum) {
		this.DevNum = DevNum;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}
	public String getOtherName() {
		return OtherName;
	}

	public void setOtherName(String OtherName) {
		this.OtherName = OtherName;
	}
	public String getUiCode() {
		return UiCode;
	}

	public void setUiCode(String UiCode) {
		this.UiCode = UiCode;
	}
	public String getUiImg() {
		return UiImg;
	}

	public void setUiImg(String UiImg) {
		this.UiImg = UiImg;
	}
	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}
	public String getSType() {
		return sType;
	}

	public void setSType(String sType) {
		this.sType = sType;
	}
	public ArrayList<DataModel> getDataModel() {
		return dataModel;
	}

	public void setDataModel(ArrayList<DataModel> dataModel) {
		this.dataModel = dataModel;
	}
	private Object image;

	public Object getImage() {
		return image;
	}

	public void setImage(Object image) {
		this.image = image;
	}
}
