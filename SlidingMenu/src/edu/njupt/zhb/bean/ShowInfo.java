package edu.njupt.zhb.bean;

import java.util.Map;

import android.util.Log;

public  class ShowInfo extends BaseInfo {

	

	private String showId;
	
	private String showName;
	
	private String ucCode;
	private Map tagList;
	public String getUcCode() {
		return ucCode;
	}

	public void setUcCode(String ucCode) {
		this.ucCode = ucCode;
	}

	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	public Map getTagList() {
		return tagList;
	}

	public void setTagList(Map tagList) {
		this.tagList = tagList;
	}
	@Override
	public void InitMap(DataModel mode) {
		// TODO Auto-generated method stub
		
	}

}