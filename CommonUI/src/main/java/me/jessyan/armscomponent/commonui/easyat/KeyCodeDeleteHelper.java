package me.jessyan.armscomponent.commonui.easyat;

import android.text.Selection;
import android.text.Spannable;

import me.jessyan.armscomponent.commonui.easyat.span.DataBindingSpan;

public class KeyCodeDeleteHelper {

    public static Boolean onDelDown(Spannable text) {
        int selectionStart = Selection.getSelectionStart(text);
        int selectionEnd = Selection.getSelectionEnd(text);
        DataBindingSpan[] spans = text.getSpans(selectionStart, selectionEnd, DataBindingSpan.class);
        if (spans == null || spans.length == 0) return false;
        for (int i = 0; i < spans.length; i++) {
            DataBindingSpan span = spans[i];
            if (text.getSpanEnd(span) == selectionStart) {
                if (selectionStart == selectionEnd) {
                    int spanStart = text.getSpanStart(span);
                    int spanEnd = text.getSpanEnd(span);
                    Selection.setSelection(text, spanStart, spanEnd);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
