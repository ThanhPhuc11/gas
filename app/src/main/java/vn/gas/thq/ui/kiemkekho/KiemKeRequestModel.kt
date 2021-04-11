package vn.gas.thq.ui.kiemkekho

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import vn.gas.thq.model.ProductNhapKhoV2Model
import vn.gas.thq.ui.nhapkho.ProductNhapKhoModel
import vn.gas.thq.ui.nhapkho.ProductNhapKhoV3Model

class KiemKeRequestModel {
    @SerializedName("shop_code")
    @Expose
    var shopCode: String? = null

    @SerializedName("original_stock")
    @Expose
    var originalStock: MutableList<ProductNhapKhoV3Model> = mutableListOf()

    @SerializedName("new_stock")
    @Expose
    var newStock: MutableList<ProductNhapKhoV3Model> = mutableListOf()
}