package me.jessyan.armscomponent.commonui.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;

import es.dmoral.toasty.Toasty;

/**
 * Project Name:LiZhiWeiKe
 * Package Name:com.util.toast
 * Created by tom on 2018/1/22 11:43 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */
public class ToastUtils {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_IFNO = 2;
    private static final int TYPE_SUCCESS = 3;
    private static final int TYPE_ERROR = 4;
    private static final int TYPE_WARN = 5;

    private static void toasty(@NonNull CharSequence message, int type) {
        //1.取消适配
        AdaptScreenUtils.cancelAdapt();
        //2.显示Toast
        switch (type) {
            case TYPE_NORMAL:
                Toasty.normal(Utils.getApp(), message, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_IFNO:
                Toasty.info(Utils.getApp(), message, Toast.LENGTH_SHORT, false).show();
                break;
            case TYPE_SUCCESS:
                Toasty.success(Utils.getApp(), message, Toast.LENGTH_SHORT, false).show();
                break;
            case TYPE_ERROR:
                Toasty.error(Utils.getApp(), message, Toast.LENGTH_LONG, false).show();
                break;
            case TYPE_WARN:
                Toasty.warning(Utils.getApp(), message, Toast.LENGTH_LONG, false).show();
                break;
        }
        //3.恢复适配
        HANDLER.postDelayed(() -> AdaptScreenUtils.autoConvertDensityOfGlobal(), 200);
    }


    /**
     * 标准类型的taost
     * <p>默认不显示icon，显示时间为Toast.LENGTH_SHORT</p>
     */
    public static void normal(@NonNull CharSequence message) {
        toasty(message, TYPE_NORMAL);
    }

    /**
     * 信息类型的taost
     * <p>默认不显示icon，显示时间为Toast.LENGTH_SHORT</p>
     */
    public static void info(@NonNull CharSequence message) {
        toasty(message, TYPE_IFNO);
    }

    /**
     * 成功类型的taost
     * <p>默认不显示icon，显示时间为Toast.LENGTH_SHORT</p>
     */
    public static void success(@NonNull CharSequence message) {
        toasty(message, TYPE_SUCCESS);
    }


    /**
     * 错误类型的toast
     * <p>默认不显示icon，显示时间为Toast.LENGTH_LONG</p>
     */
    public static void error(@NonNull CharSequence message) {
        toasty(message, TYPE_ERROR);
    }


    /**
     * 警告类型的toast
     * <p>默认不显示icon，显示时间为Toast.LENGTH_LONG</p>
     */
    public static void warn(@NonNull CharSequence message) {
        toasty(message, TYPE_WARN);
    }

}
