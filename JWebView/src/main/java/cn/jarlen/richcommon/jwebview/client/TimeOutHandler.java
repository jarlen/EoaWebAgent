package cn.jarlen.richcommon.jwebview.client;

import static android.webkit.WebViewClient.ERROR_TIMEOUT;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * @author jarlen
 * @date 2019/7/24
 * 超时消息句柄
 */
public class TimeOutHandler extends Handler {

    /**
     * 超时时间(单位毫秒)
     */
    public static final int TIME_OUT = 15000;

    private final WeakReference<Activity> weakReference;

    private final IWebView mH5HyBirdWebView;

    public TimeOutHandler(Activity activity, IWebView iWebView) {
        this.weakReference = new WeakReference<>(activity);
        this.mH5HyBirdWebView = iWebView;
    }

    @Override
    public void handleMessage(Message msg) {
        Activity activity = weakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Log.d("TimeOutHandler", "TimeOutHandler time out ");
        mH5HyBirdWebView.getWebView().stopLoading();
        mH5HyBirdWebView.onMainFrameError(mH5HyBirdWebView.getWebView(), ERROR_TIMEOUT, "网络连接超时，请检查网络后重试", (String) msg.obj);
    }
}
