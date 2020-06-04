package com.sulikeji.pipixia.app;

import android.net.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import retrofit2.HttpException;

public class ResponseErrorEngine {

    public static ApiException handleException(Throwable t) {
        String msg;
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        } else if (t instanceof ConnectException) {
            msg = "网络连接失败";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SSLException) {
            msg = "证书验证错误";
        } else if (t instanceof EOFException) {
            msg = "文件结束异常";
        } else if (t instanceof FileNotFoundException) {
            msg = "读写权限未打开";
        } else {
            msg = "未知错误";
        }
        return new ApiException(t, msg);
    }

    private static String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 401) {
            msg = "未授权";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}
