package edu.njupt.zhb.comm;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import android.R.integer;
import android.util.Log;
import edu.njupt.zhb.bean.AreasInfo;
import edu.njupt.zhb.bean.DeviceInfo;
//import com.vann.ahome.bean.AreaInfo;
//import com.vann.ahome.bean.BaseInfo;
//import com.vann.ahome.bean.DeviceInfo;
//import com.vann.ahome.bean.SenceInfo;
//import com.vann.ahome.bean.ShowInfo;

public class CommUtil extends BaseComm implements IComm {

	public static final String ATU_CMD_FIND = "FindAtu,"; //
	public static final String ATU_CMD_LOGIN = "LoginAtu,";
	public static final String ATU_CMD_INCLOUD = "InCloud,";
	public static final String ATU_CMD_NMROOM = "nmRoom,";
	public static final String ATU_CMD_WRSCENE = "wrScene,";

	public static final String ATU_CMD_WPA = "network={"; // wpa_supplicant.conf
	public static final String ATU_CMD_TIME = "SetTime:"; // +"2014-05-01 11:00:00"
	public static final String ATU_CMD_NETWORK = "SetNetwork:"; // network_config
	public static final String ATU_CMD_PUNCHER = "SetPuncher:"; // network_config
	public static final String ATU_CMD_ALIAS = "Alias,"; // alias,group,

	public static final String ATU_SERVER_ACK = "AtuServer=";
	public static final String ATU_CMD_ACK = "Command=";

	public static final String ATU_CTR_SLOW = "SLOW";

	// public String findatu = "FindAtu,";
	public String heads = "InCloud,";
	public String aut_ser = "001204011DA0";
	public String login_id = "6B8B4567";
	public ArrayList<DeviceInfo> devlist;
	public String serverIp;
	private AtuUdpInterface mInterface = null;

	public CommUtil() {

	}

	public CommUtil(String ip, int port) {
		serverIp = ip;
		UdpClient.ip = ip;
		UdpClient.operPort = port;
	}

	public CommUtil(String ip, int port, AtuUdpInterface atuInterface) {
		serverIp = ip;
		UdpClient.ip = ip;
		UdpClient.operPort = port;
		mInterface = atuInterface;
	}

	public ArrayList<DeviceInfo> datalist;

	public void getIp() {
		if (UdpClient.ip == null)
			UdpClient.ip = serverIp;
	}

	// init data
	// public void IntData() {
	// getIp();
	// aut_ser = UdpClient.getHomeGate(findatu);
	// if (aut_ser.length() > 12)
	// aut_ser = aut_ser.replace("AtuServer=", "").replace(",", "")
	// .replace(" ", "").substring(0, 12);
	// }

	public DatagramPacket FindAtu(String svrip) {
		return UdpClient.FindAtu(svrip, 9559);
	}

	public Integer FindAtuCmd(String svrip, int Port,
			AtuUdpInterface atuInterface) {
		//mInterface = atuInterface;
		Integer iCount = 0;
		DatagramPacket packet = UdpClient.FindAtu(svrip, Port);
		if (packet != null) {
			String strAck = EncodingUtils.getString(packet.getData(), "GBK")
					.toString().trim();
			if (strAck.contains("AtuServer=")) {
				String atuId = strAck.replace("AtuServer=", "")
						.replace(",", "").replace(" ", "").substring(0, 12);
				iCount = atuId.length();
				if (atuInterface != null) {
					atuInterface.ReceiveCallback(packet.getAddress()
							.getHostAddress(), atuId); // the
				}
			} else if (strAck.contains("Login=0,")) { // "Login=0"
				iCount = -200; // no login
			} else if (strAck.contains("Locked by admin,")) { // Locked by
																// // admin,
				iCount = -2;
			} else if (strAck.contains("Command_OK")) {
				iCount = 1;
			} else {
				iCount = -100;
			}

		}
		return iCount;
	}
	
	
	public Integer FindSingleAtuCmd(String mac,String svrip, int Port,
			AtuUdpInterface atuInterface) {
		//mInterface = atuInterface;
		Integer iCount = 0;
		String strAck = UdpClient.FindSingleAtu(mac,svrip, Port,atuInterface);
		if (strAck != null) {
			if (strAck.contains("AtuServer=")) {
//				String atuId = strAck.replace("AtuServer=", "")
//						.replace(",", "").replace(" ", "").substring(0, 12);
				iCount = strAck.length();
				
			} else if (strAck.contains("Login=0,")) { // "Login=0"
				iCount = -200; // no login
			} else if (strAck.contains("Locked by admin,")) { // Locked by
																// // admin,
				iCount = -2;
			} else if (strAck.contains("Command_OK")) {
				iCount = 1;
			} else {
				iCount = -100;
			}

		}
		return iCount;
	}

	// public DatagramPacket PairAtu() {
	// return UdpClient.PairAtu();
	// }

	public String SendAtuCmd(String cmd) {
		getIp();
		Log.i("IncloudCmd", cmd);
		return UdpClient.SendAtuCmd(cmd);
	}

