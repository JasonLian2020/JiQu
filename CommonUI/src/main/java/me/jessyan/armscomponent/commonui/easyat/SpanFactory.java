package me.jessyan.armscomponent.commonui.easyat;

import android.text.Spannable;
import android.text.SpannableString;

public class SpanFactory {
    public static Spannable newSpannable(CharSequence source, Object span) {
        SpannableString spannableString = SpannableString.valueOf(source);
        spannableString.setSpan(span, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
