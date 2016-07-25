package edu.njupt.zhb.comm;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import com.google.gson.Gson;

import edu.njupt.zhb.bean.AtuInfo;
import android.content.Context;
import android.support.v4.app.FragmentManager;

public class ShareClient extends Thread {

	private DatagramSocket send;
	private DatagramPacket pkg;
	private DatagramPacket messagepkg;
	private int serverPort=18888;
	private int clientPort=19999;
	private String ip;
	private Context ctx;
	private FragmentManager manager;
	public ShareClient(String ip,Context ctx,FragmentManager manager) {
		super();
		this.ctx = ctx;
		this.ip = ip;
		this.manager=manager;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void send() {
		try {
			LoadingDialogFragment dialog = LoadingDialogFragment.newInstance("共享数据中....");
			dialog.show(manager, "fragmentDialog");
			// 文件发送者设置监听端口
			send = new DatagramSocket(clientPort);

			Gson gson = new Gson();
			List<AtuInfo> atus = Config.loadArray(ctx, Config.ATUS);
			String result = gson.toJson(atus);

			BufferedInputStream bis = new BufferedInputStream(
					new ByteArrayInputStream(EncodingUtils.getBytes(result,
							"GBK")));

			// 确认信息包
			// 确认信息包
			byte[] messagebuf = new byte[1024];
			messagepkg = new DatagramPacket(messagebuf, messagebuf.length);
			// 文件包
			byte[] buf = new byte[1024 * 63];
			int len;
			while ((len = bis.read(buf)) != -1) {

				pkg = new DatagramPacket(buf, len, new InetSocketAddress(ip,
						serverPort));
				// 设置确认信息接收时间，3秒后未收到对方确认信息，则重新发送一次
				send.setSoTimeout(3000);
				while (true) {
					send.send(pkg);
					send.receive(messagepkg);
					System.out.println(new String(messagepkg.getData()));
					break;
				}
			}
			// 文件传完后，发送一个结束包
			buf = "end".getBytes();
			DatagramPacket endpkg = new DatagramPacket(buf, buf.length,
					new InetSocketAddress(ip, serverPort));
			System.out.println("文件发送完毕");
			send.send(endpkg);
			bis.close();
			send.close();
			dialog.dismiss();
		} catch (SocketException e) {
			send.close();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			send.close();
			e.printStackTrace();
		}
	}

	public void run() {
		send();
	}
}
