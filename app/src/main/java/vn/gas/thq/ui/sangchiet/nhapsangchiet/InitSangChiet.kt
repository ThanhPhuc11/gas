package vn.gas.thq.ui.sangchiet.nhapsangchiet

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class InitSangChiet {
    @SerializedName("amount_gas_liquid_bf")
    @Expose
    var amountGasLiquidBf: Int? = null

    @SerializedName("amount_gas12")
    @Expose
    var amountGas12: Int? = null

    @SerializedName("amount_gas45")
    @Expose
    var amountGas45: Int? = null
}