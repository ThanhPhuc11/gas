package vn.gas.thq.ui.xuatkhoKH.thuchienxuatkhokh

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class XuatHangModel {
    @SerializedName("export_items")
    @Expose
    var exportItems: MutableList<ExportItemModel>? = null
}