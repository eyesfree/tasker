package com.developer.krisi.tasker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ErrorModel {
    @SerializedName("error")
    @Expose
    public String error;

    @Override
    public String toString() {
        return "ErrorModel{" +
                "error='" + error + '\'' +
                '}';
    }
}
