package vn.gas.thq.ui.nhapkho

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.customview.ItemProductType4
import vn.gas.thq.model.ProductModel
import vn.hongha.ga.R

class ProductImportAdapter(private val mList: MutableList<ProductModel>) :
    RecyclerView.Adapter<ProductImportAdapter.ProductViewHolder>() {
    lateinit var mClickListener: ItemClickListener

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_type_5, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val obj: ProductModel = mList[position]
        holder.tvProductName.text = obj.name ?: "- -"
        holder.tvSLTrenXe.text = obj.quantity?.toString()

        when (obj.code) {
            "GAS12", "GAS45" -> {
                holder.imgIconType.setImageResource(R.drawable.ic_fire)
            }
            "TANK12", "TANK45" -> {
                holder.imgIconType.setImageResource(R.drawable.ic_vo_gas)
            }
        }
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
        RecyclerView.ViewHolder(itemView) {
        var tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        var tvSLTrenXe: TextView = itemView.findViewById(R.id.tvSLTrenXe)
        var edtSLNhapKho: EditText = itemView.findViewById(R.id.edtSLNhapKho)
        var edtGasRemainPrice: EditText = itemView.findViewById(R.id.edtGasRemainPrice)
        var imgIconType: ImageView = itemView.findViewById(R.id.imgIconType)

        init {
//            itemProductType.setOnClickListener(this)
            edtSLNhapKho.addTextChangedListener(afterTextChanged = {
                mClickListener.onItemSLChanged(
                    adapterPosition,
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })
        }

//        override fun onClick(p0: View?) {
//            mClickListener.onItemTopClick(p0, adapterPosition)
//        }
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        //        fun onItemTopClick(view: View?, position: Int)
        fun onItemSLChanged(position: Int, count: Int)
//        fun onItemIncreaseClick(view: View?, position: Int)
//        fun onItemDecreaseClick(view: View?, position: Int)
    }
}