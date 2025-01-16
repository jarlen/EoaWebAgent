package cn.jarlen.richcommon.jwebview.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import cn.jarlen.richcommon.jwebview.client.DefaultWebChromeClient;
import cn.jarlen.richcommon.jwebview.client.DefaultWebViewClient;
import cn.jarlen.richcommon.jwebview.client.IWebView;
import cn.jarlen.richcommon.jwebview.jsbridge.JsBridge2Native;

/**
 * Created by ldy on 2017/6/8.
 */

public class WebUtil {
    public static final String APP_CACHE_DIRNAME = "webcache";

    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebView(FragmentActivity context, IWebView iWebView, Map<String, String> header) {
        WebView webView = iWebView.getWebView();
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSavePassword(false);

        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //适配5.0不允许http和https混合使用情况
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        mWebSettings.setTextZoom(100);
        mWebSettings.setDatabaseEnabled(true);
        //Application Caches 功能
        mWebSettings.setAppCacheEnabled(true);

        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setSupportMultipleWindows(false);
        // 是否阻塞加载网络图片  协议http or https
        mWebSettings.setBlockNetworkImage(false);

        // 允许加载本地文件html  file协议
        mWebSettings.setAllowFileAccess(false);

        // 通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
        mWebSettings.setAllowFileAccessFromFileURLs(false);
        // 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        mWebSettings.setAllowUniversalAccessFromFileURLs(false);

        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mWebSettings.setLoadWithOverviewMode(false);
        mWebSettings.setUseWideViewPort(false);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setNeedInitialFocus(true);

        String cacheDirPath = getDiskCacheDir(context, APP_CACHE_DIRNAME).getAbsolutePath();

        /*设置数据库缓存路径*/
        webView.getSettings().setDatabasePath(cacheDirPath);

        /*设置  Application Caches 缓存目录*/
        webView.getSettings().setAppCachePath(cacheDirPath);

        /*开启 Application Caches 功能*/
        webView.getSettings().setAppCacheEnabled(true);

        if (header != null && header.containsKey("user-agent")) {
            webView.getSettings().setUserAgentString(header.get("user-agent"));
        }

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new DefaultWebViewClient(context, iWebView));
        webView.setWebChromeClient(new DefaultWebChromeClient(context, iWebView));
        webView.addJavascriptInterface(new JsBridge2Native(context, iWebView), JsBridge2Native.BRIDGE_NAME);
    }
//
//    public static void syncCookie(String url) {
//        HttpUrl parse = HttpUrl.parse(url);
//        if (parse != null) {
//            CookieJarManager cookieJarManager = CookieJarManager.getInstance();
//            List<Cookie> cookies = cookieJarManager.loadForRequest(parse);
//            if (cookies.size() > 0) {
//                CookieManager cookieManager = CookieManager.getInstance();
//                cookieManager.setAcceptCookie(true);
//                for (Cookie cookie : cookies) {
//                    if (!TextUtils.isEmpty(cookie.name()) && !TextUtils.isEmpty(cookie.value())) {
//                        cookieManager.setCookie(parse.host(), cookie.name() + "=" + cookie.value());
//                    }
//                }
//                cookieManager.flush();
//            }
//        }
//    }

    /**
     * 根据请求的全路径，获取工程名（含工程名）之前的地址
     *
     * @param urlPath
     * @return
     */
    public static String convertUrl(String urlPath) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlPath);
            sb.append(url.getProtocol()).append("://")
                    .append(url.getHost()).append(":")
                    .append(url.getPort());
            String path = url.getPath();
            if (TextUtils.isEmpty(path)) {
                return sb.toString();
            }
            String[] paths = path.split("/");
            if (paths.length > 0) {
                sb.append("/");
                if (url.getPath().startsWith("/")) {
                    sb.append(paths[1]);
                } else {
                    sb.append(paths[0]);
                }
            }
        } catch (Exception e) {
            return urlPath;
        }
        return sb.toString();
    }

    public static void removeAllCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(null);
        } else {
            cookieManager.removeAllCookie();
        }
    }

    /**
     * @param context
     * @return WebView 的缓存路径
     */
    public static String getWebCachePath(Context context) {
        String cachePath = context.getCacheDir().getAbsolutePath();
        File file = new File(cachePath + File.separator + APP_CACHE_DIRNAME + File.separator);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            }
        }
        if (cachePath == null) {
            cachePath = context.getCacheDir().getPath();
        }

        File file = new File(cachePath + File.separator + uniqueName + File.separator);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }


    public static String escapeJavaStyleString(String str, boolean escapeSingleQuotes, boolean escapeForwardSlash) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes, escapeForwardSlash);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            //throw new UnhandledException(ioe);
        }
        return "";
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote,
                                              boolean escapeForwardSlash) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.write("\\u" + hex(ch));
            } else if (ch > 0xff) {
                out.write("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                out.write("\\u00" + hex(ch));
            } else if (ch < 32) {

                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;


                    default:
                        if (ch > 0xf) {
                            out.write("\\u00" + hex(ch));
                        } else {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {


                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) {
                            out.write('\\');
                        }
                        out.write('\'');
                        break;
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    case '/':
                        if (escapeForwardSlash) {
                            out.write('\\');
                        }
                        out.write('/');
                        break;
                    default:
                        out.write(ch);
                        break;
                }
            }
        }
    }

}
