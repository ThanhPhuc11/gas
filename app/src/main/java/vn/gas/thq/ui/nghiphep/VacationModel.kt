package vn.gas.thq.ui.nghiphep

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class VacationModel {
    @SerializedName("created_by")
    @Expose
    var createdBy: String? = null

    @SerializedName("created_date")
    @Expose
    var createdDate: String? = null

    @SerializedName("last_modified_by")
    @Expose
    var lastModifiedBy: String? = null

    @SerializedName("last_modified_date")
    @Expose
    var lastModifiedDate: String? = null

    @SerializedName("staff_register_vacation_id")
    @Expose
    var staffRegisterVacationId: Int? = null

    @SerializedName("staff_id")
    @Expose
    var staffId: Int? = null

    @SerializedName("from_date")
    @Expose
    var fromDate: String? = null

    @SerializedName("to_date")
    @Expose
    var toDate: String? = null

    @SerializedName("reason")
    @Expose
    var reason: String? = null

    var staffName: String? = null
}