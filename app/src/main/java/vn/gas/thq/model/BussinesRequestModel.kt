package vn.gas.thq.model

data class BussinesRequestModel(
    val staff_name: String?,
    val status: Int?,
    val created_date: String?,
    val type: String?,
    val stock_trans_id: Int?,

    //Retail
    val order_id: Int?,
    val customer_name: String?,
    val approve_status: String?,
    val can_approve_status: String?,
    val sale_order_type: String?
) {
    var approve_staffs: MutableList<String>? = null
}