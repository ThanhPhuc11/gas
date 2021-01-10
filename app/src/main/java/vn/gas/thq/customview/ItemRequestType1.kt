package vn.gas.thq.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
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
            "1" -> {
                tvStatus.text = "Chờ duyệt"
                tvStatus.setTextColor(resources.getColor(R.color.blue_2C5181))
            }
            "2" -> {
                tvStatus.text = "Đã duyệt"
                tvStatus.setTextColor(resources.getColor(R.color.blue_14AFB4))
            }
            "3" -> {
                tvStatus.text = "Đã huỷ"
                tvStatus.setTextColor(resources.getColor(R.color.red_DB4755))
            }
        }
    }

    fun setThoiGian(thoigian: String?) {
        if (thoigian != null) {
            tvOrderTime.text = thoigian
        }
    }

    fun setTen(name: String?) {
        tvName.text = name ?: ""
    }
}