package vn.gas.thq.ui.nghiphep.dknghi

import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_dang_ky_nghi.*
import kotlinx.android.synthetic.main.fragment_dang_ky_nghi.edtEndDate
import kotlinx.android.synthetic.main.fragment_dang_ky_nghi.edtStaff
import kotlinx.android.synthetic.main.fragment_dang_ky_nghi.edtStartDate
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.UserModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.ArrayList

class DKNghiPhepFragment : BaseFragment() {
    lateinit var viewModel: DKNghiPhepViewModel
    private var listStaff = mutableListOf<UserModel>()
    private var staffId: Int? = 0
    private var staffName: String = ""

    companion object {
        @JvmStatic
        fun newInstance() = DKNghiPhepFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_dang_ky_nghi
    }

    override fun setViewController() {

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
                .get(DKNghiPhepViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initData() {
        edtStartDate.setText(AppDateUtils.getCurrentDate())
        edtEndDate.setText(AppDateUtils.getCurrentDate())
        edtStartDate.setOnClickListener {
            CommonUtils.showCalendarDialog(
                context,
                edtStartDate.text.toString()
            ) { strDate -> edtStartDate.setText(strDate) }
        }

        edtEndDate.setOnClickListener {
            CommonUtils.showCalendarDialog(
                context,
                edtEndDate.text.toString()
            ) { strDate -> edtEndDate.setText(strDate) }
        }

        // Set gia tri mac dinh user dang nhap
        val userModel = AppPreferencesHelper(context).userModel
        staffId = userModel.staffId
        staffName = userModel.name.toString()
        "${userModel.staffCode} - $staffName".also { edtStaff.setText(it) }

        if (AppPreferencesHelper(context).permission.firstOrNull { it == "BAN_HANG_DIEM_DANH_HO" } != null) {
            AppPreferencesHelper(context).userModel.shopId?.let { viewModel.getStaffFromShopId(it) }
            edtStaff.isClickable = true
            edtStaff.isEnabled = true
        } else {
//            showMess("Nhân viên không có quyền truy cập")
        }

        edtStaff.setOnClickListener(this::onChoose)

        btnRegister.setOnClickListener(this::registerVacation)
    }

    override fun initObserver() {
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

        viewModel.callbackRegisterSuccess.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(
                activity,
                "Thông báo",
                "Bạn đã đăng ký nghỉ phép thành công"
            ) {
                edtLyDo.text?.clear()
            }
        })

        viewModel.callbackListStaff.observe(viewLifecycleOwner, {
            listStaff.clear()
            listStaff.addAll(it)
        })
    }

    private fun registerVacation(view: View) {
        val fromDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtStartDate.text.toString()
            )
        val endDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtEndDate.text.toString()
            )

        if (AppDateUtils.validateEndDateGreaterorEqualThanStartDate(fromDate, endDate)) {
            viewModel.registerVacation(staffId!!, fromDate, endDate, edtLyDo.text.toString())
        } else {
            showMess("Từ ngày không được lớn hơn Đến ngày")
        }

    }

    private fun onChoose(view: View) {
        val doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        listStaff.forEach {
            mArrayList.add(
                DialogListModel(
                    it.staffId.toString(),
                    "${it.staffCode} - ${it.name}",
                    it.name
                )
            )
        }
        doc.show(
            activity, mArrayList,
            getString(R.string.lxbh),
            getString(R.string.enter_text_search)
        ) { item ->
            staffId = item.id.toInt()
            staffName = item.other
            edtStaff.setText(item.name)
        }
    }
}