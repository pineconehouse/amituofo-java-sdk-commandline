package com.amituofo.cmdlet.common;

import java.io.File;

public class Utils {
	public static boolean isEmpty(String value) {
		if (value != null) {
			return value.length() == 0;
		}
		return true;
	}

	public static boolean isNotEmpty(String value) {
		if (value != null) {
			return value.length() != 0;
		}
		return false;
	}

	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isFileExist(String path) {
		if ((path == null) || (path.length() == 0)) {
			return false;
		}
		try {
			return new File(path).exists();
		} catch (Exception e) {
		}
		return false;
	}
}
