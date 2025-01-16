package cn.jarlen.richcommon.jwebview.lifecycle;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jarlen
 */
public class WebViewLifeCycleManager implements IWebViewLifecycleCallbacks {

    private final List<IWebViewLifecycleCallbacks> lifecycleList = new ArrayList<>();

    public WebViewLifeCycleManager() {
    }

    public void registerLifecycleCallbacks(IWebViewLifecycleCallbacks lifecycle) {
        lifecycleList.add(lifecycle);
    }

    public void unRegisterLifecycleCallbacks(IWebViewLifecycleCallbacks lifecycle) {
        lifecycleList.remove(lifecycle);
    }

    public void unRegisterAllLifecycleCallback() {
        lifecycleList.clear();
    }

    @Override
    public void onCreated(Bundle savedInstanceState) {
        List<IWebViewLifecycleCallbacks> data = new ArrayList<>(lifecycleList);
        for (IWebViewLifecycleCallbacks obj : data) {
            obj.onCreated(savedInstanceState);
        }
    }

    @Override
    public void onStarted() {
        List<IWebViewLifecycleCallbacks> data = new ArrayList<>(lifecycleList);
        for (IWebViewLifecycleCallbacks obj : data) {
            obj.onStarted();
        }
    }

    @Override
    public void onResumed() {
        List<IWebViewLifecycleCallbacks> data = new ArrayList<>(lifecycleList);
        for (IWebViewLifecycleCallbacks obj : data) {
            obj.onResumed();
        }
    }

    @Override
    public void onPaused() {
        List<IWebViewLifecycleCallbacks> data = new ArrayList<>(lifecycleList);
        for (IWebViewLifecycleCallbacks obj : data) {
            obj.onPaused();
        }
    }

    @Override
    public void onStopped() {
        List<IWebViewLifecycleCallbacks> data = new ArrayList<>(lifecycleList);
        for (IWebViewLifecycleCallbacks obj : data) {
            obj.onStopped();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        List<IWebViewLifecycleCallbacks> data = new ArrayList<>(lifecycleList);
        for (IWebViewLifecycleCallbacks obj : data) {
            obj.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroyed() {
        List<IWebViewLifecycleCallbacks> data = new ArrayList<>(lifecycleList);
        for (IWebViewLifecycleCallbacks obj : data) {
            obj.onDestroyed();
        }
    }

    @Override
    public void onClear() {
        List<IWebViewLifecycleCallbacks> data = new ArrayList<>(lifecycleList);
        for (IWebViewLifecycleCallbacks obj : data) {
            obj.onClear();
        }
    }
}
