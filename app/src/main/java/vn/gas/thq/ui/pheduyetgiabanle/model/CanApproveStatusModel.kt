package vn.gas.thq.ui.pheduyetgiabanle.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CanApproveStatusModel {
    @SerializedName("gas_status")
    @Expose
    var gasStatus: String? = null

    @SerializedName("tank_status")
    @Expose
    var tankStatus: String? = null

    @SerializedName("debit_status")
    @Expose
    var debitStatus: String? = null

//    @SerializedName("product_type_approve_bit_map")
//    @Expose
//    var productTypeApproveBitMap: ProductTypeApproveBitMap? = null

    @SerializedName("approved_all")
    @Expose
    var approvedAll: Boolean? = null

    @SerializedName("approved_agent_all")
    @Expose
    var approvedAgentAll: Boolean? = null

    @SerializedName("reject_all")
    @Expose
    var rejectAll: Boolean? = null

}