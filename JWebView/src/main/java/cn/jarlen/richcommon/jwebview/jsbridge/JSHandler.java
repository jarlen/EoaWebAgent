package cn.jarlen.richcommon.jwebview.jsbridge;

import android.app.Activity;

import cn.jarlen.richcommon.jwebview.client.IWebView;
import cn.jarlen.richcommon.jwebview.entity.Js2NativeBean;

public abstract class JSHandler {

    public abstract void handle(Activity context, IWebView iWebView, Js2NativeBean param);

    public boolean needSign() {
        return false;
    }
}
