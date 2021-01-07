package vn.gas.thq.model

data class BussinesRequestModel(
    val staff_name: String,
    val status: String,
    val created_date: String,
    val type: String,
    val stock_trans_id: Int
)