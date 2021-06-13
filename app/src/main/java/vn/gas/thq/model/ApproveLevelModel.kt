package vn.gas.thq.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ApproveLevelModel {
    @SerializedName("product_type")
    @Expose
    var productType: String? = null

    @SerializedName("level")
    @Expose
    var level: Int? = null
}