package vn.gas.thq.ui.sangchiet.qlsangchiet

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class HistorySangChietModel {
    @SerializedName("amount_gas_liquid_bf")
    @Expose
    var amountGasLiquidBf: Int? = null

    @SerializedName("amount_gas_used")
    @Expose
    var amountGasUsed: Int? = null

    @SerializedName("amount_gas12")
    @Expose
    var amountGas12: Int? = null

    @SerializedName("amount_gas45")
    @Expose
    var amountGas45: Int? = null

    @SerializedName("amount_tank12")
    @Expose
    var amountTank12: Int? = null

    @SerializedName("amount_tank45")
    @Expose
    var amountTank45: Int? = null

    @SerializedName("created_date")
    @Expose
    var createdDate: String? = null
}