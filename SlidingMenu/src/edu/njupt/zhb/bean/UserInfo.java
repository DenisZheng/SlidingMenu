package edu.njupt.zhb.bean;

public class UserInfo extends BaseInfo {

	private String pwd;
    public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	private String current;
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	private int isadmin;
	public int getIsadmin() {
		return isadmin;
	}
	public void setIsadmin(int isadmin) {
		this.isadmin = isadmin;
	}
	private String tableName="Users";;
	public String getTableName()
	{
		return tableName;
	}
	@Override
	public void InitMap(DataModel mode) {
		// TODO Auto-generated method stub
	}
}
