package vn.gas.thq.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ProductRetailModel constructor(
    code: String?,
    quantity: Int?,
    price: Int?,
    saleType: String?
) {
    var code: String? = code
    var name: String? = null
    var quantity: Int? = quantity
    var price: Int? = price

    @SerializedName("sale_type")
    @Expose
    var saleType: String? = saleType
}