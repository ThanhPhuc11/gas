package vn.gas.thq.ui.trano.qltrano

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class HistoryTraNoModel {
    @SerializedName("created_date")
    @Expose
    var createdDate: String? = null

    @SerializedName("customer_name")
    @Expose
    var customerName: String? = null

    @SerializedName("audit_type")
    @Expose
    var auditType: String? = null

    @SerializedName("debit_type")
    @Expose
    var debitType: String? = null

    @SerializedName("debit_amount_bf")
    @Expose
    var debitAmountBf: String? = null

    @SerializedName("debit_amount")
    @Expose
    var debitAmount: String? = null

    @SerializedName("debit_amount_af")
    @Expose
    var debitAmountAf: String? = null

    @SerializedName("created_by")
    @Expose
    var createdBy: String? = null
}