package cn.jarlen.richcommon.jwebview.client;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;


/**
 * @author jarlen
 * @date 2019/7/24
 * 自定义WebViewClient
 */
public class DefaultWebViewClient extends WebViewClient {

    private static final String TAG = DefaultWebViewClient.class.getSimpleName();
    public static final int LOAD_TIME_OUT_WHAT = 10001;
    private final TimeOutHandler timeOutHandler;

    private final IWebView iWebView;

    private String startUrl;

    final WeakReference<Activity> weakReference;

    private boolean mIsFinished;
    private long usageTime;

    public DefaultWebViewClient(Activity activity, IWebView iWebView) {
        this.iWebView = iWebView;
        this.weakReference = new WeakReference<>(activity);
        this.timeOutHandler = new TimeOutHandler(activity, iWebView);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        startUrl = url;
        iWebView.onPageStarted(view, startUrl);
        //加载下一个页面时，清空上一个页面的设置
        iWebView.clear();
        mIsFinished = false;
        startTimer();
        Log.d(TAG, "onPageStarted url---> " + url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webview, String url) {
        if (shouldOverrideUrlLoading(weakReference, url)) {
            return true;
        }
        Log.d(TAG, "shouldOverrideUrlLoading  url---> " + url);
        return super.shouldOverrideUrlLoading(webview, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webview, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (shouldOverrideUrlLoading(weakReference, url)) {
            return true;
        }
        Log.d(TAG, "shouldOverrideUrlLoading22  url---> " + url);
        return super.shouldOverrideUrlLoading(webview, request);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        stopTimer();
        /*防止重定向走两次 finish*/
        if (mIsFinished) {
            Log.d(TAG, "onPageFinished mIsFinished");
            return;
        }
        iWebView.onPageFinished(view, url);
        Log.d(TAG, "onPageFinished ( " + url + " )");
        mIsFinished = true;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.d(TAG, "onReceivedError---> errorCode: " + errorCode + " failingUrl: " + failingUrl);
        iWebView.onMainFrameError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.d(TAG, "onReceivedError: " + error);
        if (request.isForMainFrame()) {
            String failingUrl = request.getUrl().toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                iWebView.onMainFrameError(view, error.getErrorCode(), error.getDescription().toString(), failingUrl);
            } else {
                iWebView.onMainFrameError(view, ERROR_UNKNOWN, "未知错误", failingUrl);
            }
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        /*接受所有网站的证书*/
        handler.proceed();
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        String key = "http://localhost";
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (url != null && url.contains(key)) {
            String imgPath = url.replace(key, "");
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(imgPath);
                return new WebResourceResponse("image/png", "UTF-8", fileInputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return super.shouldInterceptRequest(view, url);
    }

    /**
     * 计时器   计时 加载每个资源 的时间  （超时操作）
     */
    private void startTimer() {
        Log.d(TAG, "startTimeOutTimer");
        Message message = timeOutHandler.obtainMessage();
        message.what = LOAD_TIME_OUT_WHAT;
        message.obj = startUrl;
        usageTime = System.currentTimeMillis();
        timeOutHandler.sendMessageDelayed(message, TimeOutHandler.TIME_OUT);
    }

    private void stopTimer() {
        Log.d(TAG, "WebView load url:" + startUrl + " : usageTime =" + (System.currentTimeMillis() - usageTime) + "ms");
        timeOutHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 是否拦截url
     *
     * @param weakReference 软引用activity
     * @param url           目标url
     * @return true代表拦截
     */
    public static boolean shouldOverrideUrlLoading(WeakReference<Activity> weakReference, String url) {
        //电话 ， 邮箱 ， 短信
        if (handleCommonLink(weakReference, url)) {
            return true;
        }
        // intent
        if (handleIntentUrl(weakReference, url)) {
            return true;
        }
        return false;
    }

    /**
     * SMS scheme
     */
    public static final String SCHEME_SMS = "sms:";

    /**
     * intent ' s scheme
     */
    public static final String SCHEME_INTENT = "intent://";

    public static boolean handleCommonLink(WeakReference<Activity> activityWeakReference, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.startsWith(WebView.SCHEME_TEL)
                || url.startsWith(SCHEME_SMS)
                || url.startsWith(WebView.SCHEME_MAILTO)
                || url.startsWith(WebView.SCHEME_GEO)) {
            try {
                Activity mActivity = null;
                if ((mActivity = activityWeakReference.get()) == null) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
                ignored.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static boolean handleIntentUrl(WeakReference<Activity> activityWeakReference, String intentUrl) {
        if (TextUtils.isEmpty(intentUrl)) {
            return false;
        }

        if (intentUrl.startsWith(SCHEME_INTENT)) {
            try {
                return lookup(activityWeakReference, intentUrl);
            } catch (ActivityNotFoundException ignored) {
                ignored.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private static boolean lookup(WeakReference<Activity> activityWeakReference, String url) {
        try {
            Intent intent;
            Activity mActivity = null;
            if ((mActivity = activityWeakReference.get()) == null) {
                return true;
            }
            PackageManager packageManager = mActivity.getPackageManager();
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            // 跳到该应用
            if (info != null) {
                mActivity.startActivity(intent);
                return true;
            }
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
        return false;
    }
}
