package vn.gas.thq.ui.homemenu

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_menu_full.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.ui.home.MenuModel
import vn.gas.thq.ui.kehoachbh.LapKeHoachBHFragment
import vn.gas.thq.ui.kiemkekho.KiemKeKhoFragment
import vn.gas.thq.ui.lapyeucauxuatkho.LapYCXuatKhoFragment
import vn.gas.thq.ui.login.LoginFragment
import vn.gas.thq.ui.nghiphep.NghiPhepFragment
import vn.gas.thq.ui.nhapkho.NhapKhoFragment
import vn.gas.thq.ui.nhapkhonguon.NhapKhoNguonFragment
import vn.gas.thq.ui.nhapvo.NhapVoFragment
import vn.gas.thq.ui.pheduyetgiaTDL.PheDuyetGiaTDLFragment
import vn.gas.thq.ui.pheduyetgiabanle.PheDuyetGiaBanLeFragment
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanFragment
import vn.gas.thq.ui.qlyeucauduyetkehoach.QLYCKeHoachFragment
import vn.gas.thq.ui.retail.RetailContainerFragment
import vn.gas.thq.ui.retailtongdaily.RetailContainerBossFragment
import vn.gas.thq.ui.sangchiet.SangChietFragment
import vn.gas.thq.ui.thukho.ThuKhoXuatKhoFragment
import vn.gas.thq.ui.vitri.ViTriKHFragment
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
        mList.add(MenuModel(2, "Phê duyệt yêu cầu bán hàng", R.drawable.ic_menu_2))
        mList.add(MenuModel(13, "Bán hàng\nTổng đại lý", R.drawable.ic_menu_1))
        mList.add(MenuModel(16, "Phê duyệt yêu cầu bán hàng TĐL", R.drawable.ic_menu_2))
        mList.add(MenuModel(3, "Lập yêu cầu xuất kho", R.drawable.ic_menu_3))
        mList.add(MenuModel(4, "Quản lý yêu cầu xuất kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(5, "Quản lý yêu cầu cá nhân", R.drawable.ic_menu_4))
        mList.add(MenuModel(6, "Xem kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(7, "Kiểm kê kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(8, "Nhập kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(9, "Lập kế hoạch\nbán hàng", R.drawable.ic_menu_4))
        mList.add(MenuModel(10, "Quản lý\n kế hoạch\n bán hàng", R.drawable.ic_menu_4))
        mList.add(MenuModel(11, "Cập nhật\nvị trí", R.drawable.ic_menu_4))
        mList.add(MenuModel(12, "Sang chiết", R.drawable.ic_menu_4))
        mList.add(MenuModel(14, "Đăng ký nghỉ", R.drawable.ic_menu_4))
        mList.add(MenuModel(15, "Xuất nhập vỏ", R.drawable.ic_menu_4))
        mList.add(MenuModel(17, "Xuất gas nguồn", R.drawable.ic_menu_4))
        mList.add(MenuModel(100, "Đăng xuất", R.drawable.ic_menu_4))
        menuAdapter = MenuFullAdapter(mList)
        menuAdapter.setClickListener(this)
    }

    override fun onItemTopClick(view: View?, id: Int) {
        when (id) {
            1 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "BAN_HANG_LE_LAP_YEU_CAU" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_RETAIL_CONTAINER,
                        RetailContainerFragment.newInstance(ScreenId.SCREEN_RETAIL_STEP_1, null)
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            2 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "BAN_HANG_XEM_YEU_CAU" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_PHE_DUYET_GIA,
                        PheDuyetGiaBanLeFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            3 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "KHO_DAT_YEU_CAU_XUAT_KHO" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_LAP_YC_XUAT_KHO,
                        LapYCXuatKhoFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            4 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "KHO_TU_CHOI_YEU_CAU_XUAT_KHO" || it == "KHO_DUYET_YEU_CAU_XUAT_KHO" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_THU_KHO,
                        ThuKhoXuatKhoFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            5 -> viewController?.pushFragment(
                ScreenId.SCREEN_QLYC_CA_NHAN,
                QLYCCaNhanFragment.newInstance(ScreenId.HOME_SCREEN)
            )
            6 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "KHO_XEM_KHO" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_XEM_KHO,
                        XemKhoFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            7 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "KHO_KIEM_KE" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_KIEM_KE_KHO,
                        KiemKeKhoFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            8 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "KHO_NHAP_KHO_LXBH" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_NHAP_KHO,
                        NhapKhoFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            9 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "QLKHBH_TAO_KHBH" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_LAP_KE_HOACH,
                        LapKeHoachBHFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            10 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "QLKHBH_VIEW_KHBH" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_QLYC_KE_HOACH,
                        QLYCKeHoachFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            11 -> viewController?.pushFragment(
                ScreenId.SCREEN_CAP_NHAT_VI_TRI,
                ViTriKHFragment.newInstance()
            )
            12 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "KHO_NHAP_THANH_PHAM_SANG_CHIET" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_SANG_CHIET,
                        SangChietFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            13 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "BAN_HANG_DL_LAP_YEU_CAU" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_RETAIL_BOSS_CONTAINER,
                        RetailContainerBossFragment.newInstance(ScreenId.SCREEN_RETAIL_STEP_1, null)
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            14 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "BAN_HANG_DIEM_DANH" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_DANG_KY_NGHI,
                        NghiPhepFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            15 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "KHO_XUAT_NHAP_VO" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_XUAT_NHAP_VO,
                        NhapVoFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            16 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "BAN_HANG_XEM_YEU_CAU" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_PHE_DUYET_GIA,
                        PheDuyetGiaTDLFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            17 -> {
                if (AppPreferencesHelper(context).permission.firstOrNull { it == "KHO_NHAP_GAS_NGUON" } != null)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_NHAP_GAS_NGUON,
                        NhapKhoNguonFragment.newInstance()
                    )
                else showMess("Nhân viên không có quyền truy cập")
            }
            100 -> {
                viewController?.popAllFragment()
                viewController?.pushFragment(ScreenId.SCREEN_LOGIN, LoginFragment.newInstance())
            }
        }
    }
}