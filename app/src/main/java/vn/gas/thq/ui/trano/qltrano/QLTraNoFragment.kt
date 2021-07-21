package vn.gas.thq.ui.trano.qltrano

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_ql_tra_no.*
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
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

class QLTraNoFragment : BaseFragment() {
    private lateinit var viewModel: QLTraNoViewModel
    private lateinit var adapter: TraNoAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var listTraNo = mutableListOf<HistoryTraNoModel>()
    private var mListCustomer = mutableListOf<Customer>()

    private var custID: Int? = null
    private var typeRequestCongNo: String? = null

    companion object {
        @JvmStatic
        fun newInstance() = QLTraNoFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ql_tra_no
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
                .get(QLTraNoViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initData() {
        val user = AppPreferencesHelper(context).userModel
        val queryKH = "shopId==${user.shopId};status==1"
        viewModel.onGetListCustomer(queryKH, 0)
        initRecyclerView()
        edtKH.setOnClickListener(this::onChooseCustomer)
        edtLoaiCongNo.setOnClickListener(this::onChooseLoaiCongNo)

        edtStartDate.setText(AppDateUtils.getYesterdayDate())
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
        btnSearch.setOnClickListener(this::searchTraNo)
    }

    override fun initObserver() {
        viewModel.callbackListKH.observe(viewLifecycleOwner, {
            mListCustomer.clear()
            mListCustomer.addAll(it)
        })

        viewModel.callbackListHistory.observe(viewLifecycleOwner, {
            listTraNo.clear()
            listTraNo.addAll(it)
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

    private fun searchTraNo(view: View) {
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

        if (edtKH.text.isEmpty()) {
            showMess("Bạn chưa chọn khách hàng")
            return
        }

        if (edtLoaiCongNo.text.isEmpty()) {
            showMess("Bạn chưa chọn Loại công nợ")
            return
        }

        if (custID != null && typeRequestCongNo != null) {
            viewModel.historyTraNo(custID!!, typeRequestCongNo!!, fromDate, endDate)
        }
    }

    private fun initRecyclerView() {
        adapter = TraNoAdapter(listTraNo)
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

    private fun onChooseLoaiCongNo(view: View) {
        val doc = DialogList()
        val mArrayList = GetListDataDemo.getListLoaiCongNo()
        doc.show(
            activity, mArrayList,
            "Loại công nợ",
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
                return@show
            }
            typeRequestCongNo = mapTypeLoaiCongNo(item.id)
            edtLoaiCongNo.setText(item.name)
        }
    }

    private fun mapTypeLoaiCongNo(type: String): String {
        return when (type) {
            "1" -> "TANK12"
            "2" -> "TANK45"
            "3" -> "MONEY_DEBIT"
            "4" -> "ORDER_DEBIT"
            "5" -> "AGENCY_TANK12"
            "6" -> "AGENCY_TANK45"
            else -> ""
        }
    }
}