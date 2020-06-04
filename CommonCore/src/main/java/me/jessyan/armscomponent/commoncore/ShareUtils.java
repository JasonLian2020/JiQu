package me.jessyan.armscomponent.commoncore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.blankj.utilcode.util.Utils;

import java.util.List;

import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ContentBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;

import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_IMAGE;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_TEXT;
import static me.jessyan.armscomponent.commoncore.message.mvp.model.entity.FeedListBean.CONTENT_TYPE_VIDEO;

public class ShareUtils {
    public static String getShareTitle(List<ContentBean> contentList) {
        String title;
        if (contentList != null && !contentList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ContentBean contentBean : contentList) {
                if (ContentBean.TAG_AT.equals(contentBean.getTag())) {
                    stringBuilder.append("@" + contentBean.getUserName() + " ");
                } else {
                    stringBuilder.append(contentBean.getContent());
                }
            }
            title = stringBuilder.toString();
        } else {
            title = "这是一条有想法的内容";
        }
        return title;
    }

    public static String getShareText() {
        return "极趣：极其好玩才有趣。";
    }

    public static String getImageUrl(int contentType, List<ResourceBean> imageList, ResourceBean videoBean) {
        String imageUrl = null;
        switch (contentType) {
            case CONTENT_TYPE_TEXT:
                break;
            case CONTENT_TYPE_IMAGE:
                if (imageList != null && !imageList.isEmpty())
                    imageUrl = imageList.get(0).getPath();
                else
                    imageUrl = null;
                break;
            case CONTENT_TYPE_VIDEO:
                if (videoBean != null)
                    imageUrl = videoBean.getThumbnail();
                else
                    imageUrl = null;
                break;
        }
        return imageUrl;
    }

    public static Bitmap getImageData(int contentType) {
        Bitmap imageData = null;
        switch (contentType) {
            case CONTENT_TYPE_TEXT:
                imageData = BitmapFactory.decodeResource(Utils.getApp().getResources(), R.mipmap.ic_launcher);
                break;
            case CONTENT_TYPE_IMAGE:
                break;
            case CONTENT_TYPE_VIDEO:
                break;
        }
        return imageData;
    }
}
