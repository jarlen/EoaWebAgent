package cn.jarlen.richcommon.jwebview.entity.toolbar;

/**
 * 修改toolbar参数
 *
 * @author jarlen
 * @date 2018/7/23
 */

public class ChangeToolbarParams {

    /**
     * 界面toolbar背景色
     */
    private String toolbarBgColor;

    /**
     * 界面toolbar背景Url
     */
    private String toolbarBgPicUrl;
    /**
     * 界面toolbar标题颜色
     */
    private String toolbarTitleColor;

    /**
     * 界面toolbar标题居中
     */
    private boolean toolbarTitleCenter;

    /**
     * 界面toolbar按钮颜色
     */
    private String toolbarIconColor;

    /**
     * 界面toolbar分割线显隐
     */
    private boolean toolbarDividerShown = Boolean.TRUE;

    /**
     * 界面状态栏图标黑白
     */
    private boolean statusBarDarkMode = Boolean.TRUE;

    public String getToolbarBgColor() {
        return toolbarBgColor;
    }

    public void setToolbarBgColor(String toolbarBgColor) {
        this.toolbarBgColor = toolbarBgColor;
    }

    public String getToolbarTitleColor() {
        return toolbarTitleColor;
    }

    public void setToolbarTitleColor(String toolbarTitleColor) {
        this.toolbarTitleColor = toolbarTitleColor;
    }

    public String getToolbarIconColor() {
        return toolbarIconColor;
    }

    public void setToolbarIconColor(String toolbarIconColor) {
        this.toolbarIconColor = toolbarIconColor;
    }

    public boolean isStatusBarDarkMode() {
        return statusBarDarkMode;
    }

    public void setStatusBarDarkMode(boolean statusBarDarkMode) {
        this.statusBarDarkMode = statusBarDarkMode;
    }

    public boolean isToolbarTitleCenter() {
        return toolbarTitleCenter;
    }

    public void setToolbarTitleCenter(boolean toolbarTitleCenter) {
        this.toolbarTitleCenter = toolbarTitleCenter;
    }

    public boolean isToolbarDividerShown() {
        return toolbarDividerShown;
    }

    public void setToolbarDividerShown(boolean toolbarDividerShown) {
        this.toolbarDividerShown = toolbarDividerShown;
    }

    public String getToolbarBgPicUrl() {
        return toolbarBgPicUrl;
    }

    public void setToolbarBgPicUrl(String toolbarBgPicUrl) {
        this.toolbarBgPicUrl = toolbarBgPicUrl;
    }
}
