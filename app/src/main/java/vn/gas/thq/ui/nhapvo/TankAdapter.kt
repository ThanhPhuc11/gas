package vn.gas.thq.ui.nhapvo

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import vn.hongha.ga.R
import vn.hongha.ga.databinding.ItemTankBinding

class TankAdapter(private val listVacation: MutableList<VoModel>) :
    RecyclerView.Adapter<TankAdapter.TankViewHolder>() {

    var onItemXuatChange: ((Int, Int) -> Unit)? = null
    var onItemNhapChange: ((Int, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TankViewHolder {
        val itemBinding =
            ItemTankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TankViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TankViewHolder, position: Int) {
        val obj = listVacation[position]
        if (position % 2 == 0) {
            holder.itemBinding.llWrap.backgroundTintList =
                AppCompatResources.getColorStateList(holder.itemView.context, R.color.white_ffffff)
        } else {
            holder.itemBinding.llWrap.backgroundTintList =
                AppCompatResources.getColorStateList(holder.itemView.context, R.color.transparent)
        }
        holder.itemBinding.tvName.text = obj.name

        holder.itemBinding.edtSLXuat.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus && position != -1 && !TextUtils.isEmpty(holder.itemBinding.edtSLXuat.text)) {
                    onItemXuatChange?.invoke(
                        obj.productOfferingId!!,
                        holder.itemBinding.edtSLXuat.text.toString().toInt()
                    )
                }
            }
        }

        holder.itemBinding.edtSLNhap.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus && position != -1 && !TextUtils.isEmpty(holder.itemBinding.edtSLNhap.text)) {
                    onItemNhapChange?.invoke(
                        obj.productOfferingId!!,
                        holder.itemBinding.edtSLNhap.text.toString().toInt()
                    )
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return listVacation.size
    }

    inner class TankViewHolder(val itemBinding: ItemTankBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}