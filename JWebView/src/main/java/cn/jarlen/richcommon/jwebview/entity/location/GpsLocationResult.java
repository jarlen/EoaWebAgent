package cn.jarlen.richcommon.jwebview.entity.location;

/**
 * @author jarlen
 * @date 2019/8/16
 * GPS定位结果
 */
public class GpsLocationResult {
    /**
     * 定位结果
     * 0:成功
     * -1:定位失败---定位权限
     * -2:定位失败
     */
    private int status;

    /**
     * 失败描述{@link #status=-1 }
     */
    private String message;

    /**
     * 定位结果{@link #status=0 }
     */
    private GpsLocationBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GpsLocationBean getData() {
        return data;
    }

    public void setData(GpsLocationBean data) {
        this.data = data;
    }
}
