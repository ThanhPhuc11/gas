package vn.gas.thq.ui.nhapkho

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ProductShopNhapKhoModel {
    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    @SerializedName("product_code")
    @Expose
    var productCode: String? = null

    @SerializedName("current_quantity")
    @Expose
    var currentQuantity: Float? = null

    @SerializedName("available_quantity")
    @Expose
    var availableQuantity: Float? = null

    var unit: String? = null
}