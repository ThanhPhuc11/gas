package vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet

import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.customview.CustomArrayAdapter
import vn.gas.thq.ui.kehoachbh.KeHoachModel
import vn.gas.thq.util.CallBackChange
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.NumberTextWatcher
import vn.hongha.ga.R

class DSDetailKeHoachAdapter(
    private val mList: MutableList<KeHoachModel>,
) :
    RecyclerView.Adapter<DSDetailKeHoachAdapter.RequestViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_init_ke_hoach_ban_hang_v2, parent, false)

        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val obj: KeHoachModel = mList[position]
//        holder.edtLXBH.setText(obj.custName)
        holder.tvCustName.text = obj.custName

        holder.edtQuantityGas12.isFocusable = false
        holder.edtPriceGas12.isFocusable = false
        holder.edtQuantityGas45.isFocusable = false
        holder.edtPriceGas45.isFocusable = false
        holder.edtQuantityTank12.isFocusable = false
        holder.edtPriceTank12.isFocusable = false
        holder.edtQuantityTank45.isFocusable = false
        holder.edtPriceTank45.isFocusable = false

        obj.item.forEach {
            when (it.productCode) {
                "GAS12" -> {
                    holder.edtQuantityGas12.setText(it.amount.toString())
                    holder.edtPriceGas12.setText(CommonUtils.priceWithoutDecimal(it.price?.toDouble()))
                }
                "GAS45" -> {
                    holder.edtQuantityGas45.setText(it.amount.toString())
                    holder.edtPriceGas45.setText(CommonUtils.priceWithoutDecimal(it.price?.toDouble()))
                }
                "TANK12" -> {
                    holder.edtQuantityTank12.setText(it.amount.toString())
                    holder.edtPriceTank12.setText(CommonUtils.priceWithoutDecimal(it.price?.toDouble()))
                }
                "TANK45" -> {
                    holder.edtQuantityTank45.setText(it.amount.toString())
                    holder.edtPriceTank45.setText(CommonUtils.priceWithoutDecimal(it.price?.toDouble()))
                }
            }
        }
//        holder.edtQuantityGas12.clearComposingText()
//        holder.edtPriceGas12.clearComposingText()
//        holder.edtQuantityGas45.clearComposingText()
//        holder.edtPriceGas45.clearComposingText()
//        holder.edtQuantityTank12.clearComposingText()
//        holder.edtPriceTank12.clearComposingText()
//        holder.edtQuantityTank45.clearComposingText()
//        holder.edtPriceTank45.clearComposingText()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

