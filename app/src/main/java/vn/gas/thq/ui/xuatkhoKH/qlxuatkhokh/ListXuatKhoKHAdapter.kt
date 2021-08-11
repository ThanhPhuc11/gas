package vn.gas.thq.ui.xuatkhoKH.qlxuatkhokh

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.util.AppDateUtils.*
import vn.hongha.ga.databinding.ItemXuatHangBinding

class ListXuatKhoKHAdapter(
    private val mList: MutableList<BussinesRequestModel>,
) :
    RecyclerView.Adapter<ListXuatKhoKHAdapter.TraNoViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TraNoViewHolder {
        val itemBinding =
            ItemXuatHangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TraNoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TraNoViewHolder, position: Int) {
        val obj: BussinesRequestModel = mList[position]
        holder.itemBinding.tvDate.text = changeDateFormat(FORMAT_6, FORMAT_7, obj.created_date)
        holder.itemBinding.tvCustName.text = obj.customer_name ?: ""
        holder.itemBinding.tvLoaiYeuCau.text = obj.sale_order_type ?: ""
        holder.itemBinding.tvTrangThaiXuat.text =
            obj.completeOrder?.let { mapTrangThaiXuat(it) } ?: ""
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun mapTrangThaiXuat(id: Int): String {
        return when (id) {
            2 -> "Chưa xuất"
            3 -> "Hoàn thành xuất"
            4 -> "Xuất một phần"
            else -> ""
        }
    }

    inner class TraNoViewHolder(val itemBinding: ItemXuatHangBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}