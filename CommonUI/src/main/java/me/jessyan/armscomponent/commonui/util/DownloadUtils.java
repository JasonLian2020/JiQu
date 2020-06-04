package me.jessyan.armscomponent.commonui.util;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.PathUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

public class DownloadUtils {

    public static BaseDownloadTask downloadVideo(String url, @NonNull FileDownloadListener listener) {
        BaseDownloadTask downloadTask = FileDownloader.getImpl()
                .create(url)
                .setPath(getVideoSavePath(url))
                .setAutoRetryTimes(1)
                .setListener(listener);
        downloadTask.start();
        return downloadTask;
    }

    public static String getVideoSavePath(String url) {
        //路径->/storage/emulated/0/DCIM/jiqu/XXX.mp4
        String directory = getVideoSaveDirectory();
        File file = new File(directory);
        //没有创建就先创建
        if (!file.exists()) file.mkdirs();
        return directory + File.separator + EncryptUtils.encryptMD5ToString(url) + ".mp4";
    }

    private static String getVideoSaveDirectory() {
        //路径->/storage/emulated/0/DCIM/jiqu
        return PathUtils.getExternalDcimPath() + "/jiqu";
    }
}
