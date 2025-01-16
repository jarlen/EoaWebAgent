package cn.jarlen.richcommon.jwebview.client;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import cn.jarlen.richcommon.jwebview.util.RegexUtils;


public class DefaultWebChromeClient extends WebChromeClient {

    private final Context context;
    private final IWebView iWebView;

    boolean lockTitle = false;

    public DefaultWebChromeClient(Context context, IWebView iWebView) {
        this.context = context;
        this.iWebView = iWebView;
    }

    public void setLockTitle(boolean lockTitle) {
        this.lockTitle = lockTitle;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        /*接收页面标题title，规避排除部分手机title为网址的问题*/
        /*lockTitle 锁定标题不随页面改变*/
        if (lockTitle || RegexUtils.isURL(title)) {
            return;
        }
        if (TextUtils.isEmpty(title)) {
            iWebView.setTitle("");
            return;
        }
        /*规避title属于url中部分路径的tile*/
        if (view.getUrl().contains(title)) {
            return;
        }
        iWebView.setTitle(title);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        return this.iWebView.onClientJsAlert(url, message, result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (iWebView == null) {
            return;
        }
        iWebView.onProgress(view, newProgress);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        return this.iWebView.onClientJsConfirm(url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        return this.iWebView.onClientJsPrompt(url, message, defaultValue, result);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return this.iWebView.onClientShowFileChooser(filePathCallback, fileChooserParams);
    }

}
