package vn.gas.thq.ui.vitri

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ShopModel {
    @SerializedName("shop_id")
    @Expose
    var shopId: Int? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("area_code")
    @Expose
    var areaCode: String? = null

    @SerializedName("channel_type_id")
    @Expose
    var channelTypeId: String? = null

    @SerializedName("district")
    @Expose
    var district: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("home")
    @Expose
    var home: String? = null

    @SerializedName("id_issue_date")
    @Expose
    var idIssueDate: String? = null

    @SerializedName("id_issue_place")
    @Expose
    var idIssuePlace: String? = null

    @SerializedName("id_no")
    @Expose
    var idNo: String? = null

    @SerializedName("id_type")
    @Expose
    var idType: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("parent_shop_id")
    @Expose
    var parentShopId: Int? = null

    @SerializedName("precinct")
    @Expose
    var precinct: String? = null

    @SerializedName("province")
    @Expose
    var province: String? = null

    @SerializedName("shop_code")
    @Expose
    var shopCode: String? = null

    @SerializedName("shop_path")
    @Expose
    var shopPath: String? = null

    @SerializedName("shop_type")
    @Expose
    var shopType: String? = null

    @SerializedName("staff_owner_id")
    @Expose
    var staffOwnerId: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("street")
    @Expose
    var street: String? = null

    @SerializedName("street_block")
    @Expose
    var streetBlock: String? = null

    @SerializedName("tel")
    @Expose
    var tel: String? = null

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

    @SerializedName("is_deleted")
    @Expose
    var isDeleted: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null
}