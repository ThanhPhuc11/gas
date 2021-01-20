package vn.gas.thq.ui.retail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseInitRetail {
    var id: Int? = null

    @SerializedName("need_approve")
    @Expose
    var needApprove: Boolean? = false
}