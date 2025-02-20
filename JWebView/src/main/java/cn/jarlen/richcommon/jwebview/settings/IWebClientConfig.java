package cn.jarlen.richcommon.jwebview.settings;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.jarlen.richcommon.jwebview.client.IWebView;

public interface IWebClientConfig<ViewClient extends WebViewClient, ChromeClient extends android.webkit.WebChromeClient> {
    ViewClient getWebViewClient(Activity activity, IWebView iWebView);

    ChromeClient getWebChromeClient(Activity activity, IWebView iWebView);

    void toSetting(WebView webView);
}
