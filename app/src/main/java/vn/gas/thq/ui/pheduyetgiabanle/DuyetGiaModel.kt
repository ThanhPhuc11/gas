package vn.gas.thq.ui.pheduyetgiabanle

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DuyetGiaModel(
    @SerializedName("product_type")
    @Expose
    var productType: Int? = null,
    @SerializedName("reason")
    @Expose
    var reason: String? = null
)