package me.jessyan.armscomponent.commoncore;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.MediaBean;
import timber.log.Timber;

public class MediaUtils {
    public static void getVideoInfo(MediaBean mediaBean, OnGetVideoInfoListener listener) {
        //1.通过cursor获取
        MediaBean result = getVideoInfoByCursor(mediaBean);
        //判断有没获取成功
        if (result == null) {
            if (listener != null) listener.onCompleted(null);
        } else {
            if (result.getWidth() == 0 || result.getHeight() == 0) {
                //2.通过MediaMetadataRetriever获取
                getVideoInfoByRetriever(result);
            }
            if (listener != null) listener.onCompleted(result);
        }
    }

    public static void getVideoThumb(MediaBean mediaBean, OnGetVideoThumbListener listener) {
        //1.通过缓存获取
        String result = getVideoThumbByCache(mediaBean);
        if (result != null) {
            if (listener != null) listener.onCompleted(result);
            return;
        }
        //获取videoId
        if (mediaBean.getId() == -1) {
            mediaBean.setId(getVideoIdByCursor(mediaBean));
        }
        //2.通过cursor获取
        result = getVideoThumbByCursor(mediaBean);
        //判断有没获取成功
        if (result != null) {
            String destFilePath = getThumbPath(mediaBean.getPath());
            boolean copyFile = FileUtils.copyFile(result, destFilePath);
            if (listener != null) listener.onCompleted(copyFile ? destFilePath : result);
            return;
        }
        //3.通过Glide获取
        getVideoThumbByDownload(mediaBean, path -> {
            if (listener != null) listener.onCompleted(path);
        });
    }

    private static MediaBean getVideoInfoByCursor(MediaBean mediaBean) {
        Timber.tag("jason").d("getVideoInfoByCursor");
        MediaBean result = null;
        String mimeType = mediaBean.getMimeType();
        String srcPath = mediaBean.getPath();
        ContentResolver contentResolver = Utils.getApp().getContentResolver();
        Uri targetUrl = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(
                targetUrl,
                null,
                MediaStore.Video.Media.MIME_TYPE + "=? and " + MediaStore.Video.Media.DATA + "=?",
                new String[]{mimeType, srcPath},
                null);
        try {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                long duration = cursor.getLong(cursor.getColumnIndex("duration"));
                long width = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH));
                long height = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT));
                Timber.tag("jason").d("width = " + width);
                Timber.tag("jason").d("height = " + height);
                result = MediaBean.valueOf(id, srcPath, mediaBean.getContentUri(), mimeType, size, duration, width, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return result;
    }

    private static MediaBean getVideoInfoByRetriever(MediaBean result) {
        Timber.tag("jason").d("getVideoInfoByRetriever");
        try {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(result.getPath());
            String width = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String height = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String orientation = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            Timber.tag("jason").d("width = " + width);
            Timber.tag("jason").d("height = " + height);
            Timber.tag("jason").d("orientation = " + orientation);
            //事实上 MediaMetadataRetriever 这个类，他是无法灵活的判断出在不同方向上（横/竖屏）正确的宽高的，
            //在我不断的尝试中发现 MediaMetadataRetriever 它默认较长的一边是 宽，短的边是高，也就是扁长形的矩形。
            if ("90".equals(orientation)) {
                //竖屏
                result.setWidth(Long.valueOf(height));
                result.setHeight(Long.valueOf(width));
            } else {
                //横屏
                result.setWidth(Long.valueOf(width));
                result.setHeight(Long.valueOf(height));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static long getVideoIdByCursor(MediaBean mediaBean) {
        Timber.tag("jason").d("getVideoIdByCursor");
        long result = -1;
        String mimeType = mediaBean.getMimeType();
        String srcPath = mediaBean.getPath();
        ContentResolver contentResolver = Utils.getApp().getContentResolver();
        Uri targetUrl = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(
                targetUrl,
                null,
                MediaStore.Video.Media.MIME_TYPE + "=? and " + MediaStore.Video.Media.DATA + "=?",
                new String[]{mimeType, srcPath},
                null);
        try {
            while (cursor.moveToNext()) {
                result = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return result;
    }

    private static String getThumbPath(String srcPath) {
        String md5 = FileUtils.getFileMD5ToString(srcPath);
        String dir = SDCardUtils.getSDCardPathByEnvironment();
        if (TextUtils.isEmpty(dir)) dir = PathUtils.getInternalAppCachePath() + "/thumb";
        else dir = dir + File.separator + Utils.getApp().getPackageName() + "/thumb";
        return dir + File.separator + md5;
    }

    private static String getVideoThumbByCache(MediaBean mediaBean) {
        Timber.tag("jason").d("getVideoThumbByCache");
        File thumbFile = new File(getThumbPath(mediaBean.getPath()));
        if (thumbFile.exists()) return thumbFile.getAbsolutePath();
        else return null;
    }

    private static String getVideoThumbByCursor(MediaBean mediaBean) {
        Timber.tag("jason").d("getVideoThumbByCursor");
        String result = null;
        long videoId = mediaBean.getId();
        ContentResolver contentResolver = Utils.getApp().getContentResolver();
        Uri targetUrl = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(
                targetUrl,
                null,
                MediaStore.Video.Thumbnails.VIDEO_ID + "=?",
                new String[]{String.valueOf(videoId)},
                null);
        try {
            while (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return result;
    }

    @SuppressLint("CheckResult")
    private static void getVideoThumbByDownload(MediaBean mediaBean, @NonNull OnGetVideoThumbListener listener) {
        Timber.tag("jason").d("getVideoThumbByDownload");
        Glide.with(Utils.getApp())
                .asBitmap()
                .load(mediaBean.getContentUri())
                .apply(new RequestOptions().override(720, 1280))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        listener.onCompleted(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        boolean isSuccess = false;
                        File descFile = new File(getThumbPath(mediaBean.getPath()));
                        FileOutputStream os = null;
                        try {
                            os = new FileOutputStream(descFile);
                            resource.compress(Bitmap.CompressFormat.PNG, 80, os);
                            os.flush();
                            isSuccess = true;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        listener.onCompleted(isSuccess ? descFile.getAbsolutePath() : null);
                        return false;
                    }
                })
                .preload();
    }

    //==========================================interface
    public interface OnGetVideoInfoListener {
        void onCompleted(MediaBean result);
    }

    public interface OnGetVideoThumbListener {
        void onCompleted(String result);
    }
}
