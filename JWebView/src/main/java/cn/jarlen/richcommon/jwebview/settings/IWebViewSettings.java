package cn.jarlen.richcommon.jwebview.settings;

import android.webkit.WebView;

public interface IWebViewSettings<T extends android.webkit.WebSettings> {
    IWebViewSettings toSetting(WebView webView);
    T getWebViewSettings();

}
