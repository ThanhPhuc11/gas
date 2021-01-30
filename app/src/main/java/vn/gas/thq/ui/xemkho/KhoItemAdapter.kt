package vn.gas.thq.ui.xemkho

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.model.ProductModel
import vn.hongha.ga.R

class KhoItemAdapter(private val mList: MutableList<ProductModel>, private val context: Context) :
    RecyclerView.Adapter<KhoItemAdapter.ProductViewHolder>() {
    lateinit var mClickListener: ItemClickListener

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kho, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val obj: ProductModel = mList[position]
        if (position % 2 != 0) {
            holder.llWrap.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_F6F6F6))
        }
        holder.tvName.text = obj.name
        holder.tvQuantity.text = obj.quantity?.toString()
        holder.tvUnit.text = obj.unit ?: "- -"
//        holder.imgIcon.setImageResource(menu.resDrawable)
//        holder.imgIcon.setImageResource(R.drawable.ic_menu_2)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

//    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
//    }

    inner class ProductViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var llWrap: LinearLayout = itemView.findViewById(R.id.llWrap)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        var tvUnit: TextView = itemView.findViewById(R.id.tvUnit)

    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
    }
}