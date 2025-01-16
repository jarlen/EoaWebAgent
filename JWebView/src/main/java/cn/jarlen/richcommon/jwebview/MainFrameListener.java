package cn.jarlen.richcommon.jwebview;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public abstract class MainFrameListener {
    abstract void onMainFrameError(WebView view, int errorCode, String description, String failingUrl);

    abstract void onShowMainFrame();

    abstract boolean isForceBackClose();

    abstract void onReceivedTitle(String title);

    boolean onClientJsAlert(String url, String message, JsResult result) {
        return false;
    }

    boolean onClientJsConfirm(String url, String message, JsResult result) {
        return false;
    }

    boolean onClientJsPrompt(String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    boolean onClientShowFileChooser(ValueCallback valueCallbacks, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

//    void onRefresh();
}
