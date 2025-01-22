package cn.jarlen.richcommon.jwebview.client;

import android.net.Uri;
import android.util.ArrayMap;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.List;
import java.util.Map;

import cn.jarlen.richcommon.jwebview.entity.NativeCall2JsBean;
import cn.jarlen.richcommon.jwebview.entity.location.GpsLocationResult;
import cn.jarlen.richcommon.jwebview.entity.toolbar.ChangeToolbarParams;
import cn.jarlen.richcommon.jwebview.entity.toolbar.MenuItemBean;
import cn.jarlen.richcommon.jwebview.lifecycle.WebViewLifecycleCallbacks;

public interface IWebView<D> {

    /**
     * 调用网页上的js方法
     *
     * @param resp
     */
    void callJs(NativeCall2JsBean resp);

    /**
     * 添加事件监听，支持“back”，“menuclick”,“onPause”,“onResume”，“receivePushMsg”
     *
     * @param event
     * @return
     */
    boolean addEventListener(String event);

    WebView getWebView();

    /**
     * 推送数据接收接口
     *
     * @param pushMsg
     */
    void onReceivePushMsg(String pushMsg);

    void onResume();

    void onPause();

    void onDestroy();

    void registerLifecycleCallbacks(WebViewLifecycleCallbacks lifecycle);

    void unRegisterLifecycleCallbacks(WebViewLifecycleCallbacks lifecycle);

    void onPageStarted(WebView view, String url);

    void onPageFinished(WebView view, String url);

    void showLoading(boolean showLoading, boolean canCancel);

    void onProgress(WebView view, int newProgress);

    /**
     * 加载新页面时，清空上一个页面的设置，包括菜单，事件监听，Activity返回回调等
     * 甚至当前页面能够调用的本地方法的列表等。
     */
    void clear();

    D getHyBirdData();

    void loadWebViewUrl(String url, Map<String, String> urlHeader);

    boolean back();

    void onMainFrameError(WebView view, int errorCode, String description, String failingUrl);

    void onShowMainFrame();

    ArrayMap<String, String> getConfigData();

    String getCurrentUrl();

    /**
     * 设置toolbar标题
     *
     * @param title
     */
    void setTitle(String title);

    void setHostAddress(Map<String, String> hostMap);

    String getHostAddress(String hostKey);

    boolean onClientJsAlert(String url, String message, final JsResult result);

    boolean onClientJsConfirm(String url, String message, final JsResult result);

    boolean onClientJsPrompt(String url, String message, String defaultValue, final JsPromptResult result);

    boolean onClientShowFileChooser(ValueCallback<Uri[]> valueCallbacks, WebChromeClient.FileChooserParams fileChooserParams);
}