	// login user ,pwd
	public String LoginData(String user, String pwd) {
		getIp();
		String cmd = "LoginAtu," + aut_ser + "," + user + "," + pwd + ",";
		login_id = UdpClient.getHomeGate(cmd).replace("Login=", "");
		if (login_id.length() > 7)
			login_id = login_id.substring(0, 8);
		if (login_id.equals("0") || login_id.equals("")) {
			return "0";// login failed
		}
		return "1";
	}

	public ArrayList<AreasInfo> getRoomsData() {
		getIp();
		String cmd = heads + aut_ser + "," + login_id + ",getRooms,";
		String str = UdpClient.getHomeGate(cmd);
		if (str.contains("Command=")) {
			str = str.replace("Command=", "");
			// Log.i("getrooms", "cmd:"+cmd+"read:"+str);
			ArrayList<AreasInfo> list = new ArrayList<AreasInfo>();
			String[] strlist = str.split(",");
			if (strlist.length > 1) {
				for (int i = 0; i < strlist.length - 1; i++) {
					String tmp = strlist[i];
					if (tmp != "") {
						AreasInfo info = new AreasInfo();
						info.setId(tmp);
						info.setName(tmp);
						list.add(info);
					}
				}
			}
			return list;
		} else {
			return null;
		}
	}

	// 获取房间下所有设备列表、状态
	public ArrayList<DeviceInfo> getRoomsDeviceData(String roomName) {
		getIp();
		String cmd = heads + aut_ser + "," + login_id + ",rdRoom," + roomName
				+ ",";
		String str = UdpClient.getHomeGate(cmd);
		Log.i("devdata", str);
		devlist = new ArrayList<DeviceInfo>();
		ArrayList<DeviceInfo> list = new ArrayList<DeviceInfo>();
		str = str.replace("Command=", "");
		Log.i("getRoomsDeviceData", str);
		String[] strlist = str.split(";");
		for (int i = 0; i < strlist.length - 1; i++) {
			String tmp = strlist[i];
			if (tmp != "") {
				String[] dev = tmp.split(",");
				DeviceInfo info = new DeviceInfo();
				info.setUiCode("Air");
				info.setDevNum("02");
				info.setId(dev[0]);
				info.setName(dev[0]);
				info.setState(tmp);
				list.add(info);
				devlist.add(info);
			}
		}
		return list;
	}
	
	
	public Integer SetNetworkCommand(String strCommand) {
		getIp();
		Integer iCount = 0;
		String strAck = UdpClient.SendAtuCmd(heads + aut_ser + "," + login_id + ","
				+ strCommand);
		if (strAck != null) {
			if (strAck.contains("Command=")) {
				String strValue[] = strAck.split("=");
				strAck = "0"; // no login
				if (strValue.length >= 2) {
					strAck = strValue[1]; // Command success, this send
											// bytes
					String strRet[] = strAck.split(","); // remove end ","
					strAck = strRet[0];
					try {
						if (isNumeric(strAck)) {
							iCount = Integer.parseInt(strAck); // How many bytes

						} else {
							iCount = strValue.length;

						}
					} catch (Exception ex) {
						iCount = 0;
					}
				}
				if (mInterface != null) {
					mInterface.ReceiveCallback(UdpClient.ip, strValue[1]); // the
																			// Echo
																			// string
				}
			} else if (strAck.contains("Login=0,")) { // "Login=0"
				iCount = -200; // no login
			} else if (strAck.contains("Locked by admin,")) { // Locked by
																// admin,
				iCount = -2;
			} else if (strAck.contains("Command_OK")) {
				iCount = 1;
			} else {
				iCount = -100;
			}

		}
		return iCount;
	}


	public Integer SendInCloudCommand(String strCommand) {
		getIp();
		Integer iCount = 0;
		String strAck = UdpClient.SendAtuCmd(heads + aut_ser + "," + login_id + ","
				+ strCommand);
		if (strAck != null) {
			if (strAck.contains("Command=")) {
				String strValue[] = strAck.split("=");
				strAck = "0"; // no login
				if (strValue.length >= 2) {
					strAck = strValue[1]; // Command success, this send
											// bytes
					String strRet[] = strAck.split(","); // remove end ","
					strAck = strRet[0];
					try {
						if (isNumeric(strAck)) {
							iCount = Integer.parseInt(strAck); // How many bytes

						} else {
							iCount = strValue.length;

						}
					} catch (Exception ex) {
						iCount = 0;
					}
				}
				if (mInterface != null) {
					mInterface.ReceiveCallback(UdpClient.ip, strValue[1]); // the
																			// Echo
																			// string
				}
			} else if (strAck.contains("Login=0,")) { // "Login=0"
				iCount = -200; // no login
			} else if (strAck.contains("Locked by admin,")) { // Locked by
																// admin,
				iCount = -2;
			} else if (strAck.contains("Command_OK")) {
				iCount = 1;
			} else {
				iCount = -100;
			}

		}
		return iCount;
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	@Override
	public void getM3Ip() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ActionData(ArrayList<DeviceInfo> lsit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Sence(String tag, String sId, String revIp) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<DeviceInfo> getM3Data() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String Send(String tag, String code, String value, String revIp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String Read(String tag, String code, String revIp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String ReadState(String tag, String code, String revIp) {
		// TODO Auto-generated method stub
		return null;
	}

}
