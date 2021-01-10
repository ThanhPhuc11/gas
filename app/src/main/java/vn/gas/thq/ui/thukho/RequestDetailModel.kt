package vn.gas.thq.ui.thukho

import android.content.ClipData.Item
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.gas.thq.model.ProductModel


class RequestDetailModel {
    @SerializedName("staff_name")
    @Expose
    var staffName: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("created_date")
    @Expose
    var createdDate: String? = null

    @SerializedName("item")
    @Expose
    var item: MutableList<ProductModel> = mutableListOf()
}