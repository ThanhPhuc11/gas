package vn.gas.thq.ui.xuatkhoKH.thuchienxuatkhokh

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ExportItemModel {
    @SerializedName("product_code")
    @Expose
    var productCode: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Int? = null
}