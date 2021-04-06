package vn.gas.thq.ui.nhapkho

import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.model.ProductNhapKhoV2Model
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.DecimalDigitsInputFilter
import vn.hongha.ga.R
import kotlin.math.roundToInt

class ProductImportAdapter(private val mList: MutableList<ProductNhapKhoV2Model>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var mClickListener: ItemClickListener
    private var gia: Int = 0

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
            val obj: ProductNhapKhoV2Model = mList[position]
            holder.tvProductName.text = obj.name ?: "- -"
            holder.tvSLTrenXe.text = "${obj.quantity?.toString()?.replace(".", ",")} KG"
            holder.edtSLNhapKho2.setText(obj.quantity?.toString()?.replace(".", ","))
            holder.edtSLNhapKho2.requestFocus()
            holder.edtSLNhapKho2.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (getRealNumberFloat(holder.edtSLNhapKho2.text) != null) {
                        var x = getRealNumberFloat(
                            holder.edtSLNhapKho2.text
                        )!! * gia
                        Log.e("Phuc", x.toString())
                        holder.edtGasRemainPrice1.setText(
                            CommonUtils.priceWithoutDecimal(lamTronGasPrice(
                                x.toString().substring(0, x.toString().indexOf("."))
                            ).toDouble())
                        )
                    } else {
                        holder.edtGasRemainPrice1.setText("")
                    }
                    mClickListener.onItemSLChangedFloat(
                        position,
                        getRealNumberFloat(holder.edtSLNhapKho2.text)
                    )
                }
            }
//            holder.edtGasRemainPrice1.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(3, 1))
        } else if (holder is ProductViewHolder) {
            val obj: ProductNhapKhoV2Model = mList[position]
            holder.tvProductName.text = obj.name ?: "- -"
            holder.tvSLTrenXe.text = obj.quantity?.toInt().toString()
            holder.edtSLNhapKho.setText(obj.quantity?.toInt().toString())

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

    fun lamTronGasPrice(oldTongTien: String): Int {
        if (oldTongTien.toInt() < 500) return 0
        else {
            val hangNghin = oldTongTien.toInt() / 1000
            val soLe = oldTongTien.toInt() % 1000
            if (soLe >= 500) {
                return hangNghin * 1000 + 1000
            } else {
                return hangNghin * 1000
            }
        }
    }

    fun setGiaGasRemain(gia: Int) {
        this.gia = gia
    }

    private fun getRealNumberFloat(view: Editable?): Float? {
        return if (TextUtils.isEmpty(
                view.toString().trim()
            )
        ) null else CommonUtils.getFloatFromStringDecimal(
            view.toString()
                .trim()
        )
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
                    if (TextUtils.isEmpty(it.toString())) null else it.toString().toInt()
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
//            edtSLNhapKho2.addTextChangedListener(afterTextChanged = {
//                mClickListener.onItemSLChangedFloat(
//                    adapterPosition,
//                    getRealNumberFloat(it)
//                )
//            })
//            edtSLNhapKho2.setOnFocusChangeListener { v, hasFocus ->
//                if (!hasFocus) {
//                    mClickListener.onItemSLChangedFloat(
//                        adapterPosition,
//                        getRealNumberFloat(edtSLNhapKho2.text)
//                    )
//                }
//            }

            edtSLNhapKho2.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(3, 1))
        }

//        private fun getRealNumberFloat(view: Editable?): Float? {
//            return if (TextUtils.isEmpty(
//                    view.toString().trim()
//                )
//            ) null else CommonUtils.getFloatFromStringDecimal(
//                view.toString()
//                    .trim()
//            )
//        }

//        override fun onClick(p0: View?) {
//            mClickListener.onItemTopClick(p0, adapterPosition)
//        }
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        //        fun onItemTopClick(view: View?, position: Int)
        fun onItemSLChanged(position: Int, count: Int?)
        fun onItemSLChangedFloat(position: Int, count: Float?)
//        fun onItemIncreaseClick(view: View?, position: Int)
//        fun onItemDecreaseClick(view: View?, position: Int)
    }
}