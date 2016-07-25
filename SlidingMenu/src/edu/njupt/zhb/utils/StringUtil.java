package edu.njupt.zhb.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.util.Log;

public class StringUtil {
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	public static void AssetsToData(String PName, Context context) {
		String path = context.getApplicationContext().getFilesDir()
				.getAbsolutePath().replace("files", "")
				+ "databases/" + PName; // data/data目录
		File file = new File(path);
		try {
			File dir = file.getParentFile();
			if (dir.exists() == false) {
				dir.mkdirs();
			}
			// ��contentprovider��ɵ�dbɾ��
			if (file.exists()) {
				file.delete();
			}

			InputStream in = context.getAssets().open(PName); // 从assets目录下复�?
			FileOutputStream out = new FileOutputStream(file);
			int length = -1;
			byte[] buf = new byte[8192];
			while ((length = in.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
			out.flush();
			in.close();
			out.close();
		} catch (Exception ex) {

			Log.i("file error", ex.toString());
		}
	}

	public static void DelData(String PName, Context context) {
		String path = context.getApplicationContext().getFilesDir()
				.getAbsolutePath().replace("files", "")
				+ "databases/" + PName; // data/data目录
		try {
			DelFile(PName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String inputStream2String(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	public static String getFileData(String pathFile) throws IOException {
		String res = new String();
		File filex = new File(pathFile);
		FileInputStream fis = new FileInputStream(filex);
		int length = fis.available();
		byte[] buffer = new byte[length];
		fis.read(buffer);
		res = EncodingUtils.getString(buffer, "UTF-8");
		fis.close();

		return res;// String2InputStream(res);
	}

	public static String getCurrentTime(long date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = format.format(new Date(date));
		return str;

	}

	public static boolean checkFilePath(String filePath) throws IOException {
		File saveFile = new File(filePath);
		if (saveFile.isFile())
			return true;
		else {
			return false;
		}

	}

	public static void DelFile(String filePath) throws IOException {
		if (checkFilePath(filePath)) {
			File file = new File(filePath);
			file.delete();
		}
	}
}
