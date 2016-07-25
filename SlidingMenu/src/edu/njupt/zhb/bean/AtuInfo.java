package edu.njupt.zhb.bean;

import java.io.Serializable;

import android.graphics.Bitmap.Config;

public class AtuInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8794963147627774185L;
	private String atuId;

	public String getAtuId() {
		return atuId;
	}

	public void setAtuId(String atuId) {
		this.atuId = atuId;
	}

	public String getAtuCode() {
		return atuCode;
	}

	public void setAtuCode(String atuCode) {
		this.atuCode = atuCode;
	}

	public String getAtuName() {
		return atuName;
	}

	public void setAtuName(String atuName) {
		this.atuName = atuName;
	}

	public String getAtuRoom() {
		return atuRoom;
	}

	public void setAtuRoom(String atuRoom) {
		this.atuRoom = atuRoom;
	}

	public String getAtuPwd() {
		return atuPwd;
	}

	public void setAtuPwd(String atuPwd) {
		this.atuPwd = atuPwd;
	}

	public String getAtuIp() {
		return atuIp;
	}

	public void setAtuIp(String atuIp) {
		this.atuIp = atuIp;
	}

	private String atuCode;
	private String atuName;
	private String atuRoom;
	private String atuPwd;
	private String atuIp;

	public String getAtuRemoIp() {
		return atuRemoIp;
	}

	public void setAtuRemoIp(String atuRemoIp) {
		this.atuRemoIp = atuRemoIp;
	}

	public int getAtuRemoPort() {
		return atuRemoPort;
	}

	public void setAtuRemoPort(int atuRemoPort) {
		this.atuRemoPort = atuRemoPort;
	}

	

	private String atuRemoIp;
	private int atuRemoPort;
	private int  isRemote=-1;
	public int getIsRemote() {
		return isRemote;
	}

	public void setIsRemote(int isRemote) {
		this.isRemote = isRemote;
	}

	public boolean  expand;
	
	

	public AtuInfo() {

	}

	public AtuInfo(String atuId, String atuCode, String atuName,
			String atuRoom, String atuPwd, String atuIp,String atuRemoIp,int atuRemoPort,int isRemote) {
		this.atuId = atuId;
		this.atuCode = atuCode;
		this.atuName = atuName;
		this.atuRoom = atuRoom;
		this.atuPwd = atuPwd;
		this.atuIp = atuIp;
		this.atuRemoIp=atuRemoIp;
		this.atuRemoPort=atuRemoPort;
		this.isRemote=isRemote;
	}
}
