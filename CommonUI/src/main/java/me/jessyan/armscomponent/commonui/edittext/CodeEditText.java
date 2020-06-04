package me.jessyan.armscomponent.commonui.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.armscomponent.commonui.R;

public class CodeEditText extends FrameLayout {
    private List<EditText> mEditList = new ArrayList<>();
    private List<View> mLineList = new ArrayList<>();
    //view
    private EditText etFirstNumber;
    private EditText etSecondNumber;
    private EditText etThirdNumber;
    private EditText etFourthNumber;
    private View etFirstLine;
    private View etSecondLine;
    private View etThirdLine;
    private View etFourthLine;
    //标记
    private boolean isBanCallback = false;

    public CodeEditText(Context context) {
        this(context, null);
    }

    public CodeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.public_layout_auth_code, this, false);
        addView(rootView);
        //EditText
        etFirstNumber = rootView.findViewById(R.id.etFirstNumber);
        etSecondNumber = rootView.findViewById(R.id.etSecondNumber);
        etThirdNumber = rootView.findViewById(R.id.etThirdNumber);
        etFourthNumber = rootView.findViewById(R.id.etFourthNumber);
        //Line
        etFirstLine = rootView.findViewById(R.id.etFirstLine);
        etSecondLine = rootView.findViewById(R.id.etSecondLine);
        etThirdLine = rootView.findViewById(R.id.etThirdLine);
        etFourthLine = rootView.findViewById(R.id.etFourthLine);
        //EditList
        mEditList.add(etFirstNumber);
        mEditList.add(etSecondNumber);
        mEditList.add(etThirdNumber);
        mEditList.add(etFourthNumber);
        //LineList
        mLineList.add(etFirstLine);
        mLineList.add(etSecondLine);
        mLineList.add(etThirdLine);
        mLineList.add(etFourthLine);
        //默认不给点击
        etFirstNumber.setEnabled(false);
        etSecondNumber.setEnabled(false);
        etThirdNumber.setEnabled(false);
        etFourthNumber.setEnabled(false);
        //监听输入文本变化
        initTextChangedListener();
        //监听删除
        initKeyListener();
    }

    private void initTextChangedListener() {
        etFirstNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textChanged();
                nextFocus(0);
            }
        });
        etSecondNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textChanged();
                nextFocus(1);
            }
        });
        etThirdNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textChanged();
                nextFocus(2);
            }
        });
        etFourthNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textChanged();
                textCompleted(s);
            }
        });
    }

    private void initKeyListener() {
        etSecondNumber.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    preFocus(1);
                    return true;
                }
                return false;
            }
        });
        etThirdNumber.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    preFocus(2);
                    return true;
                }
                return false;
            }
        });
        etFourthNumber.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    int length = ((EditText) v).getText().length();
                    if (length < 1) {
                        //没数据
                        preFocus(3);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void preFocus(int curPosition) {
        if (isBanCallback) return;
        //edit
        EditText curEditText = getEditText(curPosition);
        EditText preEditText = getEditText(curPosition - 1);
        if (preEditText != null) {
            // 上一个
            preEditText.setText("");
            preEditText.setEnabled(true);
            preEditText.requestFocus();
            preEditText.setCursorVisible(true);
        }
        if (curEditText != null) {
            // 当前
            curEditText.setEnabled(false);
            curEditText.setCursorVisible(false);
        }
        //line
        View curLine = getLine(curPosition);
        View preLine = getLine(curPosition - 1);
        if (curLine != null) {
            // 当前
            curLine.setSelected(false);
        }
        if (preLine != null) {
            // 下一个
            preLine.setSelected(false);
        }
    }

    private void nextFocus(int curPosition) {
        if (isBanCallback) return;
        //edit
        EditText curEditText = getEditText(curPosition);
        EditText nextEditText = getEditText(curPosition + 1);
        if (nextEditText != null) {
            // 下一个
            nextEditText.setEnabled(true);
            nextEditText.requestFocus();
            nextEditText.setCursorVisible(true);
        }
        if (curEditText != null) {
            // 当前
            curEditText.setEnabled(false);
            curEditText.setCursorVisible(false);
        }
        //line
        View curLine = getLine(curPosition);
        if (curLine != null) {
            // 当前
            curLine.setSelected(true);
        }
    }

    private EditText getEditText(int index) {
        EditText editText = null;
        if (mEditList != null && !mEditList.isEmpty()) {
            if (index >= 0 && index <= mEditList.size() - 1) {
                editText = mEditList.get(index);
            }
        }
        return editText;
    }

    private View getLine(int index) {
        View line = null;
        if (mLineList != null && !mLineList.isEmpty()) {
            if (index >= 0 && index <= mLineList.size() - 1) {
                line = mLineList.get(index);
            }
        }
        return line;
    }

    private String getAllText() {
        Editable text1 = etFirstNumber.getText();
        Editable text2 = etSecondNumber.getText();
        Editable text3 = etThirdNumber.getText();
        Editable text4 = etFourthNumber.getText();
        StringBuilder stringBuilder = new StringBuilder();
        if (text1.length() > 0) {
            stringBuilder.append(text1.toString());
        }
        if (text2.length() > 0) {
            stringBuilder.append(text2.toString());
        }
        if (text3.length() > 0) {
            stringBuilder.append(text3.toString());
        }
        if (text4.length() > 0) {
            stringBuilder.append(text4.toString());
        }
        return stringBuilder.toString();
    }

    private void textChanged() {
        if (isBanCallback) return;
        String code = getAllText();
        if (onInputListener != null) onInputListener.onTextChanged(code);
    }

    private void textCompleted(Editable s) {
        if (isBanCallback) return;
        if (s.length() < 1) {
            //没数据
            etFourthNumber.setCursorVisible(true);
            etFourthLine.setSelected(false);
        } else {
            //有数据
            etFourthNumber.setCursorVisible(false);
            etFourthLine.setSelected(true);
            //输入完成
            if (onInputListener != null) onInputListener.onCompleted(getAllText());
        }
    }

    public void firstFocus() {
        isBanCallback = false;
        etFirstNumber.setEnabled(true);
        etFirstNumber.setFocusable(true);
        etFirstNumber.setFocusableInTouchMode(true);
        etFirstNumber.requestFocus();
        etFirstNumber.setCursorVisible(true);
    }

    public void resetUI() {
        isBanCallback = true;
        etFirstNumber.setText("");
        etFirstNumber.setEnabled(false);
        etFirstLine.setSelected(false);
        etSecondNumber.setText("");
        etSecondNumber.setEnabled(false);
        etSecondLine.setSelected(false);
        etThirdNumber.setText("");
        etThirdNumber.setEnabled(false);
        etThirdLine.setSelected(false);
        etFourthNumber.setText("");
        etFourthNumber.setEnabled(false);
        etFourthLine.setSelected(false);
    }

    public EditText getFirstEidtText() {
        return etFirstNumber;
    }

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    public interface OnInputListener {
        /**
         * 文本变化
         *
         * @param code
         */
        void onTextChanged(String code);

        /**
         * 输入完成
         *
         * @param code
         */
        void onCompleted(String code);
    }
}
