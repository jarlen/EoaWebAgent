package cn.jarlen.richcommon.jwebview.jsbridge.handler;

import android.app.Activity;
import android.widget.Toast;

import cn.jarlen.richcommon.jwebview.client.IWebView;
import cn.jarlen.richcommon.jwebview.entity.Js2NativeBean;
import cn.jarlen.richcommon.jwebview.entity.NativeCall2JsBean;
import cn.jarlen.richcommon.jwebview.jsbridge.JSHandler;

public class HelloHandler extends JSHandler {

    @Override
    public void handle(Activity context, IWebView iWebView, Js2NativeBean param) {
        Toast.makeText(context, param.getParams(), Toast.LENGTH_SHORT).show();
        iWebView.callJs(NativeCall2JsBean.createSuccess(param.getSuccess(), "HelloHandler调用成功"));
    }
}
