package cn.jarlen.richcommon.jwebview.entity.toolbar;


public class MenuItemBean {

    private int id;

    private int location;

    /**
     * 按钮文字
     */
    private String text;

    /**
     * 按钮文字颜色
     */
    private String textColor;

    /**
     * 按钮图片资源
     */
    private String icon;

    public MenuItemBean() {
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
