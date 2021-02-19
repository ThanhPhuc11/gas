package vn.gas.thq.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import vn.hongha.ga.R
import kotlinx.android.synthetic.main.item_product_type_1.view.*

class ItemProductType1 : CardView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView: View = inflater.inflate(R.layout.item_product_type_1, this, true)
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemProductType1)
        try {
//            mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
//            mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
            setIcon(a.getString(R.styleable.ItemProductType1_hinhAnh))
            setTen(a.getString(R.styleable.ItemProductType1_tenSanPham))
            setSoLuong(a.getString(R.styleable.ItemProductType1_soLuong))
            setGia(a.getString(R.styleable.ItemProductType1_gia))
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

    fun setIcon(trangThai: String?) {
        if (trangThai == "0") {
            imgIconType.setImageResource(R.drawable.ic_fire)
        } else {
            imgIconType.setImageResource(R.drawable.ic_vo_gas)
        }
    }

    fun setTen(name: String?) {
        if (name != null)
            tvProductName.text = name.trim()
    }

    fun setSoLuong(sl: String?) {
        if (sl != null)
            edtAmount.setText(sl.trim())
    }

    fun setGia(gia: String?) {
        if (gia != null)
            edtPrice.setText(gia.trim())
    }

    fun getEditTextSL(): EditText {
        return edtAmount
    }

    fun getEditTextGia(): AppCompatAutoCompleteTextView {
        return edtPrice
    }
}