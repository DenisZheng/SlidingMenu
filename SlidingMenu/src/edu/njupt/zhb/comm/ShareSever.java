package edu.njupt.zhb.comm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.njupt.zhb.bean.AtuInfo;
import android.content.Context;
import android.support.v4.app.FragmentManager;

public class ShareSever extends Thread {
	private DatagramSocket receive;
	private String ip;
	private int serverPort=18888;
	private int clientPort=19999;
	private Context ctx;
	private DatagramPacket pkg;
	private DatagramPacket messagepkg;
	private FragmentManager manager;
	public ShareSever(String ip, Context ctx,FragmentManager manager) {
		super();
		this.ip = ip;
		this.ctx=ctx;
		this.manager=manager;
		
	}




	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	

	

	public void receive() {
		try {
			// 接收文件监听端口
			LoadingDialogFragment dialog = LoadingDialogFragment.newInstance("接收数据中....");
			dialog.show(manager, "fragmentDialog");
			receive = new DatagramSocket(serverPort);
			// 写文件路径

            String result="";
			// 读取文件包
			byte[] buf = new byte[1024 * 63];
			pkg = new DatagramPacket(buf, buf.length);
			// 发送收到文件后 确认信息包
			byte[] messagebuf = new byte[1024];
			messagebuf = "ok".getBytes();
			messagepkg = new DatagramPacket(messagebuf, messagebuf.length,
					new InetSocketAddress(ip, clientPort));
			// 循环接收包，每接到一个包后回给对方一个确认信息，对方才发下一个包(避免丢包和乱序)，直到收到一个结束包后跳出循环，结束文件传输，关闭流
			while (true) {
				receive.receive(pkg);
				System.out.println("rev: "+EncodingUtils.getString(pkg.getData(), "GBK")
						.toString().trim());
				if (new String(pkg.getData(), 0, pkg.getLength()).equals("end")) {
					System.out.println("文件接收完毕");
					//bos.close();
					receive.close();
					break;
				}
				receive.send(messagepkg);
				//System.out.println(new String(messagepkg.getData()));
				result=result+EncodingUtils.getString(pkg.getData(), "GBK")
						.toString().trim();
				
			}
			System.out.println("result: "+result.trim());
			Gson gson = new Gson();
			List<AtuInfo> atus = gson.fromJson(result.trim(), new TypeToken<List<AtuInfo>>(){}.getType());
			Config.saveArray(ctx, Config.ATUS, atus);
			receive.close();
			dialog.dismiss();
		} catch (SocketException e) {
			receive.close();
			e.printStackTrace();
		} catch (IOException e) {
			receive.close();
			e.printStackTrace();
		}
	}

	public void run() {
		receive();
	}
}
