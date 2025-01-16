package cn.jarlen.richcommon.jwebview.entity;


public class NativeCall2JsBean {

    public static final int TYPE_EVENT = 1;

    public static final int TYPE_SUCCESS = 2;

    public static final int TYPE_ERROR = 3;

    private int type;

    private int code;

    private String message;

    private String callback;

    private Object data;

    public NativeCall2JsBean(int type, int code, String message, String callback, Object data) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.callback = callback;
        this.data = data;
    }

    public static NativeCall2JsBean createEvent(String callback, Object result) {
        return new NativeCall2JsBean(TYPE_EVENT, 0, "", callback, result);
    }

    public static NativeCall2JsBean createError(int code, String message, String callback) {
        return new NativeCall2JsBean(TYPE_ERROR, code, message, callback, "");
    }

    public static NativeCall2JsBean createError(ErrorCode errorCode, String callback) {
        return NativeCall2JsBean.createError(errorCode.code, errorCode.message, callback);
    }

    public static NativeCall2JsBean createSuccess(String callback, Object result) {
        return new NativeCall2JsBean(TYPE_SUCCESS, 0, "请求成功", callback, result);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static enum ErrorCode {

        SUCCESS(0, "请求成功"),
        ERROR_INVALID_PARAM(-1, "参数错误"),
        ERROR_NOT_SUPPORT_METHOD(-2, "当前客户端不支持该事件"),
        ERROR_SIGN(-3, "签名错误"),
        ERROR_NET(-4, "网络错误"),
        ERROR_IO(-5, "IO错误"),
        ERROR_METHOD_ACCESS_FAIL(-6, "方法访问失败"),
        ERROR_SERVER_ERROR(-7, "服务器错误"),
        ERROR_DATA_PARSE_ERROR(-8, "数据解析错误");

        private int code;
        private String message;

        private ErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
