package cn.jarlen.richcommon.jwebview.settings;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.jarlen.richcommon.jwebview.client.DefaultWebChromeClient;
import cn.jarlen.richcommon.jwebview.client.DefaultWebViewClient;
import cn.jarlen.richcommon.jwebview.client.IWebView;
import cn.jarlen.richcommon.jwebview.util.WebUtil;

public abstract class WebClientConfig {

    public WebViewClient getWebViewClient(Activity activity, IWebView iWebView) {
        return new DefaultWebViewClient(activity, iWebView);
    }

    public WebChromeClient getWebChromeClient(Activity activity, IWebView iWebView) {
        return new DefaultWebChromeClient(activity, iWebView);
    }

    public void toSetting(Activity activity, IWebView iWebView) {
        WebView webView = iWebView.getWebView();
        WebSettings webSettings = webView.getSettings();

        /*页面内支持js交互*/
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片

        /*开启之后，由页面meta设置的viewport控制自适应*/
        webSettings.setUseWideViewPort(true);

        /*设置页面展示范围不能超出view*/
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setNeedInitialFocus(true);
        webSettings.setTextZoom(100);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        /*让WebView只显示一列，不能左右滑动*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        /*缩放操作*/
        webSettings.setSupportZoom(false);
        /*设置缓存策略*/
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
        /* 允许加载本地文件html  file协议*/
        webSettings.setAllowFileAccess(true);
        /*适配5.0以上不允许http和https混合使用情况*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /*是否允许加载本地html文件 .建议关闭*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(false);
            webSettings.setAllowUniversalAccessFromFileURLs(false);
        }
        /*Application Caches 功能*/
        webSettings.setAppCacheEnabled(true);
        /*开启DOM storage API功能*/
        webSettings.setDomStorageEnabled(true);
        /*开启数据库 storage API功能*/
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = WebUtil.getDiskCacheDir(webView.getContext(), WebUtil.APP_CACHE_DIRNAME).getAbsolutePath();
        webSettings.setDatabasePath(cacheDirPath);//设置数据库缓存路径
        webSettings.setAppCachePath(cacheDirPath);//设置  Application Caches 缓存目录

        webSettings.setUserAgentString("JWeb");

        /*开启硬件加速,内存可能会增加*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            //debug下打开调试
            if (iWebView) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        webView.setWebViewClient(get);
        webView.setWebChromeClient(mWebChromeClient);
    }

}
