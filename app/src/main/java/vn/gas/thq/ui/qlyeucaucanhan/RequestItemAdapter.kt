package vn.gas.thq.ui.qlyeucaucanhan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.customview.ItemRequestType1
import vn.gas.thq.model.BussinesRequestModel
import vn.hongha.ga.R

class RequestItemAdapter(private val mList: MutableList<BussinesRequestModel>) :
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

        when (obj.status) {
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

        holder.itemRequestType1.setThoiGian(obj.created_date)
        holder.itemRequestType1.setTen(obj.staff_name)
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