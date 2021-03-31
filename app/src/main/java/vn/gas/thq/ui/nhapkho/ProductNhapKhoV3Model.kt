package vn.gas.thq.ui.nhapkho

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ProductNhapKhoV3Model {
    @SerializedName("product_code")
    @Expose
    var productCode: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Float? = 0f

    @SerializedName("price")
    @Expose
    var price: Int? = 0
}