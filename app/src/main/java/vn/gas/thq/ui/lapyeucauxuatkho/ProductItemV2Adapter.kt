package vn.gas.thq.ui.lapyeucauxuatkho

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
import kotlinx.android.synthetic.main.item_product_type_4.view.*
import vn.gas.thq.customview.ItemProductType4
import vn.gas.thq.model.ProductModel
import vn.hongha.ga.R

class ProductItemV2Adapter(private val mList: MutableList<ProductModel>) :
    RecyclerView.Adapter<ProductItemV2Adapter.ProductViewHolder>() {
    lateinit var mClickListener: ItemClickListener

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_type_4, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val obj: ProductModel = mList[position]
        holder.tvProductName.text = obj.name
        holder.tvProductDes.text = obj.description
        holder.edtSL.setText(obj.quantity.toString())
//        holder.itemProductType.setIcon("1")
        when (obj.code) {
            "GAS12" -> {
                holder.imgIconType.setImageResource(R.drawable.ic_fire)
                holder.tvProductDes.text = "SKU: Bình gas 12kg"
            }
            "GAS45" -> {
                holder.imgIconType.setImageResource(R.drawable.ic_fire)
                holder.tvProductDes.text = "SKU: Bình gas 45kg"
            }
            "TANK12" -> {
                holder.imgIconType.setImageResource(R.drawable.ic_vo_gas)
                holder.tvProductDes.text = "SKU: Bình gas 12kg"
            }
            "TANK45" -> {
                holder.imgIconType.setImageResource(R.drawable.ic_vo_gas)
                holder.tvProductDes.text = "SKU: Bình gas 45kg"
            }
        }

//        holder.edtSL.addTextChangedListener(afterTextChanged = {
//            if (holder.edtSL.isFocused)
//                mClickListener.onItemSLChanged(
//                    position,
//                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
//                )
////            holder.edtSL.isSelected = false
//        })


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

    fun setSL(position: Int, sl: Int) {
        mList[position].quantity = sl
        notifyDataSetChanged()
    }

    inner class ProductViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //        var itemProductType: ItemProductType4 = itemView.findViewById(R.id.itemProduct)
        var imgIconType: ImageView = itemView.findViewById(R.id.imgIconType)
        var tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        var tvProductDes: TextView = itemView.findViewById(R.id.tvProductDes)
        var edtSL: EditText = itemView.findViewById(R.id.edtSL)
        var imgGiam: ImageView = itemView.findViewById(R.id.imgGiam)
        var imgTang: ImageView = itemView.findViewById(R.id.imgTang)

        init {

            imgTang.setOnClickListener {
                val slHienTai = edtSL.text.toString()
                if (slHienTai.toInt() < 999) {
                    edtSL.setText((slHienTai.toInt() + 1).toString())
                    mClickListener.onItemSLChanged(
                        adapterPosition,
                        if (TextUtils.isEmpty((slHienTai.toInt() + 1).toString())) 0 else (slHienTai.toInt() + 1).toString()
                            .toInt()
                    )
                }
            }

            imgGiam.setOnClickListener {
                val slHienTai = edtSL.text.toString()
                if (slHienTai.toInt() > 0) {
                    edtSL.setText((slHienTai.toInt() - 1).toString())

                    mClickListener.onItemSLChanged(
                        adapterPosition,
                        if (TextUtils.isEmpty((slHienTai.toInt() - 1).toString())) 0 else (slHienTai.toInt() - 1).toString()
                            .toInt()
                    )
                }
            }

            edtSL.addTextChangedListener(afterTextChanged = {
                if (edtSL.isFocused)
                    mClickListener.onItemSLChanged(
                        adapterPosition,
                        if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                    )
//            holder.edtSL.isSelected = false
            })


//            itemProductType.setOnClickListener(this)
//            itemProductType.getViewSL()?.addTextChangedListener(afterTextChanged = {
//                mClickListener.onItemSLChanged(
//                    adapterPosition,
//                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
//                )
//            })

//            itemProductType.getViewGiam().setOnClickListener {
//                val slHienTai = itemProductType.getViewSL()?.text.toString()
//                if (slHienTai.toInt() > 0) {
//                    itemProductType.getViewSL()?.setText((slHienTai.toInt() - 1).toString())
//                    mClickListener.onItemSLChanged(
//                        adapterPosition,
////                        if (TextUtils.isEmpty((slHienTai.toInt() - 1).toString())) 0 else it.toString()
////                            .toInt()
//                        slHienTai.toInt() - 1
//                    )
//                }
//            }
//
//            itemProductType.getViewTang().setOnClickListener {
//                val slHienTai = itemProductType.getViewSL()?.text.toString()
//                if (slHienTai.toInt() < 999) {
//                    itemProductType.getViewSL()?.setText((slHienTai.toInt() + 1).toString())
//                    mClickListener.onItemSLChanged(
//                        adapterPosition,
////                        if (TextUtils.isEmpty((slHienTai.toInt() + 1).toString())) 0 else it.toString()
////                            .toInt()
//                        slHienTai.toInt() + 1
//                    )
//                }
//            }
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