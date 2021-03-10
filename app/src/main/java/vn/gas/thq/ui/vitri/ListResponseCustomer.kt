package vn.gas.thq.ui.vitri

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import vn.gas.thq.ui.retail.Customer


class ListResponseCustomer {
    @SerializedName("data")
    @Expose
    var data: List<Customer>? = null

    @SerializedName("page")
    @Expose
    var page: Page? = null
}