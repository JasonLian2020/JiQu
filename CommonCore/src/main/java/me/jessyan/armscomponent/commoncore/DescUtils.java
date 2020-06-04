package me.jessyan.armscomponent.commoncore;

import com.blankj.utilcode.util.Utils;

public class DescUtils {
    private DescUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 数据超过万时，显示*万，并保留1位小数（点赞、评论、分享、回复、获赞、粉丝、关注）
     * <P>例子：10000显示1.0W，18900显示1.8W，逢十进一</P>
     */
    private static String getBaseText(int count) {
        if (count >= 10000) {
            //现在是四舍五入
            return Utils.getApp().getString(R.string.public_count_text_limit, count / 10000f);
        } else {
            return Utils.getApp().getString(R.string.public_count_text_custom, count);
        }
    }

    /**
     * 点赞的描述
     *
     * @param likeCount 点数数
     */
    public static String getLikeText(int likeCount) {
        return getBaseText(likeCount);
    }

    /**
     * 评论的描述
     *
     * @param commentCount 评论数
     */
    public static String getCommentText(int commentCount) {
        return getBaseText(commentCount);
    }

    /**
     * 分享的描述
     *
     * @param shareCount 分享数
     */
    public static String getShareText(int shareCount) {
        return getBaseText(shareCount);
    }

    /**
     * 回复的描述
     *
     * @param replyCount 回复数
     */
    public static String getReplyText(int replyCount) {
        return getBaseText(replyCount);
    }
}
