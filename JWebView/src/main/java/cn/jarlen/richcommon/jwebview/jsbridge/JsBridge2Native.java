package cn.jarlen.richcommon.jwebview.jsbridge;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import cn.jarlen.richcommon.jwebview.client.IWebView;
import cn.jarlen.richcommon.jwebview.entity.Js2NativeBean;
import cn.jarlen.richcommon.jwebview.entity.NativeCall2JsBean;


/**
 * @author jarlen
 * @date 2016/4/10
 */
public class JsBridge2Native {
    public static final String BRIDGE_NAME = "JBridge";
    private final FragmentActivity currentActivity;
    private final WeakReference<IWebView> webViewWeakReference;
    private Gson gson = new Gson();

    public JsBridge2Native(FragmentActivity context, IWebView iWebView) {
        this.currentActivity = context;
        this.webViewWeakReference = new WeakReference<>(iWebView);
    }

    @JavascriptInterface
    public void callNative(final String param) {
        HandlerThreadExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                IWebView iWebView = webViewWeakReference.get();
                if (iWebView == null) {
                    return;
                }
                if (TextUtils.isEmpty(param)) {
                    iWebView.callJs(NativeCall2JsBean.createError(NativeCall2JsBean.ErrorCode.ERROR_INVALID_PARAM, ""));
                    return;
                }
                Js2NativeBean paramBean = gson.fromJson(param, Js2NativeBean.class);
                if (paramBean == null || TextUtils.isEmpty(paramBean.getMethod())) {
                    /*参数传入错误*/
                    iWebView.callJs(NativeCall2JsBean.createError(NativeCall2JsBean.ErrorCode.ERROR_INVALID_PARAM, ""));
                    return;
                }
                /*查找到对应方法的处理器*/
                JSHandler jsHandler = JSHandlerFactory.getJSHandler(paramBean.getMethod());
                if (jsHandler == null) {
                    iWebView.callJs(NativeCall2JsBean.createError(NativeCall2JsBean.ErrorCode.ERROR_NOT_SUPPORT_METHOD, paramBean.getMethod()));
                    return;
                }
                jsHandler.handle(currentActivity, iWebView, paramBean);
            }
        });
    }
}
