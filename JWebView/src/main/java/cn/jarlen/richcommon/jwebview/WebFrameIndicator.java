package cn.jarlen.richcommon.jwebview;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import cn.jarlen.richcommon.jwebview.ui.IWebFrameIndicator;

public abstract class WebFrameIndicator implements IWebFrameIndicator {

    @Override
    public void onReceivedTitle(String title) {
    }

    @Override
    public boolean onClientJsAlert(String url, String message, JsResult result) {
        return false;
    }

    public boolean onClientJsConfirm(String url, String message, JsResult result) {
        return false;
    }

    public boolean onClientJsPrompt(String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    public boolean onClientShowFileChooser(ValueCallback valueCallbacks, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

    @Override
    public void onMainFrameError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onShowMainFrame() {

    }
}
