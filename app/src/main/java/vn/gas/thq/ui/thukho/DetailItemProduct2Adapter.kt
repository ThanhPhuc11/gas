package vn.gas.thq.ui.thukho

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.customview.ItemProductType2
import vn.gas.thq.model.ProductModel
import vn.hongha.ga.R

class DetailItemProduct2Adapter(private val mList: MutableList<ProductModel>) :
    RecyclerView.Adapter<DetailItemProduct2Adapter.ProductViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_2_ycxk, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val obj: ProductModel = mList[position]
        holder.itemProductType2.setTen(obj.name)
        holder.itemProductType2.setSoLuong("${obj.quantity}")
        when (obj.code) {
            "GAS12", "GAS45" -> {
                holder.itemProductType2.setIcon("0")
            }
            "TANK12", "TANK45" -> {
                holder.itemProductType2.setIcon("1")
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ProductViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var itemProductType2: ItemProductType2 = itemView.findViewById(R.id.itemProduct)

        init {
//            itemRequestType1.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mClickListener?.onItemClick(p0, adapterPosition)
        }
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}