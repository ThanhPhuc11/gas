package vn.gas.thq.ui.vitri

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.ui.retail.Customer
import vn.hongha.ga.R

class CustomerAdapter(
    private val mList: MutableList<Customer>,
//    private val context: Context
) :
    RecyclerView.Adapter<CustomerAdapter.RequestViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer_update, parent, false)

        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val obj: Customer = mList[position]

        holder.tvCustId.text = obj.customerId
        holder.tvCustName.text = obj.name
        holder.tvAddress.text = obj.address
        holder.imgMarker.visibility = if (obj.lat == null) View.GONE else View.VISIBLE
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
        var imgMarker: ImageView = itemView.findViewById(R.id.imgMarker)
        var tvCustId: TextView = itemView.findViewById(R.id.tvCustId)
        var tvCustName: TextView = itemView.findViewById(R.id.tvCustName)
        var tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        var tvNVKD: TextView = itemView.findViewById(R.id.tvNVKD)

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