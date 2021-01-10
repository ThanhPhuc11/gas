package vn.gas.thq.ui.qlyeucaucanhan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.customview.ItemProductType4
import vn.gas.thq.model.ProductModel
import vn.hongha.ga.R

class DetailItemProduct4Adapter(private val mList: MutableList<ProductModel>) :
    RecyclerView.Adapter<DetailItemProduct4Adapter.ProductViewHolder>() {
    var mClickListener: ItemClickListener? = null
    var isReadOnly = false

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_ycxk, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val obj: ProductModel = mList[position]
        holder.itemProductType4.setTen(obj.name)
        holder.itemProductType4.setSoLuong("${obj.quantity}")
        when (obj.code) {
            "GAS12" -> {
                holder.itemProductType4.setIcon("0")
                holder.itemProductType4.setSKU("SKU: Binh gas 12kg")
            }
            "GAS45" -> {
                holder.itemProductType4.setIcon("0")
                holder.itemProductType4.setSKU("SKU: Binh gas 45kg")
            }
            "TANK12" -> {
                holder.itemProductType4.setIcon("1")
                holder.itemProductType4.setSKU("SKU: Binh gas 12kg")
            }
            "TANK45" -> {
                holder.itemProductType4.setIcon("1")
                holder.itemProductType4.setSKU("SKU: Binh gas 45kg")
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun isReadOnly() {
        isReadOnly = true
    }

    inner class ProductViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var itemProductType4: ItemProductType4 = itemView.findViewById(R.id.itemProduct)

        init {
//            itemRequestType1.setOnClickListener(this)
            if (isReadOnly) {
                itemProductType4.isReadOnly()
            }

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