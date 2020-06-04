package me.jessyan.armscomponent.commonui.easyat.watcher;

import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;

import me.jessyan.armscomponent.commonui.easyat.span.DataBindingSpan;

public class SelectionSpanWatcher implements SpanWatcher {
    private int selStart = 0;
    private int selEnd = 0;

    @Override
    public void onSpanAdded(Spannable text, Object what, int start, int end) {

    }

    @Override
    public void onSpanRemoved(Spannable text, Object what, int start, int end) {

    }

    @Override
    public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend) {
        if (what.equals(Selection.SELECTION_END) && selEnd != nstart) {
            selEnd = nstart;
            DataBindingSpan[] spans = text.getSpans(nstart, nend, DataBindingSpan.class);
            if (spans == null || spans.length == 0) return;
            DataBindingSpan span = spans[0];
            int spanStart = text.getSpanStart(span);
            int spanEnd = text.getSpanEnd(span);
            int index;
            if (Math.abs(selEnd - spanEnd) > Math.abs(selEnd - spanStart))
                index = spanStart;
            else
                index = spanEnd;
            Selection.setSelection(text, Selection.getSelectionStart(text), index);
        }

        if (what.equals(Selection.SELECTION_START) && selStart != nstart) {
            selStart = nstart;
            DataBindingSpan[] spans = text.getSpans(nstart, nend, DataBindingSpan.class);
            if (spans == null || spans.length == 0) return;
            DataBindingSpan span = spans[0];
            int spanStart = text.getSpanStart(span);
            int spanEnd = text.getSpanEnd(span);
            int index;
            if (Math.abs(selStart - spanEnd) > Math.abs(selStart - spanStart))
                index = spanStart;
            else
                index = spanEnd;
            Selection.setSelection(text, index, Selection.getSelectionEnd(text));
        }
    }
}
