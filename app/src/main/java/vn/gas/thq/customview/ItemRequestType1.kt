package vn.gas.thq.customview

import android.content.Context
import android.content.res.TypedArray
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
        if (trangThai == "1") {
            tvStatus.text = "Chờ duyệt"
        } else {
            tvStatus.text = "Đã huỷ"
        }
    }

    fun setThoiGian(thoigian: String?) {
        if (thoigian != null) {
            tvOrderTime.text = thoigian
        }
    }
}