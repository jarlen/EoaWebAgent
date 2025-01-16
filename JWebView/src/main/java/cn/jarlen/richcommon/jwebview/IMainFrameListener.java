package cn.jarlen.richcommon.jwebview;

import android.webkit.WebView;

public interface IMainFrameListener {
    void onMainFrameError(WebView view, int errorCode, String description, String failingUrl);

    void onShowMainFrame();

    boolean isForceBackClose();

//    void onRefresh();
}
