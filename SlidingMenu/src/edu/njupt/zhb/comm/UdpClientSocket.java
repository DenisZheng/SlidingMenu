package edu.njupt.zhb.comm;


import java.net.*;

/**
 * @˵�� UDP�ͻ��˳������ڶԷ���˷�����ݣ������շ���˵Ļ�Ӧ��Ϣ
 * @author cuisuqiang
 * @version 1.0
 */
public class UdpClientSocket {
	/**
	 * ���Ӷ���
	 */
	private static DatagramSocket ds = null;
	/**
	 * ��ַ����
	 */
	private static SocketAddress address = null;
	private static String ip = "255.255.255.255";// "172.16.2.204";
	private static int webPort = 5999;
	/**
	 * ���Կͻ��˷���ͽ��ջ�Ӧ��Ϣ�ķ���
	 */
	public static void main(String[] args) throws Exception {
		init();
		while(true){
			UdpClientSocket.send(address,"��ã��װ���!".getBytes());
			UdpClientSocket.receive();
			try {
				Thread.sleep(3 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * �����Ӻ͵�ַ��ʼ��
	 */
	public static void init(){
		try {
			ds = new DatagramSocket(webPort); // ����ض˿���Ϊ�ͻ���
			ds.setSoTimeout(2 * 1000);
			address = new InetSocketAddress(ip,webPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ָ���ķ���˷��������Ϣ
	 */
	public static void send(SocketAddress address,byte[] bytes){
		try {
			DatagramPacket dp = new DatagramPacket(bytes, bytes.length, address);
			ds.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���մ�ָ���ķ���˷��ص����
	 */
	public static void receive(){
		try {
			byte[] buffer = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			ds.receive(dp);		
			byte[] data = new byte[dp.getLength()];
			System.arraycopy(dp.getData(), 0, data, 0, dp.getLength());	
			System.out.println("����˻�Ӧ��ݣ�" + new String(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

