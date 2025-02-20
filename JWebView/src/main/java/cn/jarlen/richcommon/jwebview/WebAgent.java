package cn.jarlen.richcommon.jwebview;

import static cn.jarlen.richcommon.jwebview.entity.AgentEvent.EVENT_PAGE_HIDE;
import static cn.jarlen.richcommon.jwebview.entity.AgentEvent.EVENT_PAGE_RELEASE;
import static cn.jarlen.richcommon.jwebview.entity.AgentEvent.EVENT_PAGE_SHOW;
import static cn.jarlen.richcommon.jwebview.entity.AgentEvent.EVENT_PUSH_MSG_TO_PAGE;

import android.app.Activity;
import android.util.ArrayMap;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.jarlen.richcommon.jwebview.client.AbsJsInterfaceHolderImpl;
import cn.jarlen.richcommon.jwebview.client.IWebView;
import cn.jarlen.richcommon.jwebview.client.JsInterfaceHolder;
import cn.jarlen.richcommon.jwebview.entity.AgentEvent;
import cn.jarlen.richcommon.jwebview.entity.NativeCall2JsBean;
import cn.jarlen.richcommon.jwebview.lifecycle.WebViewLifeCycleManager;
import cn.jarlen.richcommon.jwebview.lifecycle.WebViewLifecycleCallbacks;
import cn.jarlen.richcommon.jwebview.settings.IWebViewSettings;
import cn.jarlen.richcommon.jwebview.settings.WebClientConfig;
import cn.jarlen.richcommon.jwebview.ui.IWebIndicator;
import cn.jarlen.richcommon.jwebview.util.ThreadUtils;
import cn.jarlen.richcommon.jwebview.util.WebUtil;

public class WebAgent implements IWebView {

    /*Begin: 注册事件集合*/
    private Set<String> eventSet = new HashSet<>();
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
    private WebFrameIndicator mFrameIndicator;

    private IWebViewSettings webViewSettings;

    /*缓存当前出现错误的页面*/
    private Set<String> mErrorUrlsSet = new HashSet<>();
    private String mCurrentUrl;

    public WebAgent(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.webViewLifeCycleManager = new WebViewLifeCycleManager();
    }

    public WebAgent(Builder builder) {
        this.webViewLifeCycleManager = new WebViewLifeCycleManager();
        this.activityWeakReference = new WeakReference<>(builder.activity);
        this.currentWebView = builder.webView;
        this.mFrameIndicator = builder.frameIndicator;
        this.webIndicator = builder.webIndicator;

        if (builder.webClientConfig == null){
            
        }

        if (builder.webClientConfig != null){
            WebSettings webSettings = new WebSettings(builder.webClientConfig.getWebViewClient(builder.activity, this),
                    builder.webClientConfig.getWebChromeClient(builder.activity, this));
            webSettings.toSetting(this.currentWebView);
        }
    }

    public class Builder {

        private Activity activity;
        private WebView webView;
        private IWebIndicator webIndicator;
        private IWebViewSettings webViewSettings;
        private WebFrameIndicator frameIndicator;
        private WebClientConfig webClientConfig;
        private boolean debugEnabled = false;

        private Builder setActivity(Activity activity){
            this.activity = activity;
            return this;
        }
        public Builder setWebView(WebView webView) {
            this.webView = webView;
            return this;
        }

        public Builder setWebClientConfig(WebClientConfig webClientConfig) {
            this.webClientConfig = webClientConfig;
            return this;
        }

        public Builder setWebViewSettings(IWebViewSettings webViewSettings) {
            this.webViewSettings = webViewSettings;
            return this;
        }

        public Builder setWebIndicator(IWebIndicator webIndicator) {
            this.webIndicator = webIndicator;
            return this;
        }

        public Builder setFrameIndicator(WebFrameIndicator frameIndicator) {
            this.frameIndicator = frameIndicator;
            return this;
        }

        public Builder setDebugEnabled(boolean enabled) {
            this.debugEnabled = enabled;
            return this;
        }

