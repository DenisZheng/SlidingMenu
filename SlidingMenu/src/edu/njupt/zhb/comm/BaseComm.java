package edu.njupt.zhb.comm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.vann.ahome.bean.AreaInfo;
//import com.vann.ahome.bean.BaseInfo;
//import com.vann.ahome.bean.DeviceInfo;
//import com.vann.ahome.bean.SenceInfo;
//import com.vann.ahome.bean.ShowInfo;
import edu.njupt.zhb.utils.XmlUtil;

public class BaseComm {
	
	public String ByteToHexString(byte[] raw) {
		String HEXES = "0123456789ABCDEF";
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
					HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
	
	public  byte[] HexStringToByte(String src) {
		src = src.trim();
		int len = src.length();
		byte[] ret = new byte[len / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < len; i += 2) {
			ret[i / 2] = uniteBytes(tmp[i], tmp[i + 1]);
		}
		return ret;
	}
	private  byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	public byte[] GetTagByte(String tag){
		int ss = Integer.valueOf(tag);
		String hex = Integer.toHexString(ss);
		if(hex.length()==1){
			hex = "000"+hex;
		}
		if(hex.length()==2){
			hex = "00"+hex;
		}
		if(hex.length()==3){
			hex = "0"+hex;
		}
		return HexStringToByte(hex);
	}
	
//	public List GetDevices(String xml){
//		List<Map> datas= XmlUtil.XmlStringToList(xml);
//		List devs = new ArrayList();
//		for (Map map : datas) {
//			BaseInfo area = new DeviceInfo();
//			area.InitMap(map);
//			devs.add(area);
//		}
//		return devs;
//	}
//	
//	public List GetAreas(String xml){
//		List<Map> datas= XmlUtil.XmlStringToList(xml);
//		List areas = new ArrayList();
//		for (Map map : datas) {
//			AreaInfo area = new AreaInfo();
//			//area.setHostIp(ip);
//			area.InitMap(map);
//			areas.add(area);
//		}
//		return areas;
//	}
//	public List GetShows(String xml){
//		List<Map> datas= XmlUtil.XmlStringToListByMap(xml);
//		List shows = new ArrayList();
//		for (Map map : datas) {
//			BaseInfo area = new ShowInfo();
//			area.InitMap(map);
//			shows.add(area);
//		}
//		return shows;
//	}
//	public List GetSences(String xml){
//		List<Map> datas= XmlUtil.XmlStringToList(xml);
//		List sences = new ArrayList();
//		for (Map map : datas) {
//			SenceInfo sence = new SenceInfo();
//			//area.setHostIp(ip);
//			sence.InitMap(map);
//			sences.add(sence);
//		}
//		return sences;
//	}
}
