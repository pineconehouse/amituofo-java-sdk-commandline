package com.amituofo.cmdlet.util;

import java.util.List;
import java.util.Map;

import com.amituofo.cmdlet.core.ArgumentParseException;
import com.amituofo.cmdlet.core.Arguments;

public class ValidUtils {

	public static void exceptionWhenArgumentNotFound(Arguments args, String argName, String msg) throws ArgumentParseException {
		if (!args.hasArgument(argName)) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenNull(Object o, String msg) throws ArgumentParseException {
		if (o == null) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenNotNull(Object o, String msg) throws ArgumentParseException {
		if (o != null) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenTrue(boolean v, String msg) throws ArgumentParseException {
		if (v) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenFalse(boolean v, String msg) throws ArgumentParseException {
		if (!v) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenEmpty(String o, String msg) throws ArgumentParseException {
		if (o == null || o.length() == 0) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenEmpty(String[] o, String msg) throws ArgumentParseException {
		if (o == null || o.length == 0) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenEmpty(List os, String msg) throws ArgumentParseException {
		if (os == null || os.size() == 0) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenEmpty(Map<?, ?> map, String msg) throws ArgumentParseException {
		if (map == null || map.size() == 0) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenNotEmpty(String o, String msg) throws ArgumentParseException {
		if (o != null || o.length() != 0) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenContains(String o, String[] contains, String msg) throws ArgumentParseException {
		if (o != null && o.length() >= 0 && contains != null) {
			for (String string : contains) {
				if (o.contains(string)) {
					throw new ArgumentParseException(msg);
				}
			}
		}
	}

	public static void exceptionWhenContainsChar(String o, String chars, String msg) throws ArgumentParseException {
		if (o != null && o.length() >= 0 && chars != null) {
			char[] charArrays = chars.toCharArray();
			for (char c : charArrays) {
				if (o.contains(String.valueOf(c))) {
					throw new ArgumentParseException(msg);
				}
			}
		}
	}

	public static void exceptionWhenZero(int d, String msg) throws ArgumentParseException {
		if (d == 0) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenZero(long d, String msg) throws ArgumentParseException {
		if (d == 0) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenLassThanZero(int d, String msg) throws ArgumentParseException {
		if (d < 0) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenLassThan(long d, final long threshold, String msg) throws ArgumentParseException {
		if (d < threshold) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenGreaterThan(long d, final long threshold, String msg) throws ArgumentParseException {
		if (d > threshold) {
			throw new ArgumentParseException(msg);
		}
	}

	public static void exceptionWhenLessThanOne(long d, String msg) throws ArgumentParseException {
		exceptionWhenLassThan(d, 1, msg);
	}
}
