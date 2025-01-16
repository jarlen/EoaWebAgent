package cn.jarlen.richcommon.jwebview.entity;


public class CallBackBean {

    public static final int TYPE_EVENT = 1;

    public static final int TYPE_SUCCESS = 2;

    public static final int TYPE_ERROR = 3;

    private int type;

    private int code;

    private String message;

    private String callback;

    private Object data;

    public CallBackBean(int type, int code, String message, String callback, Object data) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.callback = callback;
        this.data = data;
    }

    public static CallBackBean createEvent(String callback, Object result) {
        return new CallBackBean(TYPE_EVENT, 0, "", callback, result);
    }

    public static CallBackBean createError(int code, String message, String callback) {
        return new CallBackBean(TYPE_ERROR, code, message, callback, "");
    }

    public static CallBackBean createSuccess(String callback, Object result) {
        return new CallBackBean(TYPE_SUCCESS, 0, "", callback, result);
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
}
