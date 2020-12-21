package vn.gas.thq.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import vn.hongha.ga.R
import kotlinx.android.synthetic.main.item_product.*
import kotlinx.android.synthetic.main.item_product.view.*

class ItemCount : CardView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView: View = inflater.inflate(R.layout.item_product, this, true)
        imgIconType
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    fun isLiquid(isLiquid: Boolean) {
        if (isLiquid) {
            imgIconType.setImageResource(R.drawable.ic_fire)
        } else {
            imgIconType.setImageResource(R.drawable.ic_vo_gas)
        }
    }
}