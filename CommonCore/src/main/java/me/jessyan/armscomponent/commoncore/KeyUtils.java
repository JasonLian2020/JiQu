package me.jessyan.armscomponent.commoncore;

public class KeyUtils {
    /**
     * 时间戳(毫秒，13位) + 随机6位数
     */
    public static String getQiniuKey() {
        return String.valueOf(System.currentTimeMillis()) + (int) ((Math.random() * 9 + 1) * 100000);
    }

    /**
     * 评论key，用于临时标记
     */
    public static String getCommentKey() {
        return "comment_" + System.currentTimeMillis();
    }
}
