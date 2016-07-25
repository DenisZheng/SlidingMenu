package edu.njupt.zhb.comm;

import java.util.ArrayList;

import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.bean.SenceInfo;

//import com.vann.ahome.bean.AreaInfo;
//import com.vann.ahome.bean.SenceInfo;
//import com.vann.ahome.bean.ShowInfo;

public interface IComm {
//	public List GetDevicesByAreaId(String areaId);

//	public List<AreaInfo> GetAreasByIP(String ip);
//
//	public List<ShowInfo> GetShowsByDevId(String devId);
//
//	public List<SenceInfo> GetSencesByArea(String areaId);
//	
//	public List<SenceInfo> GetSencesByDev(String devId);
	public void  getM3Ip();
	public void ActionData(ArrayList<DeviceInfo> lsit);
    public  ArrayList<DeviceInfo>  getM3Data();
	public String Send(String tag, String code, String value,String revIp);
	public String Read(String tag, String code, String revIp);
	public String ReadState(String tag, String code, String revIp);
	public void Sence(String tag,String sId,String revIp);
	//public String Sence(String tag, String revIp);
}
