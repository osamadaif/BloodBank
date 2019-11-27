
package com.osama.daif.bloodbank.data.model.posts2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostToggleFavourite {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private PostToggleFavouriteData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PostToggleFavouriteData getData() {
        return data;
    }

    public void setData(PostToggleFavouriteData data) {
        this.data = data;
    }

}
