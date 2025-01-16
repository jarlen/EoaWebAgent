package cn.jarlen.richcommon.jwebview;

import android.app.Activity;
import android.util.ArrayMap;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jarlen.richcommon.jwebview.client.AbsJsInterfaceHolderImpl;
import cn.jarlen.richcommon.jwebview.client.IWebView;
import cn.jarlen.richcommon.jwebview.client.JsInterfaceHolder;
import cn.jarlen.richcommon.jwebview.entity.NativeCall2JsBean;
import cn.jarlen.richcommon.jwebview.entity.location.GpsLocationResult;
import cn.jarlen.richcommon.jwebview.entity.toolbar.ChangeToolbarParams;
import cn.jarlen.richcommon.jwebview.lifecycle.WebViewLifeCycleManager;
import cn.jarlen.richcommon.jwebview.lifecycle.WebViewLifecycleCallbacks;
import cn.jarlen.richcommon.jwebview.settings.IWebViewSettings;
import cn.jarlen.richcommon.jwebview.ui.IWebIndicator;
import cn.jarlen.richcommon.jwebview.util.ThreadUtils;
import cn.jarlen.richcommon.jwebview.util.WebUtil;

public class WebAgent implements IWebView {

    /*Begin: 注册事件集合*/
    private Set<String> eventSet = new HashSet<>();
    private static final String EVENT_BACK = "onBack";
    private static final String EVENT_MENU_CLICK = "onMenuClick";
    private static final String EVENT_ON_RESUME = "onResume";
    private static final String EVENT_ON_PAUSE = "onPause";
    private static final String EVENT_RECEIVE_PUSH_MSG = "receivePushMsg";
    private static final String EVENT_ON_WEB_CLOSE = "onWebClose";
    private static final String EVENT_RECEIVE_LOCATION = "onReceiveLocation";
    /*End: 注册事件集合*/

    private Gson gson = new Gson();

    private WebView currentWebView;
    private IWebIndicator webIndicator;

    /*快速关闭web*/
    private boolean quickCloseWeb = false;
    private boolean refreshToolBarMenu = true;
    /*WebView 注入对象*/
    private ArrayMap<String, Object> mJsInterfaces;
    private JsInterfaceHolder jsInterfaceHolder;

    private final WeakReference<Activity> activityWeakReference;

    /*页面周期管理类*/
    private final WebViewLifeCycleManager webViewLifeCycleManager;
    /*页面周期管理类*/

    /*界面结果监听*/
    private MainFrameListener mFrameListener;

    private IWebViewSettings webViewSettings;

    private String mCurrentUrl;

    public WebAgent(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.webViewLifeCycleManager = new WebViewLifeCycleManager();
    }

    public WebAgent setWebViewSettings(IWebViewSettings webViewSettings) {
        this.webViewSettings = webViewSettings;
        return this;
    }

    public WebAgent setIndicatorView(IWebIndicator webIndicator) {
        this.webIndicator = webIndicator;
        return this;
    }

    public WebAgent setWebView(WebView webView) {
        this.currentWebView = webView;
        return this;
    }

    public WebAgent setFrameListener(MainFrameListener frameListener) {
        this.mFrameListener = frameListener;
        return this;
    }

    public WebAgent setQuickCloseWeb(boolean quickCloseWeb) {
        this.quickCloseWeb = quickCloseWeb;
        return this;
    }

    public WebAgent setRefreshToolBarMenu(boolean refreshToolBarMenu) {
        this.refreshToolBarMenu = refreshToolBarMenu;
        return this;
    }

    public WebAgent addJsInterface(Object obj, String name) {
        if (null == mJsInterfaces) {
            mJsInterfaces = new ArrayMap<>();
        }
        mJsInterfaces.put(name, obj);
        return this;
    }

    public WebAgent ready() {
        webViewSettings.toSetting(currentWebView);
        if (mJsInterfaces != null && !mJsInterfaces.isEmpty()) {
            this.jsInterfaceHolder = new AbsJsInterfaceHolderImpl(this.currentWebView);
            jsInterfaceHolder.addJavaObjects(mJsInterfaces);
        }
        return this;
    }

