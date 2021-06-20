package vn.gas.thq.ui.pheduyetgiabanle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.AppDateUtils.FORMAT_6
import vn.gas.thq.util.AppDateUtils.FORMAT_7
import vn.hongha.ga.R

class HistoryAcceptBanLeAdapter(
    private val mList: MutableList<HistoryModel>,
//    private val context: Context
//    private val loaiYC: String?
) : RecyclerView.Adapter<HistoryAcceptBanLeAdapter.RequestViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)

        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val obj: HistoryModel = mList[position]
        holder.tvNgay.text = AppDateUtils.changeDateFormat(FORMAT_6, FORMAT_7, obj.issueDatetime)
        holder.tvHanhDong.text = obj.action
        holder.tvNguoiThucHien.text = obj.createdBy
        holder.tvNoiDung.text = obj.content
        holder.tvGhiChu.text = obj.description

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class RequestViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvNgay: TextView = itemView.findViewById(R.id.tvNgay)
        var tvHanhDong: TextView = itemView.findViewById(R.id.tvHanhDong)
        var tvNguoiThucHien: TextView = itemView.findViewById(R.id.tvNguoiThucHien)
        var tvNoiDung: TextView = itemView.findViewById(R.id.tvNoiDung)
        var tvGhiChu: TextView = itemView.findViewById(R.id.tvGhiChu)
    }

}