package vn.gas.thq.ui.pheduyetgia

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CommentModel {
    @SerializedName("product_type")
    @Expose
    var productType: String? = null

    @SerializedName("comment")
    @Expose
    var comment: String? = null

    @SerializedName("ok")
    @Expose
    var ok: Boolean? = null
}