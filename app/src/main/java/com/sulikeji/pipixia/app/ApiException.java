package com.sulikeji.pipixia.app;

public class ApiException extends Exception {
    private String msg;

    public ApiException(Throwable cause, String msg) {
        super(cause);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
