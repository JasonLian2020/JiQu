package me.jessyan.armscomponent.commoncore.message.mvp.model.entity;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import me.jessyan.armscomponent.commonui.easyat.span.DataBindingSpan;

public class ContentBean implements DataBindingSpan, Parcelable {
    public static final String TAG_AT = "at";
    public static final String TAG_TEXT = "text";
    /**
     * text表示内容，at表示用户
     */
    private String tag;
    private String content;
    @SerializedName("user_nickname")
    private String userName;
    @SerializedName("user_id")
    private Integer userId;

    public ContentBean() {
        //newInstance需要声明构造方法
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Spannable getSpannedName() {
        SpannableString spannableString = SpannableString.valueOf("@" + userName + " ");
        spannableString.setSpan(new ForegroundColorSpan(Color.CYAN), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (onClickListener != null) onClickListener.onClick(widget, userId, userName);
            }
        }, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public Spannable getCommentName() {
        SpannableString spannableString = SpannableString.valueOf(userName + "：");
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (onClickListener != null) onClickListener.onClick(widget, userId, userName);
            }
        }, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(@NonNull View widget, int userId, String userName);
    }

    @Override
    public void setBeanTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void setBeanContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag);
        dest.writeString(this.content);
        dest.writeString(this.userName);
        dest.writeValue(this.userId);
    }

    private ContentBean(Parcel in) {
        this.tag = in.readString();
        this.content = in.readString();
        this.userName = in.readString();
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ContentBean> CREATOR = new Parcelable.Creator<ContentBean>() {
        @Override
        public ContentBean createFromParcel(Parcel source) {
            return new ContentBean(source);
        }

        @Override
        public ContentBean[] newArray(int size) {
            return new ContentBean[size];
        }
    };
}
