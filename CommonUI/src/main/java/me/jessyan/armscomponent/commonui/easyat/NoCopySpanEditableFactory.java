package me.jessyan.armscomponent.commonui.easyat;

import android.text.Editable;
import android.text.NoCopySpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

public class NoCopySpanEditableFactory extends Editable.Factory {
    private NoCopySpan span;

    public NoCopySpanEditableFactory(NoCopySpan span) {
        this.span = span;
    }

    @Override
    public Editable newEditable(CharSequence source) {
        SpannableStringBuilder builder = SpannableStringBuilder.valueOf(source);
        builder.setSpan(span, 0, source.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }
}
