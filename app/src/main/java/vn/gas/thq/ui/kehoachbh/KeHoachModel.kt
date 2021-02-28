package vn.gas.thq.ui.kehoachbh

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.gas.thq.ui.nhapkho.ProductNhapKhoModel


class KeHoachModel {
    @SerializedName("customer_id")
    @Expose
    var customerId: Int? = null

    var custName: String = "- -"

    @SerializedName("days_per_visit")
    @Expose
    var daysPerVisit: Int? = null

    @SerializedName("item")
    @Expose
    var item: MutableList<ProductNhapKhoModel> = mutableListOf()
}