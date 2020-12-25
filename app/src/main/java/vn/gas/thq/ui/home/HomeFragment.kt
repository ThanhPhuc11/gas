package vn.gas.thq.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.pheduyetgia.PheDuyetGiaFragment
import vn.gas.thq.ui.qlyeucauduyetgia.QuanLyYeuCauDuyetGiaFragment
import vn.gas.thq.ui.retail.RetailContainerFragment
import vn.gas.thq.ui.retail.RetailFragment
import vn.gas.thq.ui.thukho.ThuKhoFragment
import vn.gas.thq.util.ScreenId
import vn.hongha.ga.R

class HomeFragment : BaseFragment(), MenuAdapter.ItemClickListener {
    private lateinit var menuAdapter: MenuAdapter

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initObserver() {

    }

    override fun initData() {
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {

    }

    override fun initView() {
        initMenuData()
        val gridLayoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        rvMenu.layoutManager = gridLayoutManager
        rvMenu.adapter = menuAdapter
    }

    private fun initMenuData() {
        val mList = mutableListOf<MenuModel>()
        mList.add(MenuModel("Bán lẻ", R.drawable.ic_menu_1))
        mList.add(MenuModel("Phê duyệt yêu cầu giảm giá", R.drawable.ic_menu_2))
        mList.add(MenuModel("Lập yêu cầu xuất kho", R.drawable.ic_menu_3))
        mList.add(MenuModel("Quản lý yêu cầu xuất kho", R.drawable.ic_menu_4))
        mList.add(MenuModel("Quản lý yêu cầu giảm giá", R.drawable.ic_menu_4))
        mList.add(MenuModel("Chức năng 6", R.drawable.ic_menu_4))
        mList.add(MenuModel("Chức năng 7", R.drawable.ic_menu_4))
        mList.add(MenuModel("Xem thêm", R.drawable.ic_more_arrow))
        menuAdapter = MenuAdapter(mList)
        menuAdapter.setClickListener(this)
    }

    override fun onItemTopClick(view: View?, position: Int) {
        when (position) {
            0 -> viewController?.pushFragment(ScreenId.SCREEN_RETAIL_CONTAINER, RetailContainerFragment.newInstance())
            1 -> viewController?.pushFragment(
                ScreenId.SCREEN_PHE_DUYET_GIA,
                PheDuyetGiaFragment.newInstance()
            )
            3 -> viewController?.pushFragment(
                ScreenId.SCREEN_THU_KHO,
                ThuKhoFragment.newInstance()
            )
            4 -> viewController?.pushFragment(
                ScreenId.SCREEN_PHE_DUYET_GIA,
                QuanLyYeuCauDuyetGiaFragment.newInstance()
            )
        }
    }
}