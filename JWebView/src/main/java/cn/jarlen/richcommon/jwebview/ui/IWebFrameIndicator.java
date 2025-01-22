package cn.jarlen.richcommon.jwebview.ui;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public interface IWebFrameIndicator {
     void onMainFrameError(WebView view, int errorCode, String description, String failingUrl);

     void onShowMainFrame();

     boolean isForceBackClose();

     void onReceivedTitle(String title);

    boolean onClientJsAlert(String url, String message, JsResult result);

    boolean onClientJsConfirm(String url, String message, JsResult result);

    boolean onClientJsPrompt(String url, String message, String defaultValue, JsPromptResult result);

     boolean onClientShowFileChooser(ValueCallback valueCallbacks, WebChromeClient.FileChooserParams fileChooserParams) ;
}
