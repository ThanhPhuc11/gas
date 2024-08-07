package vn.gas.thq.service

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class RegisterDeviceResponse {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: DeviceResponse? = null
}