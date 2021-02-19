package vn.gas.thq.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class UserModel {
    @SerializedName("staff_id")
    @Expose
    var staffId: Int? = null

    @SerializedName("staff_code")
    @Expose
    var staffCode: String? = null

    @SerializedName("sale_line_name")
    @Expose
    var saleLineName: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("mobile_number")
    @Expose
    var mobileNumber: String? = null

    @SerializedName("is_deleted")
    @Expose
    var isDeleted: Boolean? = null

    @SerializedName("is_suspended")
    @Expose
    var isSuspended: Boolean? = null

    @SerializedName("suspend_reason")
    @Expose
    var suspendReason: Any? = null

    @SerializedName("created_date")
    @Expose
    var createdDate: Any? = null

    @SerializedName("last_modified_date")
    @Expose
    var lastModifiedDate: Any? = null

    @SerializedName("login_fail_time")
    @Expose
    var loginFailTime: Any? = null
}