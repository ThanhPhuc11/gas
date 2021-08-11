package vn.gas.thq.ui.changepassword

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class NewPasswordModel {
    @SerializedName("old_password")
    @Expose
    var oldPassword: String? = null

    @SerializedName("new_password")
    @Expose
    var newPassword: String? = null
}