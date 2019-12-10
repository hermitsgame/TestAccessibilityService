package cn.com.startai.accessibility.bean;

/**
 * Created by Robin on 2019/12/10.
 * 419109715@qq.com 彬影
 */
public abstract class AAccessibilityBean {


    public abstract String[] getAccessiText();

    public abstract String[] getAccessiTextId();

    /**
     * 是否全词匹配，仅在 getAccessiText 有值时有效 ,
     * <p>
     * 默认为 true
     *
     * @return
     */
    public boolean isFullWordMatching() {
        return true;
    }
}
