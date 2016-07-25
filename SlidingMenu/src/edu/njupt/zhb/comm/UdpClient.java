package edu.njupt.zhb.comm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

import android.util.Log;

public class UdpClient {

	static String ByteToHexString(byte[] raw, int start, int len) {
		String HEXES = "0123456789ABCDEF";
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (int i = start; i < start + len; i++) {
			byte b = raw[i];
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
					HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	// static DatagramSocket _client;
	static String ip = "192.168.10.102";// "172.16.2.204";
	static int webPort = 9559;
	static int operPort = 9559;
	static int lens = 2048;
	static int sendlens = 22;

	// sendHomeGate
	static String getHomeGate(String cmd) {

		byte[] rs = Receive(ip, webPort, lens,
				EncodingUtils.getBytes(cmd, "GBK"));
		return new String(EncodingUtils.getString(rs, "GBK")).replace(" ", "");
	}

	static DatagramPacket FindAtu(String svrip, int Port) {
		byte[] sendBuf = EncodingUtils.getBytes("FindAtu,", "GBK");
		//byte[] sendBuf = "FindAtu,".getBytes();
		byte[] buf = new byte[lens];
		DatagramSocket client = null;
		try {
			client = new DatagramSocket();
			// _client.setReuseAddress(true);
			client.setBroadcast(true);// ���ñ�־��T OR E OR R�㲥��ݱ�

			DatagramPacket sendDp = new DatagramPacket(sendBuf, sendBuf.length,
					InetAddress.getByName(svrip), Port);
			client.setSoTimeout(2000);
			client.send(sendDp);
			DatagramPacket receiveDp = new DatagramPacket(buf, lens);
			Thread.sleep(500);
			client.receive(receiveDp);
			String data = EncodingUtils.getString(receiveDp.getData(), "GBK")
					.toString().trim();
			Log.i("FindAtu", "FindAtu; data: " + data + "  svrip:"
					+ receiveDp.getAddress().getHostAddress());
			client.close();
			client = null;
			return receiveDp;
		} catch (Exception e) {
			Log.i("FindAtu", "ERROR.......IP:" + svrip);
			e.printStackTrace();
		}
		client.close();
		client = null;
		return null;
	}
	
	static String FindSingleAtu(String mac,String svrip, int Port,AtuUdpInterface atuInterface) {
		byte[] sendBuf = EncodingUtils.getBytes("FindAtu,"+mac+",", "GBK");
		//byte[] sendBuf = "FindAtu,".getBytes();
		byte[] buf = new byte[lens];
		DatagramSocket client = null;
		try {
			client = new DatagramSocket();
			// _client.setReuseAddress(true);
			client.setBroadcast(true);// ���ñ�־��T OR E OR R�㲥��ݱ�

			DatagramPacket sendDp = new DatagramPacket(sendBuf, sendBuf.length,
					InetAddress.getByName(svrip), Port);
			client.setSoTimeout(5000);
			client.send(sendDp);
			DatagramPacket receiveDp = new DatagramPacket(buf, lens);
			Thread.sleep(500);
			client.receive(receiveDp);
			String data = EncodingUtils.getString(receiveDp.getData(), "GBK")
					.toString().trim();
			
			Log.i("FindSingleAtu", "FindSingleAtu: data: " + data + "  svrip:"
					+ receiveDp.getAddress().getHostAddress());
			if (data.contains("AtuServer=")) {
				String atuId = data.replace("AtuServer=", "")
						.replace(",", "").replace(" ", "").substring(0, 12);
				if (atuInterface != null) {
					atuInterface.ReceiveCallback(receiveDp.getAddress().getHostAddress(), atuId); // the
				}
			}
			client.close();
			client = null;
			return data;
		} catch (Exception e) {
			Log.i("FindSingleAtu", "ERROR.."+e.toString()+".....IP:" + svrip);
			e.printStackTrace();
		}
		client.close();
		client = null;
		return null;
	}

	static String SendAtuCmd(String cmd) {
		Log.i("IncloudCmd", cmd);
		byte[] sendBuf = EncodingUtils.getBytes(cmd, "GBK");
		byte[] buf = new byte[lens];
		DatagramSocket _client = null;
		try {
			_client = new DatagramSocket();
			// if (_client == null) {

			// _client.setReuseAddress(true);
			// _client.setBroadcast(true);
			// ��2.2���Ұ汾���С���4.0�汾�Ͽ���
			// _client.bind(new InetSocketAddress(operPort));
			// }
			DatagramPacket sendDp = new DatagramPacket(sendBuf, sendBuf.length,
					InetAddress.getByName(ip), operPort);
			_client.setSoTimeout(2000);
			_client.send(sendDp);
			DatagramPacket receiveDp = new DatagramPacket(buf, lens);
			Thread.sleep(200);
			_client.receive(receiveDp);
			String data = EncodingUtils.getString(receiveDp.getData(), "GBK")
					.toString().trim();
			Log.i("Incloud", "Packet received; data: " + data + "  ip:" + ip);
			_client.close();
			_client = null;
			return data;
		} catch (Exception e) {
			Log.i("Incloud", "ERROR.......IP:" + ip);
			e.printStackTrace();
		}
		_client.close();
		_client = null;
		return null;
	}

	static String sendHomeGate(byte[] cmd, String revIp) {
		// return "";
		byte[] rs = Receive(revIp, operPort, cmd.length, cmd);
		return new String(rs).replace(" ", "");
	}

	static void sendToHomeGate(byte[] cmd, String revIp) {
		// return "";
		// byte[] rs= Receive(revIp, operPort,22, cmd);
		// return new String(rs);
		SendTo(revIp, operPort, cmd, sendlens);
	}

	private static byte[] Receive(String revip, int port, int recLen,
			byte[] sendBuf) {
		byte[] buf = new byte[recLen];
		DatagramSocket _client = null;
		try {
			if (_client == null) {
				_client = new DatagramSocket();
				// _client.setReuseAddress(true);
				_client.setBroadcast(true);// ���ñ�־��T OR E OR R�㲥��ݱ�
											// ��2.2���Ұ汾���С���4.0�汾�Ͽ���
				// _client.bind(new InetSocketAddress(port));
			}
			DatagramPacket sendDp = new DatagramPacket(sendBuf, sendBuf.length,
					InetAddress.getByName(revip), port);
			_client.setSoTimeout(2000);
			_client.send(sendDp);
			DatagramPacket receiveDp = new DatagramPacket(buf, recLen);
			Thread.sleep(200);
			_client.receive(receiveDp);
			// BusProvider.get().post(new PacketEvent(receiveDp));
			// String data = new String(EncodingUtils.getString(buf,
			// "GBK")).trim();
			// Log.i("Incloud", "Packet received; data: " +data);
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		_client.close();
		_client = null;
		return new byte[0];
	}

	public static void SendTo(String ip, int port, byte[] sendBuf, int len) {
		DatagramSocket _client = null;
		try {
			if (_client == null) {
				_client = new DatagramSocket(null);
				_client.setReuseAddress(true);
				_client.setBroadcast(true);// ���ñ�־��T OR E OR R�㲥��ݱ�
											// ��2.2���Ұ汾���С���4.0�汾�Ͽ���
				_client.bind(new InetSocketAddress(webPort));
			}
			InetAddress address = InetAddress.getByName(ip);
			DatagramPacket sendpacket = new DatagramPacket(sendBuf, len,
					address, port);
			_client.setSoTimeout(2000);
			_client.send(sendpacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getHostIp(String userName, String passWord) {
		String cmd = "getHostIp&" + userName + "&" + passWord;
		Log.i("send hostIp", cmd);
		Log.i("send port", Integer.toString(webPort));
		byte[] rs = Receive(ip, webPort, 20, cmd.getBytes());
		if (rs.length == 0)
			return null;
		return new String(rs);
	}

}
