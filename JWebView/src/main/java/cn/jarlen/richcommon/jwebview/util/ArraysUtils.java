package cn.jarlen.richcommon.jwebview.util;

import android.text.TextUtils;

import androidx.collection.LongSparseArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by jarlen on 2017/11/29.
 */

public class ArraysUtils {

    private static final char[] bcdLookup = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};
    /**
     * SQL语句占位符支持最大数,超过这个需要分批执行
     */
    public static final int MAX_SQL_PARAM_COUNT = 1000;

    /**
     * bytes转换成十六进制字符串
     *
     * @param b 待转换的bytes数组
     * @return 对应的十六进制字符串
     */
    public static String bytes2Hex(byte[] b) {
        StringBuilder s = new StringBuilder(b.length * 2);
        for (int n = 0; n < b.length; n++) {
            s.append(bcdLookup[(b[n] >>> 4) & 0x0f]);
            s.append(bcdLookup[b[n] & 0x0f]);
        }
        return s.toString();
    }


    /**
     * 十六进制字符串转换成 bytes
     *
     * @param src 对应的十六进制字符串
     * @return 转换的bytes数组
     * @throws NumberFormatException
     */
    public static byte[] hex2Bytes(String src) throws NumberFormatException {
        byte[] bytes = new byte[src.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(src.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * 泛型方法， 按照 limit的限制拆分 List<T>
     *
     * @param list  待拆分的list
     * @param limit 拆分的不长
     * @param <T>   泛型参数
     * @return 拆分后的列表
     */
    public static <T> List<List<T>> subListByLimit(List<T> list, int limit) {
        if (isListEmpty(list)) {
            return null;
        }

        if (limit <= 0) {
            throw new IllegalArgumentException("subList limit is invalid:" + limit);
        }
        List<List<T>> wrapList = new ArrayList<>();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(new ArrayList<>(list.subList(count, (count + limit > list.size() ? list.size() : count + limit))));
            count += limit;
        }
        return wrapList;
    }

    /**
     * long类型的list转成long数组
     *
     * @param list 待转换的列表
     * @return 转换后的long数据
     */
    public static long[] list2Array(List<Long> list) {
        if (isListEmpty(list)) {
            return null;
        }
        long[] res = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    /**
     * long类型的list转成Long数组
     *
     * @param list 待转换的列表
     * @return 转换后的Long数据
     */
    public static Long[] list2LongArray(List<Long> list) {
        if (isListEmpty(list)) {
            return null;
        }
        Long[] res = new Long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    /**
     * long数组 转换为 List<Long>
     *
     * @param list 待转换的long 数组
     * @return 转换后的list<Long>
     */
    public static List<Long> array2List(long[] list) {
        if (list.length < 0) {
            return null;
        }
        List<Long> res = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            res.add(list[i]);
        }
        return res;
    }

    /**
     * 泛型方法， 数组 转换为 list
     *
     * @param list 待转换的泛型数组
     * @param <T>  泛型参数
     * @return 转换后的List<T>
     */
    public static <T> List<T> array2List(T[] list) {
        if (list.length < 0) {
            return null;
        }
        return Arrays.asList(list);
    }

    public static boolean isListEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isLongSparseArrayEmpty(LongSparseArray collection) {
        return collection == null || collection.size() == 0;
    }

    public static int getArraySize(Collection collection) {
        return isListEmpty(collection) ? 0 : collection.size();
    }

    /**
     * 判断Map是否为空
     *
     * @param map 待判断map对象
     * @return true，为空。 false 不为空
     * @作者 hkb
     * @since 2014年9月26日 下午2:01:21
     */
    public static boolean isMapEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断String类型的数组是否为空
     *
     * @param values
     * @return
     */
    public static <T> boolean isArrayEmpty(T[] values) {
        return values == null || values.length <= 0;
    }
    /**
     * 判断String类型的数组是否为空
     *
     * @param values
     * @return
     */
    public static boolean isArrayEmpty(long[] values) {
        return values == null || values.length <= 0;
    }

    /**
     * 将集合(Long)转换成SQL语句条件字符串
     *
     * @param list Long集合
     * @return 转换成SQL语句条件字符串
     */
    public static String joinerWith(List<Long> list) {
        if (isListEmpty(list)) {
            return null;
        }
        String result = "";
        for (Long aLong : list) {
            if (aLong != null) {
                result += TextUtils.isEmpty(result) ? String.valueOf(aLong) : "," + aLong;
            }
        }
        return result;
    }

    /**
     * 将集合(Long)转换成SQL语句条件字符串
     *
     * @param list Long集合
     * @return 转换成SQL语句条件字符串
     */
    public static String joinIntegerWithSeparator(List<Integer> list) {
        if (isListEmpty(list)) {
            return "";
        }
        String result = "";
        for (Integer value : list) {
            if (value == null) {
                continue;
            }
            result += TextUtils.isEmpty(result) ? String.valueOf(value) : "," + value;
        }
        return result;
    }

    /**
     * 转换 固定格式的字符串 为 List<Integer>
     *
     * @param content 以"," 分割的字符串
     * @return 分割后的 List<Integer>
     */
    public static List<Integer> listIntegerWithSeparator(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        String[] contentArray = content.split(",");
        if (contentArray == null || contentArray.length < 1) {
            return null;
        }
        List<Integer> valueList = new ArrayList<>();
        int count = contentArray.length;
        for (int index = 0; index < count; index++) {
            int value = Integer.valueOf(contentArray[index]);
            valueList.add(value);
        }
        return valueList;
    }

    /**
     * 将集合转换成SQL语句条件字符串
     *
     * @param list Long集合
     * @return 转换成SQL语句条件字符串
     */
    public static <T> String joinerWith(List<T> list, String separator) {
        if (isListEmpty(list)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (T aLong : list) {
            if (aLong != null) {
                result.append(TextUtils.isEmpty(result) ? aLong : separator + aLong);
            }
        }
        return result.toString();
    }
    /**
     * 将数组转换成SQL语句条件字符串
     *
     * @param list Long集合
     * @return 转换成SQL语句条件字符串
     */
    public static <T> String joinerWith(T[] list, String separator) {
        if (isArrayEmpty(list)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (T aLong : list) {
            if (aLong != null) {
                result.append(TextUtils.isEmpty(result) ? aLong : separator + aLong);
            }
        }
        return result.toString();
    }

    /**
     * 深拷贝List
     *
     * @param src 待拷贝的list
     * @param <T> 反省参数
     * @return 深度拷贝后的结果
     */
    public static <T> List<T> deepCopy(List<T> src) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);


            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 泛型方法，对泛型参数进行深拷贝
     *
     * @param src 待拷贝的对象
     * @param <T> 泛型参数
     * @return 深度拷贝后的结果
     */
    public static <T> T deepCopy(T src) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);


            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            T dest = (T) in.readObject();
            return dest;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换 固定格式的字符串 为 List<Long>
     *
     * @param content 以"," 分割的字符串
     * @return 分割后的 List<Long>
     */
    public static List<Long> getParamsTolist(String str) {
        final List<Long> appIds = new ArrayList<>();
        try {
            String[] appIdsStr = str.split(",");
            for (String appId : appIdsStr) {
                appIds.add(Long.valueOf(appId));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return appIds;
    }

    /**
     * 判断数组索引是否越界
     *
     * @param index
     * @param data
     * @param <T>
     * @return true为越界，false为未越界。
     */
    public static <T> boolean isIndexOutOfBounds(int index, List<T> data) {
        if (ArraysUtils.isListEmpty(data)) {
            return false;
        }
        return index >= data.size() || index < 0;
    }
}
