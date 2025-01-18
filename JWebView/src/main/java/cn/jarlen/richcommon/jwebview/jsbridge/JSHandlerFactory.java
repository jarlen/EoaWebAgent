package cn.jarlen.richcommon.jwebview.jsbridge;

import java.util.HashMap;
import java.util.Map;

import cn.jarlen.richcommon.jwebview.jsbridge.handler.AddEventListenerHandler;
import cn.jarlen.richcommon.jwebview.jsbridge.handler.HelloHandler;

public class JSHandlerFactory {

    public static final Map<String, Class<? extends JSHandler>> jsHandlerMap = new HashMap<>();

    private static void initJsHandler() {
        jsHandlerMap.put("hello", HelloHandler.class);
        jsHandlerMap.put("registerEvent", AddEventListenerHandler.class);
    }

    public static JSHandler getJSHandler(String jsHandlerName) {
        if (jsHandlerMap.isEmpty()) {
            initJsHandler();
        }
        Class<? extends JSHandler> jsHandlerClass = jsHandlerMap.get(jsHandlerName);
        if (jsHandlerClass != null) {
            try {
                return jsHandlerClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
