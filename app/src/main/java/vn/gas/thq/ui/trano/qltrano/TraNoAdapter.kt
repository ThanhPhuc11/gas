package vn.gas.thq.ui.trano.qltrano

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.util.AppDateUtils.*
import vn.gas.thq.util.CommonUtils
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
        holder.itemBinding.tvCustName.text = obj.customerName ?: ""
        holder.itemBinding.tvLoaiTacDong.text = if (obj.auditType == null) "Bán lẻ" else "Trả nợ"
        holder.itemBinding.tvLoaiCongNo.text = obj.debitType?.let { mapTypeLoaiCongNo(it) } ?: ""
        holder.itemBinding.tvKHTra.text =
            CommonUtils.priceWithoutDecimal(obj.debitAmount?.toDouble()) ?: ""
        holder.itemBinding.tvCongNoTraTruoc.text =
            CommonUtils.priceWithoutDecimal(obj.debitAmountBf?.toDouble()) ?: ""
        holder.itemBinding.tvCongNoTraSau.text =
            CommonUtils.priceWithoutDecimal(obj.debitAmountAf?.toDouble()) ?: ""
        holder.itemBinding.tvNguoiThucHien.text = obj.createdBy ?: ""
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun mapTypeLoaiCongNo(type: String): String {
        return when (type) {
            "1" -> "Công nợ vỏ bán lẻ 12kg"
            "2" -> "Công nợ vỏ bán lẻ 45kg"
            "3" -> "Công nợ tiền bán lẻ"
            "4" -> "Công nợ tiền bán đại lý"
            "5" -> "Công nợ vỏ bán đại lý 12kg"
            "6" -> "Công nợ vỏ bán đại lý 45kg"
            else -> ""
        }
    }

    inner class TraNoViewHolder(val itemBinding: ItemHistoryTraNoBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}