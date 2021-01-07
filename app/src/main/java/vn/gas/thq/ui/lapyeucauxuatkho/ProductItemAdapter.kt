package vn.gas.thq.ui.lapyeucauxuatkho

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.customview.ItemProductType4
import vn.gas.thq.model.ProductModel
import vn.hongha.ga.R

class ProductItemAdapter(private val mList: MutableList<ProductModel>) :
    RecyclerView.Adapter<ProductItemAdapter.ProductViewHolder>() {
    lateinit var mClickListener: ItemClickListener

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_ycxk, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val obj: ProductModel = mList[position]
        holder.itemProductType.setTen(obj.name)
        holder.itemProductType.setSKU(obj.description)
        holder.itemProductType.setIcon("1")
//        holder.imgIcon.setImageResource(menu.resDrawable)
//        holder.imgIcon.setImageResource(R.drawable.ic_menu_2)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

//    fun getQuantityFromCode(): MutableList<ProductModel> {
//        var listTemp = mutableListOf<ProductModel>()
//        for (obj: ProductModel in mList) {
//            listTemp.add(ProductModel("", obj.code, "", "", ))
//        }
//    }

    inner class ProductViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var itemProductType: ItemProductType4 = itemView.findViewById(R.id.itemProduct)

        init {
//            itemProductType.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mClickListener.onItemTopClick(p0, adapterPosition)
        }
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemTopClick(view: View?, position: Int)
//        fun onItemIncreaseClick(view: View?, position: Int)
//        fun onItemDecreaseClick(view: View?, position: Int)
    }
}