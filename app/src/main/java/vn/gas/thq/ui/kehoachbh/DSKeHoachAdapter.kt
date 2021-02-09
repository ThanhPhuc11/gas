package vn.gas.thq.ui.kehoachbh

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.customview.ItemRequestType1
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.util.AppDateUtils
import vn.hongha.ga.R

class DSKeHoachAdapter(
    private val mList: MutableList<KeHoachModel>,
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

        var edtPriceGas12: EditText = itemView.findViewById(R.id.edtPriceGas12)
        var edtPriceGas45: EditText = itemView.findViewById(R.id.edtPriceGas45)
        var edtPriceTank12: EditText = itemView.findViewById(R.id.edtPriceTank12)
        var edtPriceTank45: EditText = itemView.findViewById(R.id.edtPriceTank45)

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

            edtPriceGas12.addTextChangedListener(afterTextChanged = {
                mClickListener?.onItemCodePrice(
                    adapterPosition,
                    "GAS12",
                    if (TextUtils.isEmpty(it.toString())) 0 else it.toString().toInt()
                )
            })
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