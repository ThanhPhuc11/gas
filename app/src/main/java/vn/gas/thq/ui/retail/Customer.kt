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
}