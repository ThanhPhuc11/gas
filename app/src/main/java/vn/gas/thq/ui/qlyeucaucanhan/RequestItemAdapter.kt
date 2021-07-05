package vn.gas.thq.ui.qlyeucaucanhan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_item_personal_request.view.*
import vn.gas.thq.customview.ItemRequestType1
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.CommonUtils.getNameLevel
import vn.hongha.ga.R
import vn.hongha.ga.databinding.ItemHistoryVacationBinding
import vn.hongha.ga.databinding.LayoutItemPersonalRequestBinding

class RequestItemAdapter(
    private val mList: MutableList<BussinesRequestModel>,
    private val loaiYC: String?,
    private val enumStatus: MutableList<StatusValueModel>?
) :
    RecyclerView.Adapter<RequestItemAdapter.RequestViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val itemBinding =
            LayoutItemPersonalRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RequestViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val obj: BussinesRequestModel = mList[position]

        if (obj.order_id != null)
            holder.itemBinding.tvOrderId.text = obj.order_id.toString()
        else holder.itemBinding.tvOrderId.text = obj.stock_trans_id?.toString()

        if (obj.approve_status == null) {
            holder.itemBinding.llCustomer.isVisible = false
            when (obj.status) {
                0 -> {
                    setTrangThai("0", holder.itemBinding.tvStatus, holder.itemView.context)
                }
                1 -> {
                    setTrangThai("1", holder.itemBinding.tvStatus, holder.itemView.context)
                }
                2 -> {
                    setTrangThai("2", holder.itemBinding.tvStatus, holder.itemView.context)
                }
                3 -> {
                    setTrangThai("3", holder.itemBinding.tvStatus, holder.itemView.context)
                }
            }
        } else {
            setColorTrangThaiBanLe(
                R.color.blue_2C5181,
                holder.itemBinding.tvStatus,
                holder.itemView.context
            )
            if (obj.status == 8 || obj.status == 9) {
                setColorTrangThaiBanLe(
                    R.color.blue_14AFB4,
                    holder.itemBinding.tvStatus,
                    holder.itemView.context
                )
            } else if (obj.approve_status!!.contains("4")) {
                setColorTrangThaiBanLe(
                    R.color.red_DB4755,
                    holder.itemBinding.tvStatus,
                    holder.itemView.context
                )
            }
            holder.itemBinding.llCustomer.isVisible = true
            holder.itemBinding.tvCustName.text = obj.customer_name
            holder.itemBinding.tvStatus.text =
                enumStatus?.firstOrNull { it.value!!.contains("${obj.status};${obj.approve_status}") }?.name
                    ?: "${obj.status};${obj.approve_status}"
        }


        holder.itemBinding.tvSaleTime.text = AppDateUtils.changeDateFormat(
            AppDateUtils.FORMAT_6,
            AppDateUtils.FORMAT_1,
            obj.created_date
        )
        holder.itemBinding.tvName.text = obj.staff_name
        holder.itemBinding.tvLoaiYC.text = obj.sale_order_type ?: "Xuất kho"

        holder.itemBinding.llWrap.visibility = View.GONE
        holder.itemBinding.tvNguoiDuyet.visibility = View.VISIBLE
        holder.itemBinding.tvNguoiDuyetMore.visibility = View.GONE
        if (obj.approve_staffs != null) {
            holder.itemBinding.llWrap.visibility = View.VISIBLE
            if (obj.approve_staffs?.size == 0) {
                holder.itemBinding.llWrap.visibility = View.GONE
            }
            if (obj.approve_staffs?.size!! > 0) {
                holder.itemBinding.tvNguoiDuyet.text =
                    obj.approve_staffs?.get(0) ?: "- -"

                var strNguoiDuyetMore = ""
                for (i in 0 until obj.approve_staffs!!.size) {
                    strNguoiDuyetMore += "+ ${obj.approve_staffs!![i]}\n"
                }
                holder.itemBinding.tvNguoiDuyetMore.text = strNguoiDuyetMore.trim()
            }
//            var strNguoiDuyetMore = ""
//            if (obj.approve_staffs?.size!! > 1) {
////            obj.approve_staffs?.forEach {
////                strNguoiDuyetMore += "\n" +
////                        "\n${it}"
////            }
//                for (i in 0 until obj.approve_staffs!!.size) {
//                    strNguoiDuyetMore += "+ ${obj.approve_staffs!![i]}\n"
//                }
//                holder.itemRequestType1.getNguoiDuyetMore().text = strNguoiDuyetMore.trim()
//            }
        }

        holder.itemBinding.llWrapApproveLevel.visibility = View.GONE
        if (obj.approve_levels != null) {
            holder.itemBinding.llWrapApproveLevel.visibility = View.VISIBLE
            var strCapDuyet = ""
            obj.approve_levels?.toList()?.forEachIndexed { index, it1 ->
                if (it1.toString() != "0") {
                    strCapDuyet += "${CommonUtils.getNameProductType(index + 1)}: ${
                        getNameLevel(
                            it1.toString().toInt()
                        )
                    }\n"
                }
            }
            if (strCapDuyet.isEmpty()) {
                holder.itemBinding.llWrapApproveLevel.visibility = View.GONE
            } else {
                holder.itemBinding.llWrapApproveLevel.visibility = View.VISIBLE
                holder.itemBinding.tvCapDuyet.text = strCapDuyet.trim()
            }

            holder.itemBinding.tvName.text = obj.sale_line ?: ""
        }

        holder.itemView.setOnClickListener {
            mClickListener?.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun setTrangThai(trangThai: String?, tvStatus: TextView, context: Context) {
        when (trangThai) {
            "0" -> {
                tvStatus.text = "Đã huỷ"
                tvStatus.setTextColor(context.resources.getColor(R.color.red_DB4755))
            }
            "1" -> {
                tvStatus.text = "Chờ duyệt"
                tvStatus.setTextColor(context.resources.getColor(R.color.blue_2C5181))
            }
            "3" -> {
                tvStatus.text = "Đã duyệt"
                tvStatus.setTextColor(context.resources.getColor(R.color.blue_14AFB4))
            }
            "2" -> {
                tvStatus.text = "Từ chối"
                tvStatus.setTextColor(context.resources.getColor(R.color.red_DB4755))
            }
        }
    }

    private fun setColorTrangThaiBanLe(color: Int, tvStatus: TextView, context: Context) {
        tvStatus.setTextColor(context.resources.getColor(color))
    }

//    inner class RequestViewHolder constructor(itemView: View) :
//        RecyclerView.ViewHolder(itemView),
//        View.OnClickListener {
//        var itemRequestType1: ItemRequestType1 = itemView.findViewById(R.id.itemRequest)
//
//        init {
//            itemRequestType1.setOnClickListener(this)
//            itemRequestType1.getllWrap().setOnClickListener {
//                if (!itemRequestType1.getNguoiDuyetMore().isVisible) {
//                    itemRequestType1.getNguoiDuyetMore().visibility = View.VISIBLE
//                    itemRequestType1.getNguoiDuyet().visibility = View.GONE
//                } else {
//                    itemRequestType1.getNguoiDuyetMore().visibility = View.GONE
//                    itemRequestType1.getNguoiDuyet().visibility = View.VISIBLE
//                }
//            }
//        }
//
//        override fun onClick(p0: View?) {
//            mClickListener?.onItemClick(p0, adapterPosition)
//        }
//    }

    inner class RequestViewHolder(val itemBinding: LayoutItemPersonalRequestBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
//        fun onItemIncreaseClick(view: View?, position: Int)
//        fun onItemDecreaseClick(view: View?, position: Int)
    }
}