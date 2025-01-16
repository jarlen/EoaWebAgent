package cn.jarlen.richcommon.jwebview.jsbridge;

import android.webkit.JavascriptInterface;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

import cn.jarlen.richcommon.jwebview.client.IWebView;


/**
 * @author Administrator
 * @date 2016/4/10
 */
public class JsBridge2Native {
    public static final String BRIDGE_NAME = "JBridge";
    private static final String JS_METHOD_CONFIG = "config";
    private static final String JS_METHOD_INIT_CONFIG = "initConfig";
    private final FragmentActivity currentActivity;
    private final WeakReference<IWebView> webViewWeakReference;

    public JsBridge2Native(FragmentActivity context, IWebView iWebView) {
        this.currentActivity = context;
        this.webViewWeakReference = new WeakReference<>(iWebView);
    }

    @JavascriptInterface
    public void callNative(final String param) {

    }
}
