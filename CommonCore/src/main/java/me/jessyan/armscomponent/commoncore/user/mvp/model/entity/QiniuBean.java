package me.jessyan.armscomponent.commoncore.user.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

public class QiniuBean {

    /**
     * expire : 1560149353
     * image_token : JCr1_IcY3Uz3eCY9T3r1Z0zLW4DNg7YxvX9G1dDh:o0YwE8aGck8QmLLRKWwG0cfojF0=:eyJzY29wZSI6ImNtc19pbWFnZXNfYnVja2V0IiwiZGVhZGxpbmUiOjE1NjAxNTU4OTB9
     * image_uri : http://pso78p7ug.bkt.clouddn.com
     * video_token : JCr1_IcY3Uz3eCY9T3r1Z0zLW4DNg7YxvX9G1dDh:H5kgjWCXNg82roTg9mpk2AZf59M=:eyJzY29wZSI6ImNtc192aWRlb19idWNrZXQiLCJkZWFkbGluZSI6MTU2MDE1NTg5MH0=
     * video_uri : http://pso7jtoj0.bkt.clouddn.com
     */
    private String expire;//token过期时间
    @SerializedName("image_token")
    private String imageToken;//图片上传token
    @SerializedName("image_uri")
    private String imageUri;//图片访问uri
    @SerializedName("video_token")
    private String videoToken;//视频上传token
    @SerializedName("video_uri")
    private String videoUri;//视频访问uri

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getImageToken() {
        return imageToken;
    }

    public void setImageToken(String imageToken) {
        this.imageToken = imageToken;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getVideoToken() {
        return videoToken;
    }

    public void setVideoToken(String videoToken) {
        this.videoToken = videoToken;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }
}
