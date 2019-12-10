
package cn.com.startai.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.graphics.Rect;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import cn.com.startai.accessibility.bean.AAccessibilityBean;
import cn.com.startai.accessibility.bean.PermissionAccessibilityBean;
import cn.com.startai.common.utils.CAppUtils;
import cn.com.startai.common.utils.CShellUtils;
import cn.com.startai.common.utils.CThreadPoolUtils;


public class StartAIAccessibilityService extends AccessibilityService {


    private static AAccessibilityBean bean = new PermissionAccessibilityBean();


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {


        String s = event.toString();
        AccessibilityLog.v(s);
        if (bean != null) {
            AccessibilityNodeInfo rootWindowInfo = getRootInActiveWindow();
            if (rootWindowInfo == null) {
                return;
            }

            String[] accessiTexts = bean.getAccessiText();
            String[] accessiTextIds = bean.getAccessiTextId();

            boolean isClick = false;
            if (accessiTexts != null && accessiTexts.length > 0) {
                for (String accessiText : accessiTexts) {
                    isClick = findViewAndPerformClick(findByText(rootWindowInfo, accessiText), accessiText);
                }
            }

            if (!isClick && accessiTextIds != null && accessiTextIds.length > 0) {
                for (String accessiTextId : accessiTextIds) {
                    isClick = findViewAndPerformClick(findById(rootWindowInfo, accessiTextId), accessiTextId);
                }
            }
            if (isClick) {
                AccessibilityLog.v("end..............");
            }
        }

    }

    @Override
    public void onInterrupt() {

    }


    /**
     * 寻找关键view并执行点击
     *
     * @param str
     * @return
     */
    private boolean findViewAndPerformClick(List<AccessibilityNodeInfo> list, String str) {

        if (list == null || list.size() == 0) {
            AccessibilityLog.v("没有找到关键字或关键id");
        } else {
            for (AccessibilityNodeInfo nodeInfo : list) {
                String text = nodeInfo.getText().toString();
                AccessibilityLog.d("text:" + text);
                AccessibilityLog.d("找到nodeInfo:" + nodeInfo);
                if (!nodeInfo.isClickable()) {
                    AccessibilityLog.d("此nodeInfo不可点击");
                    continue;
                }
                if (bean.getAccessiText() != null && bean.getAccessiText().length > 0) {
                    if (bean.isFullWordMatching()) {
                        if (!text.equals(str)) {
                            AccessibilityLog.d("只部分包含关键字");
                            continue;
                        }
                    }
                }
                AccessibilityLog.d("执行点击事件");
                if (nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                    AccessibilityLog.d("执行成功");
                    return true;
                } else {
                    if (forceClick(nodeInfo)) {
                        AccessibilityLog.d("执行成功");
                    } else {
                        AccessibilityLog.d("执行失败");
                    }

                }
            }
        }
        return false;
    }

    private boolean forceClick(AccessibilityNodeInfo nodeInfo) {
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        AccessibilityLog.d("forceClick:" + rect.left + " " + rect.top + " " + rect.right + "  " + rect.bottom);
        int x = (rect.left + rect.right) / 2;
        int y = (rect.top + rect.bottom) / 2;
        String cmd = "input tap " + x + " " + y;

        AccessibilityLog.d("forceClick cmd: " + cmd);
        CShellUtils.CommandResult commandResult = CShellUtils.execCmd(cmd, CAppUtils.isAppRoot(), true);

        AccessibilityLog.d("commandResult: " + commandResult);
        return commandResult.result == 0;
    }


    /**
     * 通过text寻找关键按键
     *
     * @param rootInActiveWindow
     * @param textValue
     * @return
     */
    private List<AccessibilityNodeInfo> findByText(AccessibilityNodeInfo rootInActiveWindow, String textValue) {
        return rootInActiveWindow.findAccessibilityNodeInfosByText(textValue);
    }


    /**
     * 通过id寻找关键按键
     *
     * @param rootInActiveWindow
     * @param viewId
     * @return
     */
    private List<AccessibilityNodeInfo> findById(AccessibilityNodeInfo rootInActiveWindow, String viewId) {
        return rootInActiveWindow.findAccessibilityNodeInfosByViewId(viewId);
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

    public static void initAccessbilityServiceWithBean(Context context, AAccessibilityBean bean) {
        StartAIAccessibilityService.bean = bean;
        initAccessbilityService(context);
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