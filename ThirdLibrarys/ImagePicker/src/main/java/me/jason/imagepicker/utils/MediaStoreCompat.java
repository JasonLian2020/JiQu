/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jason.imagepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.jason.imagepicker.internal.entity.CaptureStrategy;

public class MediaStoreCompat {

    private final WeakReference<Activity> mContext;
    private final WeakReference<Fragment> mFragment;
    private CaptureStrategy mCaptureStrategy;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;

    public MediaStoreCompat(Activity activity) {
        mContext = new WeakReference<>(activity);
        mFragment = null;
    }

    public MediaStoreCompat(Activity activity, Fragment fragment) {
        mContext = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    /**
     * Checks whether the device has a camera feature or not.
     *
     * @param context a context to check for camera feature.
     * @return true if the device has a camera feature. false otherwise.
     */
    public static boolean hasCameraFeature(Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void setCaptureStrategy(CaptureStrategy strategy) {
        mCaptureStrategy = strategy;
    }

    public void dispatchImageCapture(Context context, int requestCode) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mCurrentPhotoUri = FileProvider.getUriForFile(mContext.get(), mCaptureStrategy.authority, photoFile);
                    captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    mCurrentPhotoUri = Uri.fromFile(photoFile);
                }
                //设置拍照后图片保存的位置
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                //设置图片保存的格式
                captureIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                if (mFragment != null) {
                    mFragment.get().startActivityForResult(captureIntent, requestCode);
                } else {
                    mContext.get().startActivityForResult(captureIntent, requestCode);
                }
            }
        }
    }

    public void dispatchVideoCapture(Context context, int requestCode) {
        Intent captureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (captureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createVideoFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mCurrentPhotoUri = FileProvider.getUriForFile(mContext.get(), mCaptureStrategy.authority, photoFile);
                    captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    mCurrentPhotoUri = Uri.fromFile(photoFile);
                }
                //设置拍照后图片保存的位置
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                //设置图片保存的格式
//                captureIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                //设置拍摄的质量
                captureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                if (mFragment != null) {
                    mFragment.get().startActivityForResult(captureIntent, requestCode);
                } else {
                    mContext.get().startActivityForResult(captureIntent, requestCode);
                }
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir;
        if (mCaptureStrategy.isPublic) {
            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!storageDir.exists()) storageDir.mkdirs();
        } else {
            storageDir = mContext.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        if (mCaptureStrategy.directory != null) {
            storageDir = new File(storageDir, mCaptureStrategy.directory);
            if (!storageDir.exists()) storageDir.mkdirs();
        }

        // Avoid joining path components manually
        File tempFile = new File(storageDir, imageFileName);

        // Handle the situation that user's external storage is not ready
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }

        return tempFile;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("VID_%s.mp4", timeStamp);
        File storageDir;
        if (mCaptureStrategy.isPublic) {
            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            if (!storageDir.exists()) storageDir.mkdirs();
        } else {
            storageDir = mContext.get().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        }
        if (mCaptureStrategy.directory != null) {
            storageDir = new File(storageDir, mCaptureStrategy.directory);
            if (!storageDir.exists()) storageDir.mkdirs();
        }

        // Avoid joining path components manually
        File tempFile = new File(storageDir, imageFileName);

        // Handle the situation that user's external storage is not ready
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }

        return tempFile;
    }

    public Uri getCurrentPhotoUri() {
        return mCurrentPhotoUri;
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }
}
