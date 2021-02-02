package vn.gas.thq.ui.homemenu

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_menu_full.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.home.MenuModel
import vn.gas.thq.ui.kiemkekho.KiemKeKhoFragment
import vn.gas.thq.ui.lapyeucauxuatkho.LapYCXuatKhoFragment
import vn.gas.thq.ui.nhapkho.NhapKhoFragment
import vn.gas.thq.ui.pheduyetgia.PheDuyetGiaFragment
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanFragment
import vn.gas.thq.ui.retail.RetailContainerFragment
import vn.gas.thq.ui.thukho.ThuKhoXuatKhoFragment
import vn.gas.thq.ui.xemkho.XemKhoFragment
import vn.gas.thq.util.ScreenId
import vn.hongha.ga.R

class MenuFullFragment : BaseFragment(), MenuFullAdapter.ItemClickListener {
    //    private lateinit var viewModel: HomeViewModel
    private lateinit var menuAdapter: MenuFullAdapter

    companion object {
        @JvmStatic
        fun newInstance() =
            MenuFullFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_menu_full
    }

    override fun initObserver() {
//        viewModel.userModelCallback.observe(viewLifecycleOwner, {
//            showInfo(it)
//        })
//
//        viewModel.callbackStart.observe(viewLifecycleOwner, {
//            showLoading()
//        })
//
//        viewModel.callbackSuccess.observe(viewLifecycleOwner, {
//            hideLoading()
//        })
//
//        viewModel.callbackFail.observe(viewLifecycleOwner, {
//            hideLoading()
//        })
//
//        viewModel.showMessCallback.observe(viewLifecycleOwner, {
//            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//        })
    }

    override fun initData() {
        initMenuData()
        val gridLayoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        rvMenu.layoutManager = gridLayoutManager
        rvMenu.adapter = menuAdapter
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {
//        viewModel =
//            ViewModelProviders.of(this,
//                context?.let {
//                    RetrofitBuilder.getInstance(it)?.create(ApiService::class.java)
//                        ?.let { apiService ->
//                            ViewModelFactory(apiService, context)
//                        }
//                })
//                .get(HomeViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Tổng hợp"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    private fun initMenuData() {
        val mList = mutableListOf<MenuModel>()
        mList.add(MenuModel(1, "Bán lẻ", R.drawable.ic_menu_1))
        mList.add(MenuModel(2, "Phê duyệt yêu cầu giảm giá", R.drawable.ic_menu_2))
        mList.add(MenuModel(3, "Lập yêu cầu xuất kho", R.drawable.ic_menu_3))
        mList.add(MenuModel(4, "Quản lý yêu cầu xuất kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(5, "Quản lý yêu cầu cá nhân", R.drawable.ic_menu_4))
        mList.add(MenuModel(6, "Xem kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(7, "Kiểm kê kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(8, "Nhập kho", R.drawable.ic_menu_4))
        menuAdapter = MenuFullAdapter(mList)
        menuAdapter.setClickListener(this)
    }

    override fun onItemTopClick(view: View?, id: Int) {
        when (id) {
            1 -> viewController?.pushFragment(
                ScreenId.SCREEN_RETAIL_CONTAINER,
                RetailContainerFragment.newInstance(ScreenId.SCREEN_RETAIL_STEP_1, null)
            )
            2 -> viewController?.pushFragment(
                ScreenId.SCREEN_PHE_DUYET_GIA,
                PheDuyetGiaFragment.newInstance()
            )
            3 -> viewController?.pushFragment(
                ScreenId.SCREEN_LAP_YC_XUAT_KHO,
                LapYCXuatKhoFragment.newInstance()
            )
            4 -> viewController?.pushFragment(
                ScreenId.SCREEN_THU_KHO,
                ThuKhoXuatKhoFragment.newInstance()
            )
            5 -> viewController?.pushFragment(
                ScreenId.SCREEN_QLYC_CA_NHAN,
                QLYCCaNhanFragment.newInstance(ScreenId.HOME_SCREEN)
            )
            6 -> viewController?.pushFragment(
                ScreenId.SCREEN_XEM_KHO,
                XemKhoFragment.newInstance()
            )
            7 -> viewController?.pushFragment(
                ScreenId.SCREEN_KIEM_KE_KHO,
                KiemKeKhoFragment.newInstance()
            )
            8 -> viewController?.pushFragment(
                ScreenId.SCREEN_NHAP_KHO,
                NhapKhoFragment.newInstance()
            )
        }
    }
}