package vn.gas.thq.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ErrorModel {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("detail")
    @Expose
    var detail: String? = null
}