    @Override
    public void callJs(NativeCall2JsBean resp) {
        Activity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        final String respStr = WebUtil.escapeJavaStyleString(
                gson.toJson(resp),
                true,
                true);
        callBackJs(activity, respStr);
    }

    private void callBackJs(Activity activity, String respStr) {
        if (ThreadUtils.isMainThread()) {
            currentWebView.loadUrl("javascript:window.EOA.callBackJs('" + respStr + "')");
            return;
        }
        activity.runOnUiThread(() -> callBackJs(activity, respStr));
    }

    @Override
    public boolean addEventListener(String event) {
        return false;
    }

    @Override
    public void setRightButtonVisibility(int location, int visibility) {

    }

    @Override
    public WebView getWebView() {
        return null;
    }

    @Override
    public void onReceivePushMsg(String pushMsg) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void registerLifecycleCallbacks(WebViewLifecycleCallbacks lifecycle) {

    }

    @Override
    public void unRegisterLifecycleCallbacks(WebViewLifecycleCallbacks lifecycle) {

    }

    @Override
    public void onPageStarted(WebView view, String url) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {

    }

    @Override
    public void showLoading(boolean showLoading, boolean canCancel) {

    }

    @Override
    public void onProgress(WebView view, int newProgress) {
        if (webIndicator == null) {
            return;
        }
        if (newProgress == 0) {
            webIndicator.reset();
        } else if (newProgress > 0 && newProgress <= 10) {
            webIndicator.show();
        } else if (newProgress > 10 && newProgress < 95) {
            webIndicator.setProgress(newProgress);
        } else {
            webIndicator.setProgress(newProgress);
            webIndicator.hide();
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public Object getHyBirdData() {
        return null;
    }

    @Override
    public boolean back() {
        if (mFrameListener != null && mFrameListener.isForceBackClose()) {
            return false;
        }
        if (sendEvent(EVENT_BACK, "")) {
            return true;
        }
        if (currentWebView.canGoBack()) {
            currentWebView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void onMainFrameError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onShowMainFrame() {
        if (mFrameListener == null) {
            return;
        }
        mFrameListener.onShowMainFrame();
    }

    @Override
    public ArrayMap<String, String> getConfigData() {
        return null;
    }

    @Override
    public String getCurrentUrl() {
        return mCurrentUrl;
    }

    @Override
    public void onReceiveLocation(GpsLocationResult locationData) {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setToolBarStyle(ChangeToolbarParams changeToolbarParams) {

    }

    @Override
    public String getHostAddress(String hostKey) {
        return null;
    }

    @Override
    public boolean onClientJsAlert(String url, String message, JsResult result) {
        return false;
    }

    @Override
    public boolean onClientJsConfirm(String url, String message, JsResult result) {
        return mFrameListener != null ? mFrameListener.onClientJsConfirm(url, message, result) : Boolean.FALSE;
    }

    @Override
    public boolean onClientJsPrompt(String url, String message, String defaultValue, JsPromptResult result) {
        return mFrameListener != null ? mFrameListener.onClientJsPrompt(url, message, defaultValue, result) : Boolean.FALSE;
    }

    @Override
    public boolean onClientShowFileChooser(ValueCallback valueCallbacks, WebChromeClient.FileChooserParams fileChooserParams) {
        return mFrameListener != null ? mFrameListener.onClientShowFileChooser(valueCallbacks, fileChooserParams) : Boolean.FALSE;
    }

    @Override
    public void setHostAddress(Map hostMap) {

    }

    @Override
    public void loadWebViewUrl(String url, Map urlHeader) {

    }

    @Override
    public void setMenu(List menuItems) {

    }

    private boolean sendEvent(String callback, Object result) {
        if (eventSet.contains(callback)) {
            callJs(NativeCall2JsBean.createEvent(callback, result));
            return true;
        }
        return false;
    }
}
