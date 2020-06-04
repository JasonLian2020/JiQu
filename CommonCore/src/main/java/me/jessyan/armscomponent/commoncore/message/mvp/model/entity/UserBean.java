package me.jessyan.armscomponent.commoncore.message.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserBean implements Parcelable {
    @SerializedName("id")
    private int userId;
    @SerializedName("user_login")
    private String userLogin;
    @SerializedName("user_pass")
    private String userPass;
    @SerializedName("user_nickname")
    private String userNickname;
    @SerializedName("avatar")
    private String userAvatar;
    private String Code;
    private String signature;
    private int sex;
    @SerializedName("like_count")
    private long likeCount;
    @SerializedName("fan_count")
    private long fanCount;
    @SerializedName("follow_count")
    private long followCount;
    @SerializedName("create_time")
    private long createTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getFanCount() {
        return fanCount;
    }

    public void setFanCount(long fanCount) {
        this.fanCount = fanCount;
    }

    public long getFollowCount() {
        return followCount;
    }

    public void setFollowCount(long followCount) {
        this.followCount = followCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.userLogin);
        dest.writeString(this.userPass);
        dest.writeString(this.userNickname);
        dest.writeString(this.userAvatar);
        dest.writeString(this.Code);
        dest.writeString(this.signature);
        dest.writeInt(this.sex);
        dest.writeLong(this.likeCount);
        dest.writeLong(this.fanCount);
        dest.writeLong(this.followCount);
        dest.writeLong(this.createTime);
    }

    public UserBean(int userId, String userNickname, String userAvatar) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.userAvatar = userAvatar;
    }

    private UserBean(Parcel in) {
        this.userId = in.readInt();
        this.userLogin = in.readString();
        this.userPass = in.readString();
        this.userNickname = in.readString();
        this.userAvatar = in.readString();
        this.Code = in.readString();
        this.signature = in.readString();
        this.sex = in.readInt();
        this.likeCount = in.readLong();
        this.fanCount = in.readLong();
        this.followCount = in.readLong();
        this.createTime = in.readLong();
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
