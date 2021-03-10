package vn.gas.thq.ui.nhapkho

import android.text.Editable
import android.text.InputFilter
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
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.DecimalDigitsInputFilter
import vn.hongha.ga.R

class ProductImportAdapter(private val mList: MutableList<ProductModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var mClickListener: ItemClickListener

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_type_5_gas, parent, false)
            ProductViewHolder2(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_type_5, parent, false)

            ProductViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder2) {
            val obj: ProductModel = mList[position]
            holder.tvProductName.text = obj.name ?: "- -"
            holder.tvSLTrenXe.text = obj.quantity?.toString()
//            holder.edtGasRemainPrice1.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(3, 1))
        } else if (holder is ProductViewHolder) {
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
        }
    }

//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val obj: ProductModel = mList[position]
//        holder.tvProductName.text = obj.name ?: "- -"
//        holder.tvSLTrenXe.text = obj.quantity?.toString()
//
//        when (obj.code) {
//            "GAS12", "GAS45" -> {
//                holder.imgIconType.setImageResource(R.drawable.ic_fire)
//            }
//            "TANK12", "TANK45" -> {
//                holder.imgIconType.setImageResource(R.drawable.ic_vo_gas)
//            }
//        }
////        holder.imgIcon.setImageResource(menu.resDrawable)
////        holder.imgIcon.setImageResource(R.drawable.ic_menu_2)
//    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList[position].code == "GAS_REMAIN") {
            1
        } else {
            0
        }
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

    inner class ProductViewHolder2 constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {


        var tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        var tvSLTrenXe: TextView = itemView.findViewById(R.id.tvSLTrenXe)
        var edtSLNhapKho2: EditText = itemView.findViewById(R.id.edtSLNhapKho2)
        var edtGasRemainPrice1: EditText = itemView.findViewById(R.id.edtGasRemainPrice1)
        var imgIconType: ImageView = itemView.findViewById(R.id.imgIconType)

        init {
//            itemProductType.setOnClickListener(this)
            edtSLNhapKho2.addTextChangedListener(afterTextChanged = {
                mClickListener.onItemSLChangedFloat(
                    adapterPosition,
                    getRealNumberFloat(it)
                )
            })

            edtSLNhapKho2.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(3, 1))
        }

        private fun getRealNumberFloat(view: Editable?): Float {
            return if (TextUtils.isEmpty(
                    view.toString().trim()
                )
            ) 0f else CommonUtils.getFloatFromStringDecimal(
                view.toString()
                    .trim()
            )
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
        fun onItemSLChangedFloat(position: Int, count: Float)
//        fun onItemIncreaseClick(view: View?, position: Int)
//        fun onItemDecreaseClick(view: View?, position: Int)
    }
}