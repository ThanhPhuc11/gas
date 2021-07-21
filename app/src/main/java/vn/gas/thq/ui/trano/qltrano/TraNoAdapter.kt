package vn.gas.thq.ui.trano.qltrano

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.util.AppDateUtils.*
import vn.hongha.ga.databinding.ItemHistoryTraNoBinding

class TraNoAdapter(
    private val mList: MutableList<HistoryTraNoModel>,
) :
    RecyclerView.Adapter<TraNoAdapter.TraNoViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TraNoViewHolder {
        val itemBinding =
            ItemHistoryTraNoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TraNoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TraNoViewHolder, position: Int) {
        val obj: HistoryTraNoModel = mList[position]
        holder.itemBinding.tvDate.text = changeDateFormat(FORMAT_6, FORMAT_7, obj.createdDate)
        holder.itemBinding.tvLoaiTacDong.text = obj.auditType ?: ""
        holder.itemBinding.tvLoaiCongNo.text = obj.debitType ?: ""
        holder.itemBinding.tvKHTra.text = obj.debitAmount ?: ""
        holder.itemBinding.tvCongNoTraTruoc.text = obj.debitAmountBf ?: ""
        holder.itemBinding.tvCongNoTraSau.text = obj.debitAmountAf ?: ""
        holder.itemBinding.tvNguoiThucHien.text = obj.createdBy ?: ""
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class TraNoViewHolder(val itemBinding: ItemHistoryTraNoBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}