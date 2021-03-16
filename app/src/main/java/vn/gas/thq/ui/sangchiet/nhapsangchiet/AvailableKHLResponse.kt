package vn.gas.thq.ui.sangchiet.nhapsangchiet

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class AvailableKHLResponse {
    @SerializedName("current_quantity")
    @Expose
    var currentQuantity: Int? = null
}