package vn.gas.thq.ui.trano.nhaptrano

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class InitTraNo {
    @SerializedName("debit_type")
    @Expose
    var debitType: Int? = null

    @SerializedName("cust_id")
    @Expose
    var custId: Int? = null

    @SerializedName("amount_cash")
    @Expose
    var amountCash: Int? = null

    @SerializedName("amount_transfer")
    @Expose
    var amountTransfer: Int? = null

    @SerializedName("tank_amount")
    @Expose
    var tankAmount: Int? = null
}