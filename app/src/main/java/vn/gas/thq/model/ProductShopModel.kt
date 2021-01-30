package vn.gas.thq.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ProductShopModel {
    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    @SerializedName("product_code")
    @Expose
    var productCode: String? = null

    @SerializedName("current_quantity")
    @Expose
    var currentQuantity: Int? = null

    @SerializedName("available_quantity")
    @Expose
    var availableQuantity: Int? = null

    var unit: String? = null
}