package vn.gas.thq.ui.nhapvo

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class VoModel {
    @SerializedName("product_offering_id")
    @Expose
    var productOfferingId: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("transfer_type")
    @Expose
    var transferType: String? = null

    var amount: Int? = null

    //Phuc vu adapter
    var slXuat: Int? = null
    var slNhap: Int? = null
}