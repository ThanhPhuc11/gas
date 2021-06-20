package vn.gas.thq.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_home.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.RequestDeviceModel
import vn.gas.thq.model.UserModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.FcmRetrofitBuilder
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.homemenu.MenuFullFragment
import vn.gas.thq.ui.kiemkekho.KiemKeKhoFragment
import vn.gas.thq.ui.lapyeucauxuatkho.LapYCXuatKhoFragment
import vn.gas.thq.ui.login.LoginFragment
import vn.gas.thq.ui.main.IntentShareViewModel
import vn.gas.thq.ui.pheduyetgiabanle.PheDuyetGiaBanLeFragment
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanFragment
import vn.gas.thq.ui.qlyeucauduyetkehoach.QLYCKeHoachFragment
import vn.gas.thq.ui.retail.RetailContainerFragment
import vn.gas.thq.ui.thukho.ThuKhoXuatKhoFragment
import vn.gas.thq.ui.xemkho.XemKhoFragment
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.ScreenId
import vn.hongha.ga.R


class HomeFragment : BaseFragment(), MenuAdapter.ItemClickListener {
    private lateinit var viewModel: HomeViewModel
    private lateinit var fcmViewModel: FcmHomeViewModel
    private lateinit var intentShareViewModel: IntentShareViewModel
    private lateinit var menuAdapter: MenuAdapter
    private var user: UserModel? = null

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

        fcmViewModel =
            ViewModelProviders.of(this,
                context?.let {
                    FcmRetrofitBuilder.getInstance(it)?.create(ApiService::class.java)
                        ?.let { apiService ->
                            ViewModelFactory(apiService, context)
                        }
                })
                .get(FcmHomeViewModel::class.java)

        intentShareViewModel =
            ViewModelProviders.of(requireActivity()).get(IntentShareViewModel::class.java)
    }

    override fun initView() {
        initMenuData()
        val gridLayoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        rvMenu.layoutManager = gridLayoutManager
        rvMenu.adapter = menuAdapter
    }

    override fun initData() {
        viewModel.getUserInfo()
        viewModel.getUserPermission()
        //handle push screen, then set null now
//        showMess(intentShareViewModel.callbackFirebaseType.value)
        handlePutScreenWhenFireIntent(intentShareViewModel.callbackFirebaseType.value)
        imgLogout.setOnClickListener {
            CommonUtils.showConfirmDiglog2Button(
                activity, "Đăng xuất", "Bạn có chắc chắn muốn đăng xuất?", getString(
                    R.string.biometric_negative_button_text
                ), getString(R.string.text_ok)
            ) {
                if (it == AppConstants.YES) {
                    viewController?.popAllFragment()
                    viewController?.pushFragment(ScreenId.SCREEN_LOGIN, LoginFragment.newInstance())
                }
            }
        }

    }

    override fun initObserver() {
        viewModel.userModelCallback.observe(viewLifecycleOwner, {
            showInfo(it)
            registerFCM()
        })

        intentShareViewModel.callbackFirebaseType.observe(viewLifecycleOwner, {
            handlePutScreenWhenFireIntent(it)
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

    private fun showInfo(user: UserModel) {
        this.user = user
        tvUserName.text = user.name
    }

    private fun registerFCM() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            Log.d("TOKEN", token ?: "null")
//            showMess(token)

            val requestDeviceModel = RequestDeviceModel().apply {
                username = user?.staffCode ?: ""
                deviceToken = token
                oldDeviceToken = ""
                deviceModel = Build.BRAND + " " + Build.MODEL
                platform = "ANDROID"
                osVersion = Build.VERSION.SDK_INT.toString()
            }
            fcmViewModel.registerFcm(requestDeviceModel)
        })
    }

    private fun handlePutScreenWhenFireIntent(type: String?) {
        type?.let {
            when (type) {
                "APPROVE_SHOP_ORDER" -> {
                    if (AppPreferencesHelper(context).permission.firstOrNull { it == "KHO_TU_CHOI_YEU_CAU_XUAT_KHO" || it == "KHO_DUYET_YEU_CAU_XUAT_KHO" } != null)
                        viewController?.pushFragment(
                            ScreenId.SCREEN_THU_KHO,
                            ThuKhoXuatKhoFragment.newInstance()
                        )
                    else showMess("Nhân viên không có quyền truy cập")
                }
                "SHOP_ORDER_APPROVED", "SHOP_ORDER_REJECTED", "SALE_ORDER_APPROVED", "SALE_ORDER_REJECTED" -> {
                    // man ca nhan
                    viewController?.pushFragment(
                        ScreenId.SCREEN_QLYC_CA_NHAN,
                        QLYCCaNhanFragment.newInstance(ScreenId.HOME_SCREEN)
                    )
                }
                "APPROVE_SALE_ORDER" -> {
                    // phe duyet gia
                    if (AppPreferencesHelper(context).permission.firstOrNull { it == "BAN_HANG_XEM_YEU_CAU" } != null)
                        viewController?.pushFragment(
                            ScreenId.SCREEN_PHE_DUYET_GIA,
                            PheDuyetGiaBanLeFragment.newInstance()
                        )
                    else showMess("Nhân viên không có quyền truy cập")
                }
                "APPROVE_SALE_PLAN", "SALE_PLAN_APPROVED", "SALE_PLAN_REJECTED" -> {
                    // tim ke hoach
                    if (AppPreferencesHelper(context).permission.firstOrNull { it == "QLKHBH_VIEW_KHBH" } != null)
                        viewController?.pushFragment(
                            ScreenId.SCREEN_QLYC_KE_HOACH,
                            QLYCKeHoachFragment.newInstance()
                        )
                    else showMess("Nhân viên không có quyền truy cập")
                }
                else -> {

                }
            }
            intentShareViewModel.callbackFirebaseType.value = null
        }

    }

    private fun initMenuData() {
        val mList = mutableListOf<MenuModel>()
        mList.add(MenuModel(1, "Bán lẻ", R.drawable.ic_menu_1))
        mList.add(MenuModel(2, "Phê duyệt yêu cầu bán hàng", R.drawable.ic_menu_2))
        mList.add(MenuModel(3, "Lập yêu cầu xuất kho", R.drawable.ic_menu_3))
        mList.add(MenuModel(4, "Quản lý yêu cầu xuất kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(5, "Quản lý yêu cầu cá nhân", R.drawable.ic_menu_4))
        mList.add(MenuModel(6, "Xem kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(7, "Kiểm kê kho", R.drawable.ic_menu_4))
        mList.add(MenuModel(8, "Xem thêm", R.drawable.ic_more_arrow))
        menuAdapter = MenuAdapter(mList)
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
            8 -> viewController?.pushFragment(
                ScreenId.MENU_FULL_SCREEN,
                MenuFullFragment.newInstance()
            )
        }
    }
}