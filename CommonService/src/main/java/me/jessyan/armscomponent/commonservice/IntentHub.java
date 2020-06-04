package me.jessyan.armscomponent.commonservice;

public interface IntentHub {
    /**
     * 组名
     */
    String APP = "/App";//宿主 App 组件

    /**
     * Key，用于获取value
     */
    String KEY = "/Key";

    /**
     * 选中的页面
     */
    String CHOOSETAB = "/ChooseTab";

    /**
     * 宿主 App 分组
     */
    String APP_KEY_CHOOSETAB_HOME = APP + KEY + CHOOSETAB + "/Home";//首页 Tab
    String APP_KEY_CHOOSETAB_PUBLISH = APP + KEY + CHOOSETAB + "/Publish";//发帖 Tab
    String APP_KEY_CHOOSETAB_MINE = APP + KEY + CHOOSETAB + "/Mine";//我的 Tab
}
