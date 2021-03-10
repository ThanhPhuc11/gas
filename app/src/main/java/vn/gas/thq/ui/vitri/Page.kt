package vn.gas.thq.ui.vitri

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Page {
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null

    @SerializedName("has_next")
    @Expose
    var hasNext: Boolean? = null

    @SerializedName("has_previous")
    @Expose
    var hasPrevious: Boolean? = null

    @SerializedName("number")
    @Expose
    var number: Int? = null

    @SerializedName("total_elements")
    @Expose
    var totalElements: Int? = null
}