package vn.gas.thq.ui.login

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class LoginModel {
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null

    @SerializedName("expires_in")
    @Expose
    var expiresIn: Int? = null

    @SerializedName("token_type")
    @Expose
    var tokenType: String? = null

    @SerializedName("refresh_token")
    @Expose
    var refreshToken: String? = null

    @SerializedName("refresh_token_expires_in")
    @Expose
    var refreshTokenExpiresIn: String? = null

    @SerializedName("correlation_id")
    @Expose
    var correlationId: String? = null
}