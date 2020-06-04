package me.jessyan.armscomponent.commonservice;

public interface PreferencesHub {
    /**
     * 组名
     */
    String APP = "/App";//宿主 App 组件
    String HOME = "/Home";//首页 Tab 组件

    /**
     * Key，用于获取value
     */
    String KEY = "/Key";

    /**
     * 首页 Tab 分组
     */
    String HOME_KEY_CATEGORYLIST = HOME + KEY + "/CategoryList";
}
