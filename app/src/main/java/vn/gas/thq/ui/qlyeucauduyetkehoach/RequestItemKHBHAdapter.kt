package vn.gas.thq.ui.qlyeucauduyetkehoach

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.util.AppDateUtils
import vn.hongha.ga.R

class RequestItemKHBHAdapter(
    private val mList: MutableList<KHBHOrderModel>,
) :
    RecyclerView.Adapter<RequestItemKHBHAdapter.RequestViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_khbh_request, parent, false)

        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val obj: KHBHOrderModel = mList[position]

        holder.tvOrderId.text = obj.planId.toString()
        holder.tvTram.text = obj.shopName.toString()
        holder.tvTuyenBH.text = obj.saleLineName.toString()
        holder.tvLXBH.text = obj.staffName.toString()
        holder.tvSaleTime.text = (
                AppDateUtils.changeDateFormat(
                    AppDateUtils.FORMAT_6,
                    AppDateUtils.FORMAT_1,
                    obj.effectDate
                )
                )
        holder.tvStatus.text = obj.statusName.toString()
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
        var tvTram: TextView = itemView.findViewById(R.id.tvTram)
        var tvTuyenBH: TextView = itemView.findViewById(R.id.tvTuyenBH)
        var tvLXBH: TextView = itemView.findViewById(R.id.tvLXBH)
        var tvSaleTime: TextView = itemView.findViewById(R.id.tvSaleTime)
        var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        var llWrap: LinearLayout = itemView.findViewById(R.id.llWrap)

        init {
            llWrap.setOnClickListener(this)
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