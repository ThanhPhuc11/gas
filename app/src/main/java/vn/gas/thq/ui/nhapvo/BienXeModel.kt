package vn.gas.thq.ui.nhapvo

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class BienXeModel {
    @SerializedName("license_plate_id")
    @Expose
    var licensePlateId: Int? = null

    @SerializedName("plate_no")
    @Expose
    var plateNo: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("shop_id")
    @Expose
    var shopId: Int? = null
}