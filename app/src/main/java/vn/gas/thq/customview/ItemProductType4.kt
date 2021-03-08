package vn.gas.thq.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.item_product_type_4.view.*
import vn.hongha.ga.R

class ItemProductType4 : CardView {
    //    var edtSL: TextInputEditText
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView: View = inflater.inflate(R.layout.item_product_type_4, this, true)
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemProductType1)
        try {
//            mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
//            mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
            setIcon(a.getString(R.styleable.ItemProductType1_hinhAnh))
            setTen(a.getString(R.styleable.ItemProductType1_tenSanPham))
            setSoLuong(a.getString(R.styleable.ItemProductType1_soLuong))
            setSKU(a.getString(R.styleable.ItemProductType1_sku))

//            imgGiam.setOnClickListener {
//                val slHienTai = edtSL.text.toString()
//                if (slHienTai.toInt() > 0) {
//                    edtSL.setText((slHienTai.toInt() - 1).toString())
//                }
//            }
//            imgTang.setOnClickListener {
//                val slHienTai = edtSL.text.toString()
//                if (slHienTai.toInt() < 999) {
//                    edtSL.setText((slHienTai.toInt() + 1).toString())
//                }
//            }
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
            tvProductName.text = name
    }

    fun setSoLuong(sl: String?) {
        if (sl != null)
            edtSL.setText(sl)
    }

//    fun setGia(gia: String?) {
//        if (gia != null)
//            btnPrice.text = gia
//    }

    fun setSKU(sku: String?) {
        if (sku != null)
            tvProductDes.text = sku
    }

    fun getSL() {
        edtSL.text.toString().toInt()
    }

    fun getViewSL(): TextInputEditText? {
        return edtSL
    }

    fun getViewGiam(): ImageView {
        return imgGiam
    }

    fun getViewTang(): ImageView {
        return imgTang
    }

    fun isReadOnly() {
        edtSL.isEnabled = false
        imgTang.isEnabled = false
        imgGiam.isEnabled = false
        imgTang.visibility = View.INVISIBLE
        imgGiam.visibility = View.INVISIBLE
    }
}