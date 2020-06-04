package me.jessyan.armscomponent.commonui.easyat;

import android.text.Editable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.armscomponent.commonui.easyat.span.DataBindingSpan;

public class FormatHelper {

    public static String editableToJson(Editable editable, Class<? extends DataBindingSpan> _class) {
        DataBindingSpan[] spans = editable.getSpans(0, editable.length(), _class);
        if (spans == null || spans.length == 0) {
            String value = editable.toString();
            if (TextUtils.isEmpty(value))
                return "";
            else {
                List<DataBindingSpan> results = new ArrayList<>();
                DataBindingSpan contentBean = getContentBean(value, _class);
                if (contentBean != null) results.add(contentBean);
                return new Gson().toJson(results);
            }
        } else {
            int length = editable.length();
            int lastSpanEnd = 0;
            List<DataBindingSpan> results = new ArrayList<>();
            for (int i = 0; i < spans.length; i++) {
                DataBindingSpan span = spans[i];
                int spanStart = editable.getSpanStart(span);
                int spanEnd = editable.getSpanEnd(span);
                if (i == 0) {
                    if (spanStart != 0) {
                        char[] dest = new char[spanStart - 0];
                        editable.getChars(0, spanStart, dest, 0);
                        String value = String.valueOf(dest);
                        DataBindingSpan contentBean = getContentBean(value, _class);
                        if (contentBean != null) results.add(contentBean);
                    }
                } else {
                    if (spanStart != lastSpanEnd) {
                        char[] dest = new char[spanStart - lastSpanEnd];
                        editable.getChars(lastSpanEnd, spanStart, dest, 0);
                        String value = String.valueOf(dest);
                        DataBindingSpan contentBean = getContentBean(value, _class);
                        if (contentBean != null) results.add(contentBean);
                    }
                }
                //拼接用户
                span.setBeanTag("at");
                results.add(span);
                //最后记录上一次结束的位置
                lastSpanEnd = spanEnd;
                //结束的时候在判断下后面还有没
                if (i == spans.length - 1 && spanEnd != length) {
                    char[] dest = new char[length - spanEnd];
                    editable.getChars(spanEnd, length, dest, 0);
                    String value = String.valueOf(dest);
                    DataBindingSpan contentBean = getContentBean(value, _class);
                    if (contentBean != null) results.add(contentBean);
                }

            }
            return new Gson().toJson(results);
        }
    }

    private static DataBindingSpan getContentBean(String value, Class<? extends DataBindingSpan> _class) {
        try {
            DataBindingSpan contentBean = _class.newInstance();
            contentBean.setBeanTag("text");
            contentBean.setBeanContent(value);
            return contentBean;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
