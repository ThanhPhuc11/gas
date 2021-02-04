package vn.gas.thq.ui.kehoachbh

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class RequestKeHoachModel {
    @SerializedName("detail")
    @Expose
    var detail: List<KeHoachModel>? = null
}