package vn.gas.thq.model

class BussinesRequestModel {
    var staff_name: String? = null
    var status: Int? = null
    var created_date: String? = null
    var type: String? = null
    var stock_trans_id: Int? = null

    //Retail
    var order_id: Int? = null
    var customer_name: String? = null
    var approve_status: String? = null
    var can_approve_status: String? = null
    var can_comment_status: String? = null
    var sale_order_type: String? = null

    var approve_staffs: MutableList<String>? = null


    //Ban le new
    var sale_line: String? = null

    //    var approve_levels: List<ApproveLevelModel>? = null
    var approve_levels: String? = null

    var displayType: String? = null

    var completeOrder: Int? = null
}