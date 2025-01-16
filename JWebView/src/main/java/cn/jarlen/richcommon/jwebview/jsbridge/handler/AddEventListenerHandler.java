package cn.jarlen.richcommon.jwebview.jsbridge.handler;

import android.app.Activity;

import cn.jarlen.richcommon.jwebview.client.IWebView;
import cn.jarlen.richcommon.jwebview.entity.Js2NativeBean;
import cn.jarlen.richcommon.jwebview.entity.NativeCall2JsBean;
import cn.jarlen.richcommon.jwebview.jsbridge.JSHandler;

public class AddEventListenerHandler extends JSHandler {

    @Override
    public void handle(Activity context, IWebView iWebView, Js2NativeBean param) {
        String event = param.getParams();
        boolean support = iWebView.addEventListener(event);
        if (support) {
            iWebView.callJs(NativeCall2JsBean.createSuccess(param.getSuccess(), ""));
        } else {
            iWebView.callJs(NativeCall2JsBean.createError(NativeCall2JsBean.ErrorCode.ERROR_NOT_SUPPORT_METHOD, event));
        }
    }
}
