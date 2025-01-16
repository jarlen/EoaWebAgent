package cn.jarlen.richcommon.jwebview.client;

import android.webkit.JavascriptInterface;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author jarlen
 */
public abstract class AbsJsInterfaceHolder implements JsInterfaceHolder {

    @Override
    public boolean checkObject(Object v) {
        boolean tag = false;
        Class clazz = v.getClass();
        Method[] mMethods = clazz.getMethods();
        for (Method mMethod : mMethods) {
            Annotation[] mAnnotations = mMethod.getAnnotations();
            for (Annotation mAnnotation : mAnnotations) {
                if (mAnnotation instanceof JavascriptInterface) {
                    tag = true;
                    break;
                }
            }
            if (tag) {
                break;
            }
        }
        return tag;
    }
}
