package cn.jarlen.richcommon.jwebview.client;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
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

import cn.jarlen.richcommon.jwebview.util.IntentUrl;


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
        if (IntentUrl.shouldOverrideUrlLoading(weakReference, url)) {
            return true;
        }
        Log.d(TAG, "shouldOverrideUrlLoading  url---> " + url);
        return super.shouldOverrideUrlLoading(webview, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webview, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (IntentUrl.shouldOverrideUrlLoading(weakReference, url)) {
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

}
