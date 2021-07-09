package vn.gas.thq.ui.sangchiet.nhapsangchiet

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class AvailableKHLResponse {
    @SerializedName("current_gas_quantity")
    @Expose
    var currentGasQuantity: Float? = null

    @SerializedName("current_gas_remain_quantity")
    @Expose
    var currentGasRemainQuantity: Float? = null

    @SerializedName("current_gas_kk_quantity")
    @Expose
    var currentGasKkQuantity: Float? = null
}