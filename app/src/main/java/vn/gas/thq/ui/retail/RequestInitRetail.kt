package vn.gas.thq.ui.retail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.gas.thq.model.ProductRetailModel

class RequestInitRetail {
    @SerializedName("customer_id")
    @Expose
    var customerId: Int? = null

    @SerializedName("debit")
    @Expose
    var debit: Int? = null

    @SerializedName("lat")
    @Expose
    var lat: Float? = null

    @SerializedName("lng")
    @Expose
    var lng: Float? = null

    @SerializedName("pay_method")
    @Expose
    var payMethod: Int? = 1

    @SerializedName("item")
    @Expose
    var item: List<ProductRetailModel>? = null
}