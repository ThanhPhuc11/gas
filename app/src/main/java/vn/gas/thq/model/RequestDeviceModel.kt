package vn.gas.thq.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class RequestDeviceModel {
    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("device_token")
    @Expose
    var deviceToken: String? = null

    @SerializedName("old_device_token")
    @Expose
    var oldDeviceToken: String? = null

    @SerializedName("device_model")
    @Expose
    var deviceModel: String? = null

    @SerializedName("platform")
    @Expose
    var platform: String? = null

    @SerializedName("os_version")
    @Expose
    var osVersion: String? = null
}