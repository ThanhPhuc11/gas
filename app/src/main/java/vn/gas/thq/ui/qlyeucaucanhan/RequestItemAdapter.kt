package vn.gas.thq.ui.qlyeucaucanhan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.customview.ItemRequestType1
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.util.AppDateUtils
import vn.hongha.ga.R

class RequestItemAdapter(
    private val mList: MutableList<BussinesRequestModel>,
    private val loaiYC: String?,
    private val enumStatus: MutableList<StatusValueModel>?
) :
    RecyclerView.Adapter<RequestItemAdapter.RequestViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request_ycxk, parent, false)

        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val obj: BussinesRequestModel = mList[position]

        holder.itemRequestType1.setOrderId(obj.order_id?.toString())

        if (obj.approve_status == null) {
            holder.itemRequestType1.isVisibleKH(false)
            when (obj.status) {
                0 -> {
                    holder.itemRequestType1.setTrangThai("0")
                }
                1 -> {
                    holder.itemRequestType1.setTrangThai("1")
                }
                2 -> {
                    holder.itemRequestType1.setTrangThai("2")
                }
                3 -> {
                    holder.itemRequestType1.setTrangThai("3")
                }
            }
        } else {
            holder.itemRequestType1.setColorTrangThaiBanLe(R.color.yellow_FFBF00)
            if (obj.status == 8 || obj.status == 9) {
                holder.itemRequestType1.setColorTrangThaiBanLe(R.color.blue_14AFB4)
            } else if (obj.approve_status.contains("4")) {
                holder.itemRequestType1.setColorTrangThaiBanLe(R.color.red_DB4755)
            }
            holder.itemRequestType1.isVisibleKH(true)
            holder.itemRequestType1.setTenKH(obj.customer_name)
//            holder.itemRequestType1.setTrangThaiBanLe("${obj.status};${obj.approve_status}")
//            enumStatus?.forEach {
//                if (it.value == "${obj.status};${obj.approve_status}") {
//                    holder.itemRequestType1.setTrangThaiBanLe(it.name)
//                    return@forEach
//                }
//            }
            holder.itemRequestType1.setTrangThaiBanLe(
                enumStatus?.firstOrNull { it.value!!.contains("${obj.status};${obj.approve_status}") }?.name
                    ?: "${obj.status};${obj.approve_status}"
            )
        }


        holder.itemRequestType1.setThoiGian(
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_6,
                AppDateUtils.FORMAT_1,
                obj.created_date
            )
        )
        holder.itemRequestType1.setTen(obj.staff_name)
        holder.itemRequestType1.setLoaiYC(loaiYC)
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
        var itemRequestType1: ItemRequestType1 = itemView.findViewById(R.id.itemRequest)

        init {
            itemRequestType1.setOnClickListener(this)
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