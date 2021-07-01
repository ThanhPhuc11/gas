package vn.gas.thq.ui.pheduyetgiabanle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.util.AppDateUtils
import vn.hongha.ga.R

class ListYCBanLeAdapter(
    private val mList: MutableList<BussinesRequestModel>,
    private val enumStatus: MutableList<StatusValueModel>?,
    private val context: Context
//    private val loaiYC: String?
) :
    RecyclerView.Adapter<ListYCBanLeAdapter.RequestViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request_duyet_gia_ban_le, parent, false)

        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val obj: BussinesRequestModel = mList[position]

//        when (obj.status) {
//            0 -> {
//                holder.itemRequestType1.setTrangThai("0")
//            }
//            1 -> {
//                holder.itemRequestType1.setTrangThai("1")
//            }
//            2 -> {
//                holder.itemRequestType1.setTrangThai("2")
//            }
//            3 -> {
//                holder.itemRequestType1.setTrangThai("3")
//            }
//        }

        holder.tvOrderId.text = obj.order_id?.toString()
        holder.tvTuyenXe.text = obj.sale_line ?: "- -"
        holder.tvCustName.text = obj.customer_name ?: "- -"
        holder.tvSaleOrderType.text = obj.sale_order_type ?: "- -"
        holder.tvDate.text = AppDateUtils.changeDateFormat(
            AppDateUtils.FORMAT_6,
            AppDateUtils.FORMAT_1,
            obj.created_date
        )
        holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.blue_2C5181))
        if (obj.status == 8 || obj.status == 9) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.blue_14AFB4))
        } else if (obj.approve_status!!.contains("4")) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red_DB4755))
        }
//        enumStatus?.forEach {
//            if (it.value == "${obj.status};${obj.approve_status}") {
//                holder.tvStatus.text = it.name
//                return
//            } else {
//                holder.tvStatus.text = obj.approve_status
//            }
//        }
        holder.tvStatus.text =
            enumStatus?.firstOrNull { it.value!!.contains("${obj.status};${obj.approve_status}") }?.name
                ?: "${obj.status};${obj.approve_status}"

        var strCapDuyet = ""
        holder.llWrap.visibility = View.VISIBLE
//        obj.approve_levels?.forEach { it1 ->
//            if (it1.level != 0) {
//                strCapDuyet += "${getNameProductType(it1.productType!!)}: ${getNameLevel(it1.level!!)}\n"
//            }
//        }
        obj.approve_levels?.toList()?.forEachIndexed { index, it1 ->
            if (it1.toString() != "0") {
                strCapDuyet += "${getNameProductType(index + 1)}: ${getNameLevel(it1.toString().toInt())}\n"
            }
        }
        if (strCapDuyet.isEmpty()) {
            holder.llWrap.visibility = View.GONE
        } else {
            holder.llWrap.visibility = View.VISIBLE
            holder.tvCapDuyet.text = strCapDuyet.trim()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun getNameProductType(type: Int): String {
        return when (type) {
            1 -> "Giá khí"
            2 -> "Giá vỏ"
            3 -> "Công nợ"
            else -> ""
        }
    }

    private fun getNameLevel(level: Int): String {
        return when (level) {
            2 -> "NVKD"
            3 -> "PGD KD"
            4 -> "TT"
            7 -> "CÔNG TY"
            else -> ""
        }
    }

    inner class RequestViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        var tvTuyenXe: TextView = itemView.findViewById(R.id.tvTuyenXe)
        var tvCustName: TextView = itemView.findViewById(R.id.tvCustName)
        var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        var llWrap: LinearLayout = itemView.findViewById(R.id.llWrap)
        var tvCapDuyet: TextView = itemView.findViewById(R.id.tvCapDuyet)
        var tvNguoiDuyetMore: TextView = itemView.findViewById(R.id.tvNguoiDuyetMore)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var tvSaleOrderType: TextView = itemView.findViewById(R.id.tvSaleOrderType)

        init {
            itemView.setOnClickListener(this)
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
//        fun onItemIncreaseClick(view: View?, position: Int)
//        fun onItemDecreaseClick(view: View?, position: Int)
    }
}