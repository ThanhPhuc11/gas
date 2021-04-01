package vn.gas.thq.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.layout_item_personal_request.view.*
import vn.hongha.ga.R

class ItemRequestType1 : CardView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView: View = inflater.inflate(R.layout.layout_item_personal_request, this, true)
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemRequestType)
        try {
            setTrangThai(a.getString(R.styleable.ItemRequestType_trangThai))
            setThoiGian(a.getString(R.styleable.ItemRequestType_thoiGian))

        } finally {
            a.recycle()
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    fun setTrangThai(trangThai: String?) {
        when (trangThai) {
            "0" -> {
                tvStatus.text = "Đã huỷ"
                tvStatus.setTextColor(resources.getColor(R.color.red_DB4755))
            }
            "1" -> {
                tvStatus.text = "Chờ duyệt"
                tvStatus.setTextColor(resources.getColor(R.color.blue_2C5181))
            }
            "3" -> {
                tvStatus.text = "Đã duyệt"
                tvStatus.setTextColor(resources.getColor(R.color.blue_14AFB4))
            }
            "2" -> {
                tvStatus.text = "Từ chối"
                tvStatus.setTextColor(resources.getColor(R.color.red_DB4755))
            }
        }
    }

    fun setTrangThaiBanLe(approveStatus: String?) {
        tvStatus.text = approveStatus ?: ""
    }

    fun setColorTrangThaiBanLe(color: Int) {
        tvStatus.setTextColor(resources.getColor(color))
    }

    fun setThoiGian(thoigian: String?) {
        if (thoigian != null) {
            tvSaleTime.text = thoigian
        }
    }

    fun setTen(name: String?) {
        tvName.text = name ?: ""
    }

    fun setLoaiYC(loaiYC: String?) {
        tvLoaiYC.text = loaiYC ?: ""
    }

    fun isVisibleKH(isVisible: Boolean) {
        llCustomer.isVisible = isVisible
    }

    fun setTenKH(custName: String?) {
        tvCustName.text = custName ?: "- -"
    }

    fun setOrderId(orderId: String?) {
        tvOrderId.text = orderId ?: "- -"
    }

    fun getllWrap(): LinearLayout {
        return llWrap
    }

    fun getNguoiDuyet(): TextView {
        return tvNguoiDuyet
    }

    fun getNguoiDuyetMore(): TextView {
        return tvNguoiDuyetMore
    }
}