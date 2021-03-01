package vn.gas.thq.ui.retail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GasRemainModel {
    @SerializedName("gas_remain")
    @Expose
    var gasRemain: Float? = null
}