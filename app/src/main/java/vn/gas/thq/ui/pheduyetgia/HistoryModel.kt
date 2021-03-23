package vn.gas.thq.ui.pheduyetgia

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class HistoryModel {
    @SerializedName("issue_datetime")
    @Expose
    var issueDatetime: String? = null

    @SerializedName("action")
    @Expose
    var action: String? = null

    @SerializedName("created_by")
    @Expose
    var createdBy: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("content")
    @Expose
    var content: String? = null
}