package vn.gas.thq.model

data class ProductNhapKhoV2Model(
    val name: String?,
    val code: String?,
    val description: String?,
    val type: String?,
    var quantity: Float?,
    var unit: String?
)