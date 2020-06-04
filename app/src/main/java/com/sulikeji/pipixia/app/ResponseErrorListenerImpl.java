package com.sulikeji.pipixia.app;

import android.content.Context;

import me.jessyan.armscomponent.commonui.util.ToastUtils;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import timber.log.Timber;

public class ResponseErrorListenerImpl implements ResponseErrorListener {
    @Override
    public void handleResponseError(Context context, Throwable t) {
        Timber.tag("Catch-Error").w(t.getMessage());
        //这里不光只能打印错误, 还可以根据不同的错误做出不同的逻辑处理
        //这里只是对几个常用错误进行简单的处理, 展示这个类的用法, 在实际开发中请您自行对更多错误进行更严谨的处理
        ApiException exception = ResponseErrorEngine.handleException(t);
        ToastUtils.normal(exception.getMsg());
    }
}
