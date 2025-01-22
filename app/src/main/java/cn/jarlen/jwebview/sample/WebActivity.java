package cn.jarlen.jwebview.sample;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import cn.jarlen.richcommon.jwebview.WebFrameIndicator;
import cn.jarlen.richcommon.jwebview.WebAgent;
import cn.jarlen.richcommon.jwebview.client.DefaultWebChromeClient;
import cn.jarlen.richcommon.jwebview.client.DefaultWebViewClient;
import cn.jarlen.richcommon.jwebview.jsbridge.JsBridge2Native;
import cn.jarlen.richcommon.jwebview.settings.WebSettings;
import cn.jarlen.richcommon.jwebview.ui.DefaultWebIndicator;
import cn.jarlen.richcommon.util.AndroidBug5497Workaround;
import cn.jarlen.richcommon.util.StatusBarUtil;
import cn.jarlen.richcommon.widget.toolbar.CommonToolBar;
import cn.jarlen.richcommon.widget.toolbar.ToolBarButtonType;

public class WebActivity extends AppCompatActivity {

    WebAgent webAgent;

    protected WebView webView;
    private CommonToolBar commonToolBar;
    private FrameLayout webViewContainer;
    private DefaultWebIndicator webIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        loadUrl();
    }

    private void initView() {
        commonToolBar = findViewById(R.id.toolbar_web);
        webViewContainer = findViewById(R.id.layout_web);
        webView = findViewById(R.id.view_web);
        webIndicator = findViewById(R.id.web_indicator);

        AndroidBug5497Workaround.assistActivity(this);
        StatusBarUtil.immersive(this);
        StatusBarUtil.darkMode(this);
        StatusBarUtil.setPaddingSmart(this, commonToolBar);
        commonToolBar.setButtonIcon(ToolBarButtonType.LEFT_SECOND_BUTTON, R.drawable.common_widget_ic_toolbar_close);
        initWebView();
    }

    private void initWebView() {
        webAgent = new WebAgent(this);
        WebSettings webSettings = new WebSettings(new DefaultWebViewClient(this, webAgent),
                new DefaultWebChromeClient(this, webAgent));
        webAgent.setWebViewSettings(webSettings)
                .setWebView(webView)
                .setIndicatorView(webIndicator)
                .addJsInterface(new JsBridge2Native(this, webAgent), JsBridge2Native.BRIDGE_NAME)
                .addFrameIndicator(new WebFrameIndicator(){
                    @Override
                    public void onMainFrameError(WebView view, int errorCode, String description, String failingUrl) {

                    }

                    @Override
                    public void onShowMainFrame() {

                    }

                    @Override
                    public boolean isForceBackClose() {
                        return false;
                    }
                })
                .ready();
    }

    private void loadUrl() {
        webAgent.loadWebViewUrl("https://www.baidu.com", null);
    }
}