package vn.gas.thq.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.UserModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.LoginRetrofitBuilder
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.lapyeucauxuatkho.LapYCXuatKhoFragment
import vn.gas.thq.ui.login.LoginViewModel
import vn.gas.thq.ui.pheduyetgia.PheDuyetGiaFragment
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanFragment
import vn.gas.thq.ui.qlyeucauduyetgia.QuanLyYeuCauDuyetGiaFragment
import vn.gas.thq.ui.retail.RetailContainerFragment
import vn.gas.thq.ui.thukho.ThuKhoXuatKhoFragment
import vn.gas.thq.ui.xemkho.XemKhoFragment
import vn.gas.thq.util.ScreenId
import vn.hongha.ga.R

class HomeFragment : BaseFragment(), MenuAdapter.ItemClickListener {
    private lateinit var viewModel: HomeViewModel
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
        viewModel.userModelCallback.observe(viewLifecycleOwner, {
            showInfo(it)
        })

        viewModel.callbackStart.observe(viewLifecycleOwner, {
            showLoading()
        })

        viewModel.callbackSuccess.observe(viewLifecycleOwner, {
            hideLoading()
        })

        viewModel.callbackFail.observe(viewLifecycleOwner, {
            hideLoading()
        })

        viewModel.showMessCallback.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun initData() {
        viewModel.getUserInfo()
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {
        viewModel =
            ViewModelProviders.of(this,
                context?.let {
                    RetrofitBuilder.getInstance(it)?.create(ApiService::class.java)
                        ?.let { apiService ->
                            ViewModelFactory(apiService, context)
                        }
                })
                .get(HomeViewModel::class.java)
    }

    override fun initView() {
        initMenuData()
        val gridLayoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        rvMenu.layoutManager = gridLayoutManager
        rvMenu.adapter = menuAdapter
    }

    private fun showInfo(user: UserModel) {
        tvUserName.text = user.name
    }

    private fun initMenuData() {
        val mList = mutableListOf<MenuModel>()
        mList.add(MenuModel("Bán lẻ", R.drawable.ic_menu_1))
        mList.add(MenuModel("Phê duyệt yêu cầu giảm giá", R.drawable.ic_menu_2))
        mList.add(MenuModel("Lập yêu cầu xuất kho", R.drawable.ic_menu_3))
        mList.add(MenuModel("Quản lý yêu cầu xuất kho", R.drawable.ic_menu_4))
        mList.add(MenuModel("Quản lý yêu cầu cá nhân", R.drawable.ic_menu_4))
        mList.add(MenuModel("Xem kho", R.drawable.ic_menu_4))
        mList.add(MenuModel("Chức năng 7", R.drawable.ic_menu_4))
        mList.add(MenuModel("Xem thêm", R.drawable.ic_more_arrow))
        menuAdapter = MenuAdapter(mList)
        menuAdapter.setClickListener(this)
    }

    override fun onItemTopClick(view: View?, position: Int) {
        when (position) {
            0 -> viewController?.pushFragment(
                ScreenId.SCREEN_RETAIL_CONTAINER,
                RetailContainerFragment.newInstance(ScreenId.SCREEN_RETAIL_STEP_1, null)
            )
            1 -> viewController?.pushFragment(
                ScreenId.SCREEN_PHE_DUYET_GIA,
                PheDuyetGiaFragment.newInstance()
            )
            2 -> viewController?.pushFragment(
                ScreenId.SCREEN_LAP_YC_XUAT_KHO,
                LapYCXuatKhoFragment.newInstance()
            )
            3 -> viewController?.pushFragment(
                ScreenId.SCREEN_THU_KHO,
                ThuKhoXuatKhoFragment.newInstance()
            )
            4 -> viewController?.pushFragment(
                ScreenId.SCREEN_QLYC_CA_NHAN,
                QLYCCaNhanFragment.newInstance(ScreenId.HOME_SCREEN)
            )
            5 -> viewController?.pushFragment(
                ScreenId.SCREEN_XEM_KHO,
                XemKhoFragment.newInstance()
            )
        }
    }
}