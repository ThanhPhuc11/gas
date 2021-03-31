package vn.gas.thq.ui.nhapkho

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class RequestNhapKho {
    @SerializedName("staff_code")
    @Expose
    var staffCode: String? = null

    @SerializedName("gas_price")
    @Expose
    var gasPrice: Int? = 0

    @SerializedName("item")
    @Expose
    var item: MutableList<ProductNhapKhoV3Model> = mutableListOf()
}