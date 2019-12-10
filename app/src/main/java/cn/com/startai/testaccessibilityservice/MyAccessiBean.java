package cn.com.startai.testaccessibilityservice;

import cn.com.startai.accessibility.bean.AAccessibilityBean;

/**
 * Created by Robin on 2019/12/10.
 * 419109715@qq.com 彬影
 */
public class MyAccessiBean extends AAccessibilityBean {
    @Override
    public String[] getAccessiText() {
        return new String[]{"允许", "Allow","安装","重新安装","打开应用","install","Reinstall"};
    }

    @Override
    public String[] getAccessiTextId() {
        return new String[0];
    }


}
