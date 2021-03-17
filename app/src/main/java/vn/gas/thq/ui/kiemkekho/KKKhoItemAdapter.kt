package vn.gas.thq.ui.kiemkekho

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import vn.gas.thq.model.ProductModel
import vn.hongha.ga.R

class KKKhoItemAdapter(private val mList: MutableList<ProductModel>, private val context: Context) :
    RecyclerView.Adapter<KKKhoItemAdapter.ProductViewHolder>() {
    private lateinit var mClickListener: ItemClickListener

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kiem_ke_kho, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val obj: ProductModel = mList[position]
        if (position % 2 != 0) {
            holder.llWrap.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_F6F6F6))
        }
        holder.tvName.text = obj.name
        holder.tvQuantity.text = obj.quantity?.toString()
        holder.edtCheckQuantity.text = ""
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
        var edtCheckQuantity: TextView = itemView.findViewById(R.id.edtCheckQuantity)
        var tvUnit: TextView = itemView.findViewById(R.id.tvUnit)

        init {
            edtCheckQuantity.addTextChangedListener(afterTextChanged = {
                mClickListener.onItemSLChanged(
                    adapterPosition,
                    if (TextUtils.isEmpty(it.toString())) null else it.toString().toInt()
                )
            })
        }

    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemSLChanged(position: Int, count: Int?)
    }
}