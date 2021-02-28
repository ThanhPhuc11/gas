package vn.gas.thq.service

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeviceResponse {
    @SerializedName("create_user")
    @Expose
    var createUser: String? = null

    @SerializedName("update_user")
    @Expose
    var updateUser: String? = null

    @SerializedName("create_timestamp")
    @Expose
    var createTimestamp: String? = null

    @SerializedName("update_timestamp")
    @Expose
    var updateTimestamp: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("device_model")
    @Expose
    var deviceModel: String? = null

    @SerializedName("platform")
    @Expose
    var platform: String? = null

    @SerializedName("os_version")
    @Expose
    var osVersion: String? = null

    @SerializedName("is_active")
    @Expose
    var isActive: Boolean? = null
}