package me.jason.imagepicker.utils;

import android.os.Looper;

public class ThreadUtils {
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
