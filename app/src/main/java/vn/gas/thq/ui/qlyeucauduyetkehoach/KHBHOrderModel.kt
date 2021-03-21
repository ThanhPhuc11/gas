package vn.gas.thq.ui.qlyeucauduyetkehoach

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class KHBHOrderModel {
    @SerializedName("plan_id")
    @Expose
    var planId: Int? = null

    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null

    @SerializedName("sale_line_name")
    @Expose
    var saleLineName: String? = null

    @SerializedName("staff_name")
    @Expose
    var staffName: String? = null

    @SerializedName("status_name")
    @Expose
    var statusName: String? = null

    @SerializedName("effect_date")
    @Expose
    var effectDate: String? = null

    @SerializedName("can_approve_status")
    @Expose
    var canApproveStatus: String = "22"
}