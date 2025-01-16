package cn.jarlen.richcommon.jwebview.util;

import android.content.Context;
import android.text.TextUtils;

import androidx.collection.ArrayMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hkb on 2018/1/23.
 */

public class CookieUtils {

    private static final String SEPARATOR_TAGS = ",";
    private static final String SEPARATOR_VALUES = "=";
    private static final String COOIKE_FILE_NAME = "cooikes";

    /**
     * 更新存储Cookie
     *
     * @param context
     * @param appId     H5页面地址
     * @param accessKey 存储的KEY
     * @param values    存储的值
     */
    public static void saveOrUpdate(Context context, String appId, String accessKey, List<String> values) {
        Map<String, String> cookies = new HashMap<>(1);
        String data = ArraysUtils.joinerWith(values, SEPARATOR_TAGS);
        if (data != null) {
            cookies.put(accessKey, data);
        }
        save(appId, cookies);
    }

    /**
     * 更新存储Cookie
     *
     * @param context
     * @param appId   H5页面地址
     * @param key     存储的KEY
     * @param values  存储的值
     */
    public static void saveOrUpdate(Context context, String appId, String key, String values) {
        Map<String, String> cookies = getCookies(appId);
        cookies.put(key, values);
        save(appId, cookies);
    }

    /**
     * 取出Cookie
     *
     * @param keyId
     * @param key
     * @return
     */
    public static String getCookie( String keyId, String key) {
        return getCookies(keyId).get(key);
    }

    private static Map<String, String> getCookies(String appId) {
        Map<String, String> result = new ArrayMap<>();
        String cookie = SpUtils.getInstance(COOIKE_FILE_NAME).getString(appId,"");
        if (TextUtils.isEmpty(cookie)) {
            return result;
        }
        String[] items = cookie.split(SEPARATOR_VALUES);
        if (ArraysUtils.isArrayEmpty(items) || items.length != 2) {
            return result;
        }
        result.put(items[0], items[1]);
        return result;
    }

    private static void save(String appId, Map<String, String> cookies) {
        if (ArraysUtils.isMapEmpty(cookies)) {
            return;
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (Map.Entry<String, String> next : cookies.entrySet()) {
            stringBuffer.append(next.getKey());
            stringBuffer.append(SEPARATOR_VALUES);
            stringBuffer.append(next.getValue());
            stringBuffer.append(SEPARATOR_TAGS);
        }
       SpUtils.getInstance(COOIKE_FILE_NAME).put(appId, stringBuffer.toString());
    }
}
