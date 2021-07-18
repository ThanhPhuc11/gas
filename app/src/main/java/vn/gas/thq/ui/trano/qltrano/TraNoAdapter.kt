package vn.gas.thq.ui.trano.qltrano

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.util.AppDateUtils.*
import vn.hongha.ga.R

class TraNoAdapter(
    private val mList: MutableList<HistoryTraNoModel>,
//    private val context: Context
) :
    RecyclerView.Adapter<TraNoAdapter.SangChietViewHolder>() {
    var mClickListener: ItemClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SangChietViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_sang_chiet, parent, false)

        return SangChietViewHolder(view)
    }

    override fun onBindViewHolder(holder: SangChietViewHolder, position: Int) {
        val obj: HistoryTraNoModel = mList[position]

        holder.tvDate.text = changeDateFormat(FORMAT_6, FORMAT_1, obj.createdDate)
        holder.tvUseKHL.text = obj.amountGasUsed?.toString()
        holder.tvUseGas.text = obj.amountGasRemainUsed?.toString()
        holder.tvUseGasKK.text = obj.amountGasKkUsed?.toString()
        holder.tvGas12.text = obj.amountGas12?.toString()
        holder.tvGas45.text = obj.amountGas45?.toString()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class SangChietViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var tvUseKHL: TextView = itemView.findViewById(R.id.tvUseKHL)
        var tvUseGas: TextView = itemView.findViewById(R.id.tvUseGas)
        var tvUseGasKK: TextView = itemView.findViewById(R.id.tvUseGasKK)
        var tvGas12: TextView = itemView.findViewById(R.id.tvGas12)
        var tvGas45: TextView = itemView.findViewById(R.id.tvGas45)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
//            mClickListener?.onItemClick(p0, adapterPosition)
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