//    fun setCustomer(position: Int, custName: String?) {
//        mList[position].custName = custName ?: ""
//    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun clearText() {

    }

    private fun expandTitle(titleGroup: TextView, container: View) {
        if (container.isVisible) {
            titleGroup.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_right_circle,
                0
            )
            CommonUtils.collapse(container)
        } else {
            titleGroup.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_down_circle,
                0
            )
            CommonUtils.expand(container)
        }
    }

    private fun expandContent(icon: ImageView, container: View) {
        if (container.isVisible) {
            icon.setImageResource(R.drawable.ic_max)
            CommonUtils.collapse(container)
        } else {
            icon.setImageResource(R.drawable.ic_min)
            CommonUtils.expand(container)
        }
    }

    inner class RequestViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var linearWrapContent: LinearLayout = itemView.findViewById(R.id.llWrapContent)
        var llWarpFullContent: LinearLayout = itemView.findViewById(R.id.llWarpFullContent)
        var imgCollapse: ImageView = itemView.findViewById(R.id.imgCollapse)

        var tvCustName: TextView = itemView.findViewById(R.id.tvCustName)
        var edtQuantityGas12: EditText = itemView.findViewById(R.id.edtQuantityGas12)
        var edtQuantityGas45: EditText = itemView.findViewById(R.id.edtQuantityGas45)
        var edtQuantityTank12: EditText = itemView.findViewById(R.id.edtQuantityTank12)
        var edtQuantityTank45: EditText = itemView.findViewById(R.id.edtQuantityTank45)

        var edtPriceGas12: AppCompatAutoCompleteTextView = itemView.findViewById(R.id.edtPriceGas12)
        var edtPriceGas45: AppCompatAutoCompleteTextView = itemView.findViewById(R.id.edtPriceGas45)
        var edtPriceTank12: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.edtPriceTank12)
        var edtPriceTank45: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.edtPriceTank45)

        init {
            tvCustName.setOnClickListener { expandTitle(tvCustName, linearWrapContent) }

            imgCollapse.setOnClickListener { expandContent(imgCollapse, llWarpFullContent) }

//            edtQuantityGas12.addTextChangedListener(afterTextChanged = {
//                mClickListener?.onItemCodeQuantity(
//                    adapterPosition,
//                    "GAS12",
//                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
//                )
//            })
//            edtQuantityGas45.addTextChangedListener(afterTextChanged = {
//                mClickListener?.onItemCodeQuantity(
//                    adapterPosition,
//                    "GAS45",
//                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
//                )
//            })
//            edtQuantityTank12.addTextChangedListener(afterTextChanged = {
//                mClickListener?.onItemCodeQuantity(
//                    adapterPosition,
//                    "TANK12",
//                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
//                )
//            })
//            edtQuantityTank45.addTextChangedListener(afterTextChanged = {
//                mClickListener?.onItemCodeQuantity(
//                    adapterPosition,
//                    "TANK45",
//                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
//                )
//            })

//            edtPriceGas12.setAdapter(suggestAdapter)
//            edtPriceGas12.addTextChangedListener(
//                NumberTextWatcher(edtPriceGas12, suggestAdapter,
//                    object : CallBackChange {
//                        override fun afterEditTextChange(it: Editable?) {
//                            mClickListener?.onItemCodePrice(
//                                adapterPosition,
//                                "GAS12",
//                                if (TextUtils.isEmpty(it.toString())) 0 else CommonUtils.getIntFromStringDecimal(
//                                    it.toString()
//                                )
//                            )
//                        }
//
//                    })
//            )
//            edtPriceGas45.setAdapter(suggestAdapter)
//            edtPriceGas45.addTextChangedListener(
//                NumberTextWatcher(edtPriceGas45, suggestAdapter,
//                    object : CallBackChange {
//                        override fun afterEditTextChange(it: Editable?) {
//                            mClickListener?.onItemCodePrice(
//                                adapterPosition,
//                                "GAS45",
//                                if (TextUtils.isEmpty(it.toString())) 0 else CommonUtils.getIntFromStringDecimal(
//                                    it.toString()
//                                )
//                            )
//                        }
//                    })
//            )
//            edtPriceTank12.setAdapter(suggestAdapter)
//            edtPriceTank12.addTextChangedListener(
//                NumberTextWatcher(edtPriceTank12, suggestAdapter,
//                    object : CallBackChange {
//                        override fun afterEditTextChange(it: Editable?) {
//                            mClickListener?.onItemCodePrice(
//                                adapterPosition,
//                                "TANK12",
//                                if (TextUtils.isEmpty(it.toString())) 0 else CommonUtils.getIntFromStringDecimal(
//                                    it.toString()
//                                )
//                            )
//                        }
//                    })
//            )
//            edtPriceTank45.setAdapter(suggestAdapter)
//            edtPriceTank45.addTextChangedListener(
//                NumberTextWatcher(edtPriceTank45, suggestAdapter,
//                    object : CallBackChange {
//                        override fun afterEditTextChange(it: Editable?) {
//                            mClickListener?.onItemCodePrice(
//                                adapterPosition,
//                                "TANK45",
//                                if (TextUtils.isEmpty(it.toString())) 0 else CommonUtils.getIntFromStringDecimal(
//                                    it.toString()
//                                )
//                            )
//                        }
//                    })
//            )
        }

        override fun onClick(p0: View?) {
            mClickListener?.onItemClick(adapterPosition)
        }
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
        fun onItemChange(position: Int, count: Int)
        fun onItemCodeQuantity(position: Int, code: String, quantity: Int)
        fun onItemCodePrice(position: Int, code: String, price: Int)
    }
}