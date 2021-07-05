package vn.gas.thq.util

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

fun String.converFormatDate(currentFormat: String, requiredFormat: String): String {
    var result: String = String.toString()
    if (TextUtils.isEmpty(String.toString())) {
        return result
    }
    val formatterOld = SimpleDateFormat(currentFormat)
    val formatterNew = SimpleDateFormat(requiredFormat, Locale.getDefault())
    var date: Date? = null
    try {
        date = formatterOld.parse(String.toString())
    } catch (e: Exception) {
    }
    if (date != null) {
        result = formatterNew.format(date)
    }
    return result
}