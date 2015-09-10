package com.tu.preferences;

import android.util.Log;

import com.tu.preference.BuildConfig;


/**
 * 日志工具类
 * 
 * @author Altas
 * @date 2014年9月23日
 */
public final class LogUtils {
	public static int LEVEL = 3;
	public static String customTagPrefix = "";

	private static boolean allowPrint(int level) {
		return level >= LEVEL;
	}
	private static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}
	private static String generateTag() {
		String tag = "%s.%s(L:%d)";
		StackTraceElement caller = getCallerStackTraceElement();
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
		tag = String.format(tag, new Object[] { callerClazzName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber()) });
		tag = customTagPrefix + ":" + tag;
		return tag;
	}

	public static void v(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.v(tag, msg, tr);
	}

	public static void v(String tag, String format, Object[] args) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.v(tag, String.format(format, args));
	}

	public static void v(String tag, Throwable tr, String format, Object[] args) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.v(tag, String.format(format, args), tr);
	}

	public static void v(String msg) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.v(generateTag(), msg);
	}

	public static void v(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.v(generateTag(), msg, tr);
	}

	public static void d(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.d(tag, msg, tr);
	}

	public static void d(String tag, String format, Object[] args) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.d(tag, String.format(format, args));
	}

	public static void d(String tag, Throwable tr, String format, Object[] args) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.d(tag, String.format(format, args), tr);
	}

	public static void d(String msg) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.d(generateTag(), msg);
	}

	public static void d(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.d(generateTag(), msg, tr);
	}

	public static void i(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.i(tag, msg);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.i(tag, msg, tr);
	}

	public static void i(String tag, String format, Object[] args) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.i(tag, String.format(format, args));
	}

	public static void i(String tag, Throwable tr, String format, Object[] args) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.i(tag, String.format(format, args), tr);
	}

	public static void i(String msg) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.i(generateTag(), msg);
	}

	public static void i(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.i(generateTag(), msg, tr);
	}

	public static void w(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.w(tag, msg);
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.w(tag, msg, tr);
	}

	public static void w(String tag, String format, Object[] args) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.w(tag, String.format(format, args));
	}

	public static void w(String tag, Throwable tr, String format, Object[] args) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.w(tag, String.format(format, args), tr);
	}

	public static void w(String msg) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.w(generateTag(), msg);
	}

	public static void w(Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.w(generateTag(), tr);
	}

	public static void w(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		Log.w(generateTag(), msg, tr);
	}

	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable tr) {
		Log.e(tag, msg, tr);
	}

	public static void e(String tag, String format, Object[] args) {
		Log.e(tag, String.format(format, args));
	}

	public static void e(String tag, Throwable tr, String format, Object[] args) {
		Log.e(tag, String.format(format, args), tr);
	}

	public static void e(String msg) {
		Log.e(generateTag(), msg);
	}

	public static void e(String msg, Throwable tr) {
		Log.e(generateTag(), msg, tr);
	}

	public static void wtf(String tag, String msg) {
		if (!allowPrint(7)) {
			return;
		}
		Log.wtf(tag, msg);
	}

	public static void wtf(String tag, String msg, Throwable tr) {
		if (!allowPrint(7)) {
			return;
		}
		Log.wtf(tag, msg, tr);
	}

	public static void wtf(String tag, String format, Object[] args) {
		if (!allowPrint(7)) {
			return;
		}
		Log.wtf(tag, String.format(format, args));
	}

	public static void wtf(String tag, Throwable tr, String format, Object[] args) {
		if (!allowPrint(7)) {
			return;
		}
		Log.wtf(tag, String.format(format, args), tr);
	}

	public static void wtf(String msg) {
		if (!allowPrint(7)) {
			return;
		}
		Log.wtf(generateTag(), msg);
	}

	public static void wtf(String msg, Throwable tr) {
		if (!allowPrint(7)) {
			return;
		}
		Log.wtf(generateTag(), msg, tr);
	}
}