package vn.gas.thq.ui.xemkho

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class KhoModel {
    @SerializedName("type")
    @Expose
    var type: Int? = 0

    @SerializedName("code")
    @Expose
    var code: String? = ""

    @SerializedName("name")
    @Expose
    var name: String? = ""

    @SerializedName("id")
    @Expose
    var id: Int? = 0

}