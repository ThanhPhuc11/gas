package vn.gas.thq.ui.nhapkho

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ProductNhapKhoModel {
    @SerializedName("product_code")
    @Expose
    var productCode: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Int? = 0
}