        public WebAgent build() {
            return new WebAgent(this);
        }
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
        if (isSupportEvent(event)) {
            eventSet.add(event);
            return true;
        }
        return false;
    }

    @Override
    public WebView getWebView() {
        return currentWebView;
    }

    @Override
    public void onReceivePushMsg(String pushMsg) {
        sendEvent(EVENT_PUSH_MSG_TO_PAGE, pushMsg);
    }

    @Override
    public void onResume() {
        sendEvent(EVENT_PAGE_SHOW, "");
        if (currentWebView != null) {
            currentWebView.onResume();
        }
        webViewLifeCycleManager.onResumed();
    }

    @Override
    public void onPause() {
        sendEvent(EVENT_PAGE_HIDE, "");
        if (currentWebView != null) {
            currentWebView.onPause();
        }
        webViewLifeCycleManager.onPaused();
    }

    @Override
    public void onDestroy() {
        if (currentWebView != null) {
            currentWebView.destroy();
        }
        webViewLifeCycleManager.unRegisterAllLifecycleCallback();
    }

    @Override
    public void registerLifecycleCallbacks(WebViewLifecycleCallbacks lifecycle) {
        webViewLifeCycleManager.registerLifecycleCallbacks(lifecycle);
    }

    @Override
    public void unRegisterLifecycleCallbacks(WebViewLifecycleCallbacks lifecycle) {
        webViewLifeCycleManager.unRegisterLifecycleCallbacks(lifecycle);
    }

    @Override
    public void onPageStarted(WebView view, String url) {
        this.mCurrentUrl = url;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        this.mCurrentUrl = url;
        if (!mErrorUrlsSet.contains(url)) {
            onShowMainFrame();
        } else {
            view.setVisibility(View.VISIBLE);
        }
        if (!mErrorUrlsSet.isEmpty()) {
            mErrorUrlsSet.clear();
        }
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
        if (mFrameIndicator != null && mFrameIndicator.isForceBackClose()) {
            return false;
        }
        if (sendEvent(EVENT_PAGE_RELEASE, "")) {
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
        if (mFrameIndicator == null) {
            return;
        }
        mFrameIndicator.onShowMainFrame();
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
    public void setTitle(String title) {

    }

    @Override
    public String getHostAddress(String hostKey) {
        return null;
    }

    @Override
    public boolean onClientJsAlert(String url, String message, JsResult result) {
        return mFrameIndicator != null ? mFrameIndicator.onClientJsAlert(url, message, result) : Boolean.FALSE;
    }

    @Override
    public boolean onClientJsConfirm(String url, String message, JsResult result) {
        return mFrameIndicator != null ? mFrameIndicator.onClientJsConfirm(url, message, result) : Boolean.FALSE;
    }

    @Override
    public boolean onClientJsPrompt(String url, String message, String defaultValue, JsPromptResult result) {
        return mFrameIndicator != null ? mFrameIndicator.onClientJsPrompt(url, message, defaultValue, result) : Boolean.FALSE;
    }

    @Override
    public boolean onClientShowFileChooser(ValueCallback valueCallbacks, WebChromeClient.FileChooserParams fileChooserParams) {
        return mFrameIndicator != null ? mFrameIndicator.onClientShowFileChooser(valueCallbacks, fileChooserParams) : Boolean.FALSE;
    }

    @Override
    public void setHostAddress(Map hostMap) {

    }

    @Override
    public void loadWebViewUrl(String url, Map urlHeader) {

    }

    private boolean sendEvent(String callback, Object result) {
        if (eventSet.contains(callback)) {
            callJs(NativeCall2JsBean.createEvent(callback, result));
            return true;
        }
        return false;
    }

    private boolean sendEvent(AgentEvent event, Object result) {
        callJs(NativeCall2JsBean.createEvent(event.getEvent(), result));
        return false;
    }

    private boolean isSupportEvent(String event) {
        return EVENT_PAGE_RELEASE.equals(event)
                || EVENT_PAGE_SHOW.equals(event)
                || EVENT_PAGE_HIDE.equals(event)
                || EVENT_PUSH_MSG_TO_PAGE.equals(event);
    }
}
