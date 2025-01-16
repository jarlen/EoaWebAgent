package cn.jarlen.richcommon.jwebview.client;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.WebView;

import java.util.Map;
import java.util.Set;

public class AbsJsInterfaceHolderImpl extends AbsJsInterfaceHolder {

    private static final String TAG = AbsJsInterfaceHolderImpl.class.getSimpleName();
    private final WebView mWebView;

    public AbsJsInterfaceHolderImpl(WebView mWebView) {
        this.mWebView = mWebView;
    }

    @Override
    public JsInterfaceHolder addJavaObjects(Map<String, Object> maps) {
        Set<Map.Entry<String, Object>> sets = maps.entrySet();
        for (Map.Entry<String, Object> mEntry : sets) {
            Object v = mEntry.getValue();
            boolean t = checkObject(v);
            if (!t) {
                Log.e(TAG, v.getClass().getSimpleName() + " has not offer method javascript to call ,please check addJavascriptInterface annotation was be added ");
            } else {
                addJavaObjectDirect(mEntry.getKey(), v);
            }
        }
        return this;
    }

    @Override
    public JsInterfaceHolder addJavaObject(String k, Object v) {
        boolean t = checkObject(v);
        if (!t) {
            Log.e(TAG, v.getClass().getSimpleName() + " has not offer method javascript to call ,please check addJavascriptInterface annotation was be added ");
        } else {
            addJavaObjectDirect(k, v);
        }
        return this;
    }

    @SuppressLint("JavascriptInterface")
    private JsInterfaceHolder addJavaObjectDirect(String k, Object v) {
        this.mWebView.addJavascriptInterface(v, k);
        return this;
    }
}
