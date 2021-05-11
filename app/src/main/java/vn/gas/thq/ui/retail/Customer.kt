package vn.gas.thq.ui.retail

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Customer {
    @SerializedName("customer_id")
    @Expose
    var customerId: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("tel_contact")
    @Expose
    var telContact: String? = null

    @SerializedName("staff_name")
    @Expose
    var staffName: String? = null

    @SerializedName("sale_line_name")
    @Expose
    var saleLineName: String? = null

    @SerializedName("shop_area")
    @Expose
    var shopArea: String? = null

    var lat: Float? = null
    var lng: Float? = null
}