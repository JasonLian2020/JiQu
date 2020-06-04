package me.jessyan.armscomponent.commoncore;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;

import me.jessyan.armscomponent.commoncore.user.mvp.model.entity.UserBean;
import me.jessyan.armscomponent.commonsdk.utils.sp.UserPreUtil;

public class ApplicationCache {

    private ApplicationCache() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    private static UserBean userBean;//用户信息
    private static String userToken;//登录token  需要鉴权接口， header传Authorization参数
    //key
    private static final String KEY_USER_BEAN = "KEY_USER_BEAN";
    private static final String KEY_USER_TOKEN = "KEY_USER_TOKEN";

    public static void saveUserBean(UserBean userBean) {
        ApplicationCache.userBean = userBean;
        String json = "";
        if (userBean != null) json = GsonUtils.toJson(userBean);
        UserPreUtil.save(KEY_USER_BEAN, json);
    }

    public static UserBean getUserBean() {
        if (userBean == null) {
            String json = UserPreUtil.get(KEY_USER_BEAN, "");
            if (!TextUtils.isEmpty(json)) userBean = GsonUtils.fromJson(json, UserBean.class);
        }
        return userBean;
    }

    public static void saveUserToken(String userToken) {
        ApplicationCache.userToken = userToken;
        UserPreUtil.save(KEY_USER_TOKEN, TextUtils.isEmpty(userToken) ? "" : userToken);
    }

    public static String getUserToken() {
        if (TextUtils.isEmpty(userToken)) {
            userToken = UserPreUtil.get(KEY_USER_TOKEN, "");
        }
        return userToken;
    }
}