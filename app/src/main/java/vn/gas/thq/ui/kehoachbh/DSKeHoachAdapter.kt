package vn.gas.thq.ui.kehoachbh

import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.customview.CustomArrayAdapter
import vn.gas.thq.customview.ItemRequestType1
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CallBackChange
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.NumberTextWatcher
import vn.hongha.ga.R

class DSKeHoachAdapter(
    private val mList: MutableList<KeHoachModel>,
    private val suggestAdapter: CustomArrayAdapter
) :
    RecyclerView.Adapter<DSKeHoachAdapter.RequestViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_init_ke_hoach_ban_hang, parent, false)

        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val obj: KeHoachModel = mList[position]
        holder.edtLXBH.setText(obj.custName)
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

    inner class RequestViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var edtLXBH: EditText = itemView.findViewById(R.id.edtLXBH)
        var edtTanSuat: EditText = itemView.findViewById(R.id.edtTanSuat)
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
            edtLXBH.setOnClickListener(this)
            edtTanSuat.addTextChangedListener(afterTextChanged = {
                mClickListener?.onItemChange(
                    adapterPosition,
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })
            edtQuantityGas12.addTextChangedListener(afterTextChanged = {
                mClickListener?.onItemCodeQuantity(
                    adapterPosition,
                    "GAS12",
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })
            edtQuantityGas45.addTextChangedListener(afterTextChanged = {
                mClickListener?.onItemCodeQuantity(
                    adapterPosition,
                    "GAS45",
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })
            edtQuantityTank12.addTextChangedListener(afterTextChanged = {
                mClickListener?.onItemCodeQuantity(
                    adapterPosition,
                    "TANK12",
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })
            edtQuantityTank45.addTextChangedListener(afterTextChanged = {
                mClickListener?.onItemCodeQuantity(
                    adapterPosition,
                    "TANK45",
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })

            edtPriceGas12.setAdapter(suggestAdapter)
            edtPriceGas12.addTextChangedListener(
                NumberTextWatcher(edtPriceGas12, suggestAdapter,
                    object : CallBackChange {
                        override fun afterEditTextChange(it: Editable?) {
                            mClickListener?.onItemCodePrice(
                                adapterPosition,
                                "GAS12",
                                if (TextUtils.isEmpty(it.toString())) 0 else CommonUtils.getIntFromStringDecimal(it.toString())
                            )
                        }

                    })
            )
            edtPriceGas45.addTextChangedListener(afterTextChanged = {
                mClickListener?.onItemCodePrice(
                    adapterPosition,
                    "GAS45",
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })
            edtPriceTank12.addTextChangedListener(afterTextChanged = {
                mClickListener?.onItemCodePrice(
                    adapterPosition,
                    "TANK12",
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })
            edtPriceTank45.addTextChangedListener(afterTextChanged = {
                mClickListener?.onItemCodePrice(
                    adapterPosition,
                    "TANK45",
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })
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