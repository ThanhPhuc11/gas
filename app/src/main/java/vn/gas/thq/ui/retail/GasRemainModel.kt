package vn.gas.thq.ui.retail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.gas.thq.model.ProductModel
import vn.gas.thq.model.ProductShopModel
import vn.gas.thq.ui.nhapkho.ProductNhapKhoModel
import vn.gas.thq.ui.nhapkho.ProductShopNhapKhoModel

class GasRemainModel {
    @SerializedName("gas_remain")
    @Expose
    var gasRemain: Float? = null

    @SerializedName("return_item")
    @Expose
    var returnItem: List<ProductNhapKhoModel>? = null
}