package cn.com.startai.accessibility.bean;

/**
 * Created by Robin on 2019/12/10.
 * 419109715@qq.com 彬影
 */
public class PermissionAccessibilityBean extends AAccessibilityBean {

    @Override
    public String[] getAccessiText() {
        return null;
    }

    @Override
    public String[] getAccessiTextId() {
        return new String[]{"com.android.packageinstaller:id/permission_allow_button"};
    }


}
