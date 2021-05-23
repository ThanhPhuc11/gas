package vn.gas.thq.ui.nghiphep.lichsu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.ui.nghiphep.VacationModel
import vn.gas.thq.util.AppDateUtils
import vn.hongha.ga.databinding.ItemHistoryVacationBinding

class VacationAdapter(private val listVacation: MutableList<VacationModel>) :
    RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val itemBinding =
            ItemHistoryVacationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacationViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        val obj = listVacation[position]
        "${
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_6,
                AppDateUtils.FORMAT_7, obj.fromDate
            )
        }\n${
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_6,
                AppDateUtils.FORMAT_7, obj.toDate
            )
        }".also { holder.itemBinding.tvDate.text = it }
        holder.itemBinding.tvStaffName.text = obj.staffName
        holder.itemBinding.tvReason.text = obj.reason
        holder.itemBinding.tvRegisterDate.text = AppDateUtils.changeDateFormat(
            AppDateUtils.FORMAT_6,
            AppDateUtils.FORMAT_7, obj.createdDate
        )
    }

    override fun getItemCount(): Int {
        return listVacation.size
    }

    inner class VacationViewHolder(val itemBinding: ItemHistoryVacationBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}