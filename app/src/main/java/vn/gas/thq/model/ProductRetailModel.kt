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

    @SerializedName("price_plan")
    @Expose
    var pricePlan: Int? = 0

    @SerializedName("price_standard")
    @Expose
    var priceStandard: Int? = 0

    @SerializedName("sale_type")
    @Expose
    var saleType: String? = saleType
}