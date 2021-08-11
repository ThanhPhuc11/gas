package vn.gas.thq.ui.xuatkhoKH.qlxuatkhokh

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_ql_xuat_kho_kh.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.gas.thq.util.dialog.GetListDataDemo
import vn.hongha.ga.R
import java.util.*

class QLXuatKhoKHFragment : BaseFragment() {
    private lateinit var viewModel: QLXuatKhoKHViewModel
    private lateinit var adapter: ListXuatKhoKHAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var listOrder = mutableListOf<BussinesRequestModel>()
    private var mListCustomer = mutableListOf<Customer>()

    private var custID: Int? = null
    private var completeOrder: Int? = null

    companion object {
        @JvmStatic
        fun newInstance() = QLXuatKhoKHFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ql_xuat_kho_kh
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
                .get(QLXuatKhoKHViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Xuất kho cho khách hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        val user = AppPreferencesHelper(context).userModel
        val queryKH = "shopId==${user.shopId};status==1"
        viewModel.onGetListCustomer(queryKH, 0)
        initRecyclerView()
        edtKH.setOnClickListener(this::onChooseCustomer)
        edtTrangThaiXuatHang.setOnClickListener(this::onChooseTrangThaiXuatHang)

        edtStartDate.setText(AppDateUtils.getInitMonthDate())
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
        btnSearch.setOnClickListener(this::search)
    }

    override fun initObserver() {
        viewModel.callbackListKH.observe(viewLifecycleOwner, {
            mListCustomer.clear()
            mListCustomer.addAll(it)
        })

        viewModel.mListDataSearch.observe(viewLifecycleOwner, {
            listOrder.clear()
            listOrder.addAll(it)
            adapter.notifyDataSetChanged()
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

    private fun search(view: View) {
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

//        if (edtKH.text.isEmpty()) {
//            showMess("Bạn chưa chọn khách hàng")
//            return
//        }

//        if (edtLoaiCongNo.text.isEmpty()) {
//            showMess("Bạn chưa chọn Loại công nợ")
//            return
//        }

        if (AppDateUtils.validateEndDateGreaterorEqualThanStartDate(fromDate, endDate)) {
            viewModel.onSearch(custID, completeOrder, fromDate, endDate, 0)
        } else {
            showMess("Từ ngày không được lớn hơn Đến ngày")
        }

    }

    private fun initRecyclerView() {
        adapter = ListXuatKhoKHAdapter(listOrder)
//        adapter.setClickListener(this)

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvResult.layoutManager = linearLayoutManager
        rvResult.adapter = adapter
    }

    private fun onChooseCustomer(view: View) {
        val doc = DialogList()
        val mArrayList = ArrayList<DialogListModel>()
        mListCustomer.forEach {
            mArrayList.add(DialogListModel(it.customerId ?: "", it.name ?: ""))
        }
        doc.show(
            activity, mArrayList,
            getString(R.string.customer),
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
                return@show
            }
//            resetAll()
            custID = item.id.toInt()
            edtKH.setText(item.name)
        }
    }

    private fun onChooseTrangThaiXuatHang(view: View) {
        val doc = DialogList()
        val mArrayList = GetListDataDemo.getListTrangThaiXuatHang()
        doc.show(
            activity, mArrayList,
            "Trạng thái xuất hàng",
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
                return@show
            }
            completeOrder = item.id.toInt()
            edtTrangThaiXuatHang.setText(item.name)
        }
    }
}