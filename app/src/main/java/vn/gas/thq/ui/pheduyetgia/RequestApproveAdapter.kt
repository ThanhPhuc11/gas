package vn.gas.thq.ui.pheduyetgia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.hongha.ga.R

class RequestApproveAdapter(
    private val mList: MutableList<BussinesRequestModel>,
    private val enumStatus: MutableList<StatusValueModel>?
//    private val loaiYC: String?
) :
    RecyclerView.Adapter<RequestApproveAdapter.RequestViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request_duyet_gia, parent, false)

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
        holder.tvLXBH.text = obj.staff_name
        holder.tvCustName.text = obj.customer_name
        holder.tvDate.text = obj.created_date
        enumStatus?.forEach {
            if (it.value == "${obj.status};${obj.approve_status}") {
                holder.tvStatus.text = it.name
                return
            } else {
                holder.tvStatus.text = obj.approve_status
            }
        }

//        holder.itemRequestType1.setLoaiYC(loaiYC)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class RequestViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        var tvLXBH: TextView = itemView.findViewById(R.id.tvLXBH)
        var tvCustName: TextView = itemView.findViewById(R.id.tvCustName)
        var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)

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