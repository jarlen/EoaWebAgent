package cn.jarlen.richcommon.util;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * @author jarlen
 * @date 2020/7/15
 * 用于解决全屏时，Activity设置了adjustSize却不起作用的问题。
 * setContentView之后调用{@link #assistActivity(Activity)}
 */
public class AndroidBug5497Workaround {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    public static void assistActivity(Activity activity) {
        new AndroidBug5497Workaround(activity);
    }

    private Activity activity;
    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private int screenHeight;

    private AndroidBug5497Workaround(Activity activity) {
        this.activity = activity;
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
        screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            /*int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();*/
            int usableHeightSansKeyboard = screenHeight;

            /*这个判断是为了解决19之前的版本不支持沉浸式状态栏导致布局显示不完全的问题*/
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                int statusBarHeight = getStatusBarHeight();
                usableHeightSansKeyboard -= statusBarHeight;
            }
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                setHeight(usableHeightSansKeyboard - heightDifference);
            } else {
                // keyboard probably just became hidden
                //还原默认高度，不能用计算的值，因为虚拟导航栏显示或者隐藏的时候也会改变高度
                setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private void setHeight(int height) {
        if (frameLayoutParams.height == height) {
            //不必要的更新，直接返回
            return;
        }
        frameLayoutParams.height = height;
        //触发布局更新
        mChildOfContent.requestLayout();
    }

    private int getStatusBarHeight() {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        /*这个判断是为了解决19之后的版本在弹出软键盘时，键盘和推上去的布局（adjustResize）之间有黑色区域的问题*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            return (r.bottom - r.top) + statusBarHeight;
        }
        return (r.bottom - r.top);
    }
}