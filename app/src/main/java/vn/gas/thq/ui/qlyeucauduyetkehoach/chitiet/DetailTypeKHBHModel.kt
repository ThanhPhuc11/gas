package vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class DetailTypeKHBHModel {
    @SerializedName("approve_type")
    @Expose
    var approveType: String? = null

    @SerializedName("reason")
    @Expose
    var reason: String? = null
}