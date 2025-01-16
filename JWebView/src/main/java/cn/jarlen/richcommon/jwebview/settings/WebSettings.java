package cn.jarlen.richcommon.jwebview.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

/**
 * @author jarlen
 * @date 2020/6/3
 * H5应用设置
 */
public class WebSettings implements IWebViewSettings {

    public static final String WEB_CACHE_DIR_NAME = "web_cache";

    private android.webkit.WebSettings mWebSettings;
    private final WebViewClient mWebViewClient;
    private final WebChromeClient mWebChromeClient;

    public WebSettings(WebViewClient mWebViewClient, WebChromeClient mWebChromeClient) {
        this.mWebViewClient = mWebViewClient;
        this.mWebChromeClient = mWebChromeClient;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public IWebViewSettings toSetting(WebView webView) {
        mWebSettings = webView.getSettings();

        /*页面内支持js交互*/
        mWebSettings.setJavaScriptEnabled(true);
        /*设置编码格式*/
        mWebSettings.setDefaultTextEncodingName("utf-8");
        /*支持自动加载图片*/
        mWebSettings.setLoadsImagesAutomatically(true);

        /*开启之后，由页面meta设置的viewport控制自适应*/
        mWebSettings.setUseWideViewPort(true);

        /*设置页面展示范围不能超出view*/
        mWebSettings.setLoadWithOverviewMode(true);

        mWebSettings.setNeedInitialFocus(true);
        mWebSettings.setTextZoom(100);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        /*让WebView只显示一列，不能左右滑动*/
        mWebSettings.setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        /*开启硬件加速,内存可能会增加*/
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        /*debug下打开调试*/
        WebView.setWebContentsDebuggingEnabled(false);

        /*缩放操作*/
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);

        /*设置缓存策略,采用默认缓存*/
        mWebSettings.setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);

        /* 允许加载本地文件html  file协议*/
        mWebSettings.setAllowFileAccess(true);

        /*适配5.0以上不允许http和https混合使用情况*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        /*是否允许加载本地html文件 .建议关闭*/
        mWebSettings.setAllowFileAccessFromFileURLs(false);
        mWebSettings.setAllowUniversalAccessFromFileURLs(false);

        /*Application Caches 功能*/
        mWebSettings.setAppCacheEnabled(true);
        /*开启DOM storage API功能*/
        mWebSettings.setDomStorageEnabled(true);
        /*开启数据库 storage API功能*/
        mWebSettings.setDatabaseEnabled(true);

        String cacheDirPath = getWebCachePath(webView.getContext().getApplicationContext());
        /*设置数据库缓存路径*/
        /*
         *Android 4.4开发人员不能（或不必）分配替代数据库路径;所有这些都由Android默认处理
         *mWebSettings.setDatabasePath(cacheDirPath);
         */
        /*设置  Application Caches 缓存目录*/
        mWebSettings.setAppCachePath(cacheDirPath);
        mWebSettings.setUserAgentString("rich-common/android");

        webView.setWebViewClient(mWebViewClient);
        webView.setWebChromeClient(mWebChromeClient);
        return this;
    }

    @Override
    public android.webkit.WebSettings getWebViewSettings() {
        return mWebSettings;
    }

    /**
     * @param context
     * @return WebView 的缓存路径
     */
    private static String getWebCachePath(Context context) {
        String cachePath = context.getCacheDir().getAbsolutePath();
        File file = new File(cachePath + File.separator + WEB_CACHE_DIR_NAME + File.separator);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }
}
