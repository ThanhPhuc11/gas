package vn.gas.thq.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import vn.hongha.ga.R

class MenuAdapter(private val mList: MutableList<MenuModel>) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    lateinit var mClickListener: ItemClickListener

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = if (viewType == 7) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_option_menu_more, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_option_menu, parent, false)
        }

        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu: MenuModel = mList[position]
        holder.tvTitle.text = menu.resString
        holder.imgIcon.setImageResource(menu.resDrawable)
//        holder.imgIcon.setImageResource(R.drawable.ic_menu_2)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MenuViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mClickListener.onItemTopClick(p0, adapterPosition)
        }
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemTopClick(view: View?, position: Int)
    }
}