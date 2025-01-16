package cn.jarlen.richcommon.jwebview.util;

public class ThreadUtils {

    public static String getThreadPrint() {
        Thread thread = Thread.currentThread();
        return thread.toString();
    }

    public static boolean isMainThread() {
        return Thread.currentThread().getName().equals("main");
    }
}
