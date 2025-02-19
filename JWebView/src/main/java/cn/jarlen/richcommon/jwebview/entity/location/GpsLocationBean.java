package cn.jarlen.richcommon.jwebview.entity.location;

/**
* @author jarlen
* @date 2019/8/16
* GPS定位数据
*/
public class GpsLocationBean {
    private double latitude;
    private double longitude;
    private String address;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
