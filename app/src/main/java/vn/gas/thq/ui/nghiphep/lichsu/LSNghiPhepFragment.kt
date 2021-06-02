package vn.gas.thq.ui.nghiphep.lichsu

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dang_ky_nghi.*
import kotlinx.android.synthetic.main.fragment_lich_su_nghi.*
import kotlinx.android.synthetic.main.fragment_lich_su_nghi.edtEndDate
import kotlinx.android.synthetic.main.fragment_lich_su_nghi.edtStaff
import kotlinx.android.synthetic.main.fragment_lich_su_nghi.edtStartDate
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.UserModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.nghiphep.VacationModel
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class LSNghiPhepFragment : BaseFragment() {
    lateinit var viewModel: LSNghiPhepViewModel
    private lateinit var adapter: VacationAdapter
    private var listVacation = mutableListOf<VacationModel>()
    private var listStaff = mutableListOf<UserModel>()
    private var fromDate: String = ""
    private var endDate: String = ""
    private var staffId: Int? = 0
    private var staffName: String = ""

    companion object {
        @JvmStatic
        fun newInstance() = LSNghiPhepFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_lich_su_nghi
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
                .get(LSNghiPhepViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initData() {
        initRecyclerView()
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
        btnSearch.setOnClickListener(this::onSearch)

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

        viewModel.callbackListVacation.observe(viewLifecycleOwner, {
            listVacation.clear()
            it.forEach { vacationModel ->
                vacationModel.staffName = staffName
            }
            listVacation.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.callbackListStaff.observe(viewLifecycleOwner, {
            listStaff.clear()
            listStaff.addAll(it)
        })
    }

    private fun onSearch(view: View) {
        fromDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtStartDate.text.toString()
            )
        endDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtEndDate.text.toString()
            )
        if (AppDateUtils.validateEndDateGreaterorEqualThanStartDate(fromDate, endDate)) {
            viewModel.getVacation(staffId!!, fromDate, endDate)
        }
    }

    private fun initRecyclerView() {
        adapter = VacationAdapter(listVacation)
//        adapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvResult.layoutManager = linearLayoutManager
        rvResult.adapter = adapter

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
//            if (AppConstants.NOT_SELECT == item.id) {
//                return@show
//            }
            staffId = item.id.toInt()
            staffName = item.other
            edtStaff.setText(item.name)
        }
    }
}