package cn.com.startai.accessibility;


import cn.com.startai.common.utils.CLog;

import static cn.com.startai.common.CommonSDKInterface.TAG;

/**
 * Created by Robin on 2019/11/12.
 * 419109715@qq.com 彬影
 */
public class AccessibilityLog {
    public static boolean isLog = true;

    public static void v(String text) {
        if (isLog) {
            CLog.v(TAG, text);
        }
    }

    public static void d(String text) {
        if (isLog) {
            CLog.d(TAG, text);
        }
    }
}
