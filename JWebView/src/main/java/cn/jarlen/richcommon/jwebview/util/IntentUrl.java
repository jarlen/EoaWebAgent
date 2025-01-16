package cn.jarlen.richcommon.jwebview.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import java.lang.ref.WeakReference;

public class IntentUrl {
    /**
     * 是否拦截url
     *
     * @param weakReference 软引用activity
     * @param url           目标url
     * @return true代表拦截
     */
    public static boolean shouldOverrideUrlLoading(WeakReference<Activity> weakReference, String url) {
        //电话 ， 邮箱 ， 短信
        if (handleCommonLink(weakReference, url)) {
            return true;
        }
        // intent
        if (handleIntentUrl(weakReference, url)) {
            return true;
        }
        return false;
    }

    /**
     * SMS scheme
     */
    public static final String SCHEME_SMS = "sms:";

    /**
     * intent ' s scheme
     */
    public static final String SCHEME_INTENT = "intent://";

    public static boolean handleCommonLink(WeakReference<Activity> activityWeakReference, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.startsWith(WebView.SCHEME_TEL)
                || url.startsWith(SCHEME_SMS)
                || url.startsWith(WebView.SCHEME_MAILTO)
                || url.startsWith(WebView.SCHEME_GEO)) {
            try {
                Activity mActivity = null;
                if ((mActivity = activityWeakReference.get()) == null) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
                ignored.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static boolean handleIntentUrl(WeakReference<Activity> activityWeakReference, String intentUrl) {
        if (TextUtils.isEmpty(intentUrl)) {
            return false;
        }

        if (intentUrl.startsWith(SCHEME_INTENT)) {
            try {
                return lookup(activityWeakReference, intentUrl);
            } catch (ActivityNotFoundException ignored) {
                ignored.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private static boolean lookup(WeakReference<Activity> activityWeakReference, String url) {
        try {
            Intent intent;
            Activity mActivity = null;
            if ((mActivity = activityWeakReference.get()) == null) {
                return true;
            }
            PackageManager packageManager = mActivity.getPackageManager();
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            // 跳到该应用
            if (info != null) {
                mActivity.startActivity(intent);
                return true;
            }
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
        return false;
    }
}
