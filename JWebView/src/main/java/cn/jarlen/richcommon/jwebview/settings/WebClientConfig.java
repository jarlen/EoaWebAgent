package cn.jarlen.richcommon.jwebview.settings;

import android.app.Activity;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import cn.jarlen.richcommon.jwebview.client.DefaultWebChromeClient;
import cn.jarlen.richcommon.jwebview.client.DefaultWebViewClient;
import cn.jarlen.richcommon.jwebview.client.IWebView;

public class WebClientConfig implements IWebClientConfig {
    @Override
    public WebViewClient getWebViewClient(Activity activity, IWebView iWebView) {
        return new DefaultWebViewClient(activity, iWebView);
    }

    @Override
    public WebChromeClient getWebChromeClient(Activity activity, IWebView iWebView) {
        return new DefaultWebChromeClient(activity, iWebView);
    }
}
