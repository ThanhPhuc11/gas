package vn.gas.thq.ui.vitri

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class SaleLineModel {
    @SerializedName("created_by")
    @Expose
    var createdBy: String? = null

    @SerializedName("created_date")
    @Expose
    var createdDate: String? = null

    @SerializedName("last_modified_by")
    @Expose
    var lastModifiedBy: String? = null

    @SerializedName("last_modified_date")
    @Expose
    var lastModifiedDate: String? = null

    @SerializedName("sale_line_id")
    @Expose
    var saleLineId: Int? = null

    @SerializedName("sale_line_code")
    @Expose
    var saleLineCode: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("number_plate")
    @Expose
    var numberPlate: String? = null
}