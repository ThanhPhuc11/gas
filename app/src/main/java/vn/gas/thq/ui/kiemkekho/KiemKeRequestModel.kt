package vn.gas.thq.ui.kiemkekho

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import vn.gas.thq.ui.nhapkho.ProductNhapKhoModel

class KiemKeRequestModel {
    @SerializedName("shop_code")
    @Expose
    var shopCode: String? = null

    @SerializedName("original_stock")
    @Expose
    var originalStock: MutableList<ProductNhapKhoModel> = mutableListOf()

    @SerializedName("new_stock")
    @Expose
    var newStock: MutableList<ProductNhapKhoModel> = mutableListOf()
}