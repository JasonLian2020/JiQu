package me.jessyan.armscomponent.commoncore;

import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;

public class ConvertUtils {
    private ConvertUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    private static Gson getGson() {
        return ArmsUtils.obtainAppComponentFromContext(Utils.getApp()).gson();
    }

    /**
     * 转换字段{@link FeedListBean#more}
     */
    public static void convertFieldMore(FeedListBean bean) {
        if (bean == null || bean.getMore() == null) return;
        switch (bean.getContentType()) {
            case FeedListBean.CONTENT_TYPE_IMAGE:
                Gson gson1 = getGson();
                try {
                    List<ResourceBean> imageList = gson1.fromJson(gson1.toJson(bean.getMore()), new TypeToken<List<ResourceBean>>() {
                    }.getType());
                    bean.setImageList(imageList);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case FeedListBean.CONTENT_TYPE_VIDEO:
                Gson gson2 = getGson();
                try {
                    ResourceBean resourceBean = gson2.fromJson(gson2.toJson(bean.getMore()), ResourceBean.class);
                    bean.setVideoBean(resourceBean);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 转换字段{@link FeedListBean#toParentInfo}
     */
    public static void convertFieldToParent(FeedListBean bean) {
        if (bean == null) return;
        convertFieldMore(bean.getToParentInfo());
    }
}
