package edu.njupt.zhb.comm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.njupt.zhb.utils.CustomToast;
import android.content.*;
import android.util.Base64;

public class Config {
	public static String ATUS = "AUTS";
	private static String[] PWDSTR = { "i", "n", "C", "l", "o", "u", "d", "P",
			"a", "s", "w", "r" };
	public static String GETPASSCODE = "passcode";
	public static String GETGENCODE = "getcode";
	private static String cookie = "";
	public static int LAN_CTR_MODE = 0;
	public static int REMO_CTR_MODE = 1;
	public static int SLOW_CTR_MODE = 2;
	public static int ERR_CTR_MODE = -1;

	public static boolean saveArray(Context mContext, String listName, List sKey) {
		SharedPreferences sp = mContext.getSharedPreferences(listName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor mEdit1 = sp.edit();
		try {
			String liststr = List2String(sKey);
			mEdit1.putString(listName, liststr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mEdit1.commit();
	}

	public static List loadArray(Context mContext, String listName) {
		SharedPreferences sp = mContext.getSharedPreferences(listName,
				Context.MODE_PRIVATE);
		List<Object> sKey = new ArrayList<Object>();
		// sKey.clear();
		String liststr = sp.getString(listName, "");
		try {
			sKey = String2List(liststr);
			return sKey;
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sKey;
	}

	public static String List2String(List list) throws IOException {
		// 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 然后将得到的字符数据装载到ObjectOutputStream
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		// writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
		objectOutputStream.writeObject(list);
		// 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
		String SceneListString = new String(Base64.encode(
				byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
		// 关闭objectOutputStream
		objectOutputStream.close();
		return SceneListString;
	}

	@SuppressWarnings("unchecked")
	public static List String2List(String SceneListString)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
				Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				mobileBytes);
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		List list = (List) objectInputStream.readObject();
		objectInputStream.close();
		return list;
	}

	private static String GetIdCode(String passcode) {
		String incode = "";
		for (int i = 0; i < passcode.length(); i++) {
			String num = passcode.substring(i, i + 1);
			incode += PWDSTR[Integer.parseInt(num)];
		}
		System.out.println("incode:=====" + incode);
		return incode;
	}

	public static String GetPassCode() {
		try {
			String uri = "http://www.cseej.com/passcode.php";
			String result = "";
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(5 * 1000);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("请求url失败");
			}
			InputStream is = conn.getInputStream();
			try {
				result = readData(is, "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String setCookie = conn.getHeaderField("Set-Cookie");
			cookie = setCookie.substring(0, setCookie.indexOf(";"));

			System.out.println("passcode:=====" + result);
			return GetIdCode(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static int GetGenCode(String moblienum, String idcode) {
		try {
			String uri = "http://www.cseej.com/gen_code.php?mobilenumber="
					+ moblienum + "&id_code=" + idcode + "";
			String result = "";
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(5 * 1000);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("请求url失败");
			}
			InputStream is = conn.getInputStream();
			try {
				result = readData(is, "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("revStr:=====" + result);
			int regcode = Integer.parseInt(result);
			return regcode;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1000;
	}

	public static int RegUser(String moblienum, String passwd, String regcode) {
		try {

			String uri = "http://www.cseej.com/reg.php";
			String result = "";
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(5 * 1000);
			PrintWriter pw = new PrintWriter(conn.getOutputStream());
			pw.print("mobilenumber=" + moblienum + "&pass1=" + passwd
					+ "&pass2=" + passwd + "&reg_code=" + regcode + "");
			pw.flush();
			pw.close();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("请求url失败");
			}
			InputStream is = conn.getInputStream();
			try {
				result = readData(is, "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result.contains("Register Success!")) {
				return 102;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -103;
	}

	public static String RemLogin(String mobilenum, String passwd, String serial) {
		try {
			String uri = "http://www.cseej.com/login.php";
			String result = "";
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			// conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(5 * 1000);
			PrintWriter pw = new PrintWriter(conn.getOutputStream());
			pw.print("name=" + mobilenum + "&password=" + passwd + "&serial="
					+ serial);
			pw.flush();
			pw.close();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("请求url失败");
			}
			InputStream is = conn.getInputStream();
			try {
				result = readData(is, "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("RemLogin:=====" + result);
			if (!result.contains("Error:")) {
				result = result.replace("<br>", "");
				System.out.println("RemLogin:=====" + result);
			}
			// System.out.println("RemLogin:=====" + result);
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static int RemControl(String mobilenum, String passwd,
			String serial, String airpass, String cmd,
			AtuUdpInterface atuInterface) {
		try {
			String uri = "http://www.cseej.com/login.php";
			String result = "";
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(10 * 1000);
			PrintWriter pw = new PrintWriter(conn.getOutputStream());
			// String params="";
			pw.print("name=" + mobilenum + "&password=" + passwd + "&serial="
					+ serial + "&airpass=" + airpass + "&cmd=" + cmd);
			pw.flush();
			pw.close();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("请求url失败");
			}
			InputStream is = conn.getInputStream();
			try {
				result = readData(is, "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("RemLogin:=====" + result);
			if (!result.contains("Error:")) {
				result = result.replace("<br>", "");
				System.out.println("RemControl:=====" + result);
				if (atuInterface != null) {
					atuInterface.ReceiveCallback(uri, result); // the
				}
				return result.length();
			}
			// System.out.println("RemLogin:=====" + result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public static int ChangPwd(String mobilenum, String passwd, String newpass) {
		try {
			String uri = "http://www.cseej.com/changepass.php";
			String result = "";
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			// conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(5 * 1000);
			PrintWriter pw = new PrintWriter(conn.getOutputStream());
			pw.print("name=" + mobilenum + "&password=" + passwd + "&newpass1="
					+ newpass + "&newpass2=" + newpass);
			pw.flush();
			pw.close();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("请求url失败");
			}
			InputStream is = conn.getInputStream();
			try {
				result = readData(is, "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result.contains("Success")) {
				return 105;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -105;
	}

	public static String readData(InputStream inSream, String charsetName)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inSream.close();
		return new String(data, charsetName);
	}

	public static int getUdpPort(Boolean isRemo, int RemoPort) {
		if (isRemo) {
			return RemoPort;
		}
		return 9559;
	}
}
