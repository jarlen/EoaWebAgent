package cn.jarlen.richcommon.jwebview.lifecycle;

import android.os.Bundle;

public interface IWebViewLifecycleCallbacks {
    void onCreated(Bundle savedInstanceState);

    void onStarted();

    void onResumed();

    void onPaused();

    void onStopped();

    void onSaveInstanceState(Bundle outState);

    void onDestroyed();

    void onClear();
}
