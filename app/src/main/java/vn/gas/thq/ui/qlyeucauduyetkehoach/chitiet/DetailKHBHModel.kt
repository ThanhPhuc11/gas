package vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.gas.thq.ui.kehoachbh.KeHoachModel


class DetailKHBHModel {
    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null

    @SerializedName("sale_line_name")
    @Expose
    var saleLineName: String? = null

    @SerializedName("staff_name")
    @Expose
    var staffName: String? = null

    @SerializedName("effect_date")
    @Expose
    var effectDate: String? = null

    @SerializedName("detail")
    @Expose
    var detail: MutableList<KeHoachModel>? = null
}