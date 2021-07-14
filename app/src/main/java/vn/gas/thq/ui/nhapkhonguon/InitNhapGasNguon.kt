package vn.gas.thq.ui.nhapkhonguon

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class InitNhapGasNguon {
    @SerializedName("license_plate_id")
    @Expose
    var licensePlateId: Int? = null

    @SerializedName("shop_id")
    @Expose
    var shopId: Int? = null

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Int? = null
}