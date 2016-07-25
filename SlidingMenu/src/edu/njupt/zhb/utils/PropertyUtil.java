package edu.njupt.zhb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertyUtil {
	public static Properties loadConfig(String file) {
		Properties properties = new Properties();
		try {
			FileInputStream s = new FileInputStream(file);
			properties.load(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static void saveConfig(String file, Properties properties) {
		try {
			File f = new File(file);
			if (f.exists() == true) {
				FileInputStream fileInputStream_temp = new FileInputStream(file);
				Properties properties_temp = new Properties();
				properties_temp.load(fileInputStream_temp);
				if (properties != null && properties.size() > 0) {
					properties_temp.putAll(properties);
				}
				FileOutputStream s = new FileOutputStream(file, false);
				properties_temp.store(s, "");
			} else {
				FileOutputStream s = new FileOutputStream(file, false);
				properties.store(s, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
