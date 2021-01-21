package vn.gas.thq.model

import android.os.Parcelable
import java.io.Serializable

data class TransferRetailModel(
    val orderId: String?,

    val khiBan12: Int?,
    val khiBanPrice12: Int?,
    val khiBan45: Int?,
    val khiBanPrice45: Int?,

    val voThu12: Int?,
    val voThu45: Int?,

    val voBan12: Int?,
    val voBanPrice12: Int?,
    val voBan45: Int?,
    val voBanPrice45: Int?,

    val voMua12: Int?,
    val voMuaPrice12: Int?,
    val voMua45: Int?,
    val voMuaPrice45: Int?,

    val tienThucTe: Int?
) : Serializable