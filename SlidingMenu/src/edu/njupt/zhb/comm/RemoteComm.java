package edu.njupt.zhb.comm;

import java.util.ArrayList;
import java.util.List;

import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.bean.DeviceInfo;
import edu.njupt.zhb.bean.SenceInfo;

import android.util.Log;

//import com.vann.ahome.bean.AreaInfo;
//import com.vann.ahome.bean.SenceInfo;
//import com.vann.ahome.bean.ShowInfo;

public class RemoteComm extends BaseComm implements IComm {

	static String _hostId ="";
	public RemoteComm(String serverIp,String hostIp){
		UdpClient.ip = serverIp;
		_hostId = hostIp+",";
	}
	public void  getM3Ip()
	{
		UdpClient.getHomeGate("$vnm,ask,");
		
	}
	public void ActionData(ArrayList<DeviceInfo> lsit){
		
	}
	public  ArrayList<DeviceInfo>  getM3Data()
	{
		return new ArrayList<DeviceInfo>();
		
	}
	public static String GetHostIP(String serverIp,String userName,String passWord){
		UdpClient.ip = serverIp;
		String xml = UdpClient.getHostIp(userName,passWord);
		if(xml==null) return null;
		xml =xml.trim();
		return xml;
	}
	
//	@Override
//	public List GetDevicesByAreaId(String areaId) {
//		String xml = UdpClient.getHomeGate(_hostId+"@GetAreaDevData&aid="+areaId);
//		return GetDevices(xml);
//	}
//
//	@Override
//	public List<AreaInfo> GetAreasByIP(String ip) {
//		String xml = UdpClient.getHomeGate(_hostId+"@GetAreaData&aid=-1");
//		Log.i("aaaaaaa", xml);
//		return GetAreas(xml);
//	}
//
//	@Override
//	public List<ShowInfo> GetShowsByDevId(String devId) {
//		String xml = UdpClient.getHomeGate(_hostId+"@GetAreaDevTagData&aid=0&did="+devId);
//		Log.i("aaaaaa", xml);
//		return GetShows(xml);
//	}
//
//	@Override
//	public List<SenceInfo> GetSencesByArea(String areaId) {
//		String xml = UdpClient.getHomeGate(_hostId+"@GetAreaSenceData&aid="+areaId);
//		Log.i("GetSencesByArea", xml);
//		return GetSences(xml);
//	}
//
//	@Override
//	public List<SenceInfo> GetSencesByDev(String devId) {
//		String xml = UdpClient.getHomeGate(_hostId+"@GetDevSenceData&did="+devId);
//		Log.i("GetSencesByArea", xml);
//		return GetSences(xml);
//	}

	private byte[] GetHostIPByte(){
		String ss =_hostId.substring(0,_hostId.length()-1);
		String[] ips =ss.split("\\.");
		byte[] rs = new byte[]{Integer.valueOf(ips[0]).byteValue(),Integer.valueOf(ips[1]).byteValue(),Integer.valueOf(ips[2]).byteValue(),Integer.valueOf(ips[3]).byteValue()};
		return rs;
	}
	
	@Override
	public String Send(String tag, String code, String value, String revIp) {
		// TODO Auto-generated method stub
		
		//0E 01 00 78 02 00 01 0F
		byte[] sendBuf = new byte[12];
		sendBuf[0] = 0x0e;
		sendBuf[1] = 0x01;
		
		sendBuf[2] = this.GetTagByte(tag)[0];
		sendBuf[3] =  this.GetTagByte(tag)[1];
		
		sendBuf[4] =  Integer.valueOf(code).byteValue();
		byte[] val = HexStringToByte(value);
		sendBuf[5] = val[0];
		sendBuf[6] = val[1];		
		sendBuf[7] = 0x0f;
		System.arraycopy(GetHostIPByte(), 0, sendBuf, 8, 4);
		Log.i("CMD", tag+"==="+ByteToHexString(sendBuf));
		String rev = UdpClient.sendHomeGate(sendBuf,revIp);
		Log.i("REV", rev);
		return rev;
	}
	
	public String Read(String tag, String code, String revIp){
		byte[] sendBuf = new byte[12];
		sendBuf[0] = 0x0e;
		sendBuf[1] = 0x02;
		
		sendBuf[2] = this.GetTagByte(tag)[0];
		sendBuf[3] =  this.GetTagByte(tag)[1];
		
		sendBuf[4] =  Integer.valueOf(code).byteValue();
		sendBuf[5] = 0x00;
		sendBuf[6] = 0x00;		
		sendBuf[7] = 0x0f;
		System.arraycopy(GetHostIPByte(), 0, sendBuf, 8, 4);
		Log.i("CMD", tag+"==="+ByteToHexString(sendBuf));
		String rev = UdpClient.sendHomeGate(sendBuf,revIp);
		Log.i("REV", rev);
		
		return rev;
	}
	public String ReadState(String tag, String code, String revIp){
		
		
		return "0";
	}
	public String Sence(String tag, String revIp){
		byte[] sendBuf = new byte[12];
		sendBuf[0] = 0x0e;
		sendBuf[1] = 0x03;
		
		sendBuf[2] = this.GetTagByte(tag)[0];
		sendBuf[3] =  this.GetTagByte(tag)[1];
		
		sendBuf[4] = 0x00;
		sendBuf[5] = 0x00;
		sendBuf[6] = 0x00;		
		sendBuf[7] = 0x0f;
		System.arraycopy(GetHostIPByte(), 0, sendBuf, 8, 4);
		Log.i("CMD", tag+"==="+ByteToHexString(sendBuf));
		String rev = UdpClient.sendHomeGate(sendBuf,revIp);
		Log.i("REV", rev);
		
		return rev;
	}
	public void Sence(String tag,String sId,String revIp){
		
	}
}
