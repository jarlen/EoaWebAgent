package cn.jarlen.richcommon.jwebview.util;

import android.content.Context;

public class ContextUtils {
    public static Context contextInstance;
    public static Context getContext() {
        return contextInstance;
    }

    public static void setContext(Context context) {
        contextInstance = context.getApplicationContext();
    }
}
