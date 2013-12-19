package com.snowd.android.jimi.common;

/**
 * Created by xuelong.wenxl on 13-12-19.
 */
public class Log {

    private static boolean print = false;

    public static void turnOn() {
        print = true;
    }

    public static void turnOff() {
        print = false;
    }

    public static void d(String tag, Object args) {
        if (print) android.util.Log.d(tag, String.valueOf(args));
    }
    public static void v(String tag, Object args) {
        if (print) android.util.Log.v(tag, String.valueOf(args));
    }
    public static void w(String tag, Object args) {
        if (print) android.util.Log.w(tag, String.valueOf(args));
    }
    public static void i(String tag, Object args) {
        if (print) android.util.Log.i(tag, String.valueOf(args));
    }
    public static void e(String tag, Object args) {
        if (print) android.util.Log.e(tag, String.valueOf(args));
    }
}
