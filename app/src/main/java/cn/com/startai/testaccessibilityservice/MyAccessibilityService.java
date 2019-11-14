//package cn.com.startai.testaccessibilityservice;
//
//import android.accessibilityservice.AccessibilityService;
//import android.view.accessibility.AccessibilityEvent;
//import android.view.accessibility.AccessibilityNodeInfo;
//
//import java.util.List;
//import java.util.Locale;
//
//import cn.com.startai.common.utils.CLog;
//import cn.com.startai.common.utils.TAndL;
//
//import static cn.com.startai.common.CommonSDKInterface.TAG;
//
//public class MyAccessibilityService extends AccessibilityService {
//
//
//    public static final String PACKAGE_INSTALLER = "com.android.packageinstaller";
//
//    @Override
//    public void onAccessibilityEvent(AccessibilityEvent event) {
//
//
//        String s = event.toString();
//        CLog.v(TAG,s);
//        CharSequence packageName = event.getPackageName();
//        if (PACKAGE_INSTALLER.equals(packageName)) {
//            TAndL.L("发现 packageName = " + packageName);
//            AccessibilityNodeInfo rootWindowInfo = getRootInActiveWindow();
//            if (tryClickByPermission(rootWindowInfo)) {
//
//            } else if (tryClickByInstall(rootWindowInfo)) {
//
//            }
//        }
//
//    }
//
//    @Override
//    public void onInterrupt() {
//
//    }
//
//
//    /**
//     * 寻找关键按键 并执行点击事件
//     *
//     * @param rootInActiveWindow
//     * @param textValue
//     * @return
//     */
//    private boolean findTextAndPerformAction(AccessibilityNodeInfo rootInActiveWindow, String textValue) {
//        TAndL.L("findTextAndPerformAction -------------------------------");
//        List<AccessibilityNodeInfo> accessibilityNodeInfosByText = rootInActiveWindow.findAccessibilityNodeInfosByText(textValue);
//        for (AccessibilityNodeInfo nodeInfo : accessibilityNodeInfosByText) {
//            TAndL.L("nodeInfo = " + nodeInfo);
//            String text = nodeInfo.getText().toString();
//            String classNameNode = nodeInfo.getClassName().toString();
//            TAndL.L("找到包含关键字'" + textValue + "'的节点:" + text);
//
//            if ("android.widget.Button".equals(classNameNode)) {
//                TAndL.L("执行点击事件");
//                return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            } else {
//                TAndL.L("不是按键");
//            }
//        }
//        TAndL.L("没有找到包含关键字的节点");
//        return false;
//    }
//
//
//    /**
//     * 尝试自动点击授权
//     */
//    private boolean tryClickByPermission(AccessibilityNodeInfo rootview) {
//        if (rootview == null) {
//            return false;
//        }
//
//        Locale locale = getResources().getConfiguration().locale;
//        String country = locale.getCountry();
//        String text = "Allow";
//        TAndL.L("country = " + country);
//        if ("CN".equalsIgnoreCase(country)) {
//            text = "允许";
//        }
//        TAndL.L("尝试寻找授权关键字：'" + text + "'，并执行点击");
//        return findTextAndPerformAction(rootview, text);
//
//    }
//
//    /**
//     * 尝试自动点击安装程序
//     *
//     * @param rootview
//     * @return
//     */
//    private boolean tryClickByInstall(AccessibilityNodeInfo rootview) {
//        return false;
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        TAndL.L("MyAccessibilityService.onDestroy");
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        TAndL.L("MyAccessibilityService.onCreate");
//    }
//
//    @Override
//    protected void onServiceConnected() {
//        super.onServiceConnected();
//        TAndL.L("MyAccessibilityService.onServiceConnected");
//
//    }
//}