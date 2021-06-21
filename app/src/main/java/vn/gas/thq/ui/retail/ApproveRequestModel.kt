package vn.gas.thq.ui.retail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.gas.thq.model.ProductRetailModel


class ApproveRequestModel {
    @SerializedName("customer_id")
    @Expose
    var customerId: Int? = null

    @SerializedName("customer_name")
    @Expose
    var customerName: String? = null

    @SerializedName("customer_address")
    @Expose
    var customerAddress: String? = null

    @SerializedName("customer_tel_contact")
    @Expose
    var customerTelContact: String? = null

    @SerializedName("sale_line_name")
    @Expose
    var saleLineName: String? = null

    @SerializedName("debt_amount_tank12")
    @Expose
    var debtAmountTank12: Int? = null

    @SerializedName("debt_amount_tank45")
    @Expose
    var debtAmountTank45: Int? = null

    @SerializedName("debt_amount")
    @Expose
    var debtAmount: Int? = null

    @SerializedName("gas_return")
    @Expose
    var amountGasReturn: Float? = null

    @SerializedName("fee_ship")
    @Expose
    var feeShip: Int? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("approve_status")
    @Expose
    var approveStatus: String? = null

    @SerializedName("can_approve_status")
    @Expose
    var canApproveStatus: String? = null

    @SerializedName("items")
    @Expose
    var item: List<ProductRetailModel>? = null

    @SerializedName("payment_amount_money")
    @Expose
    var paymentAmountMoney: Int? = null

    @SerializedName("payment_amount_transfer")
    @Expose
    var paymentAmountTransfer: Int? = null

    @SerializedName("debt_expire_date")
    @Expose
    var debtExpireDate: String? = null
}