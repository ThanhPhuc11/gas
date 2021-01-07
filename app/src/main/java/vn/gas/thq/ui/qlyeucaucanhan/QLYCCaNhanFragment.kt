package vn.gas.thq.ui.qlyeucaucanhan

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_init_export_request.*
import kotlinx.android.synthetic.main.fragment_init_export_request.rvRequestItem
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.*
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.edtEndDate
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.edtStartDate
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.AppDateUtils.FORMAT_2
import vn.gas.thq.util.AppDateUtils.FORMAT_5
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.GetListDataDemo
import vn.hongha.ga.R
import java.util.*

class QLYCCaNhanFragment : BaseFragment() {
    private lateinit var viewModel: QLYCCaNhanViewModel
    private lateinit var adapter: RequestItemAdapter
    var mList = mutableListOf<BussinesRequestModel>()
    private var status: String? = null

    companion object {
        @JvmStatic
        fun newInstance(): QLYCCaNhanFragment {
            val args = Bundle()

            val fragment = QLYCCaNhanFragment()
            fragment.arguments = args
            return fragment
        }
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
                .get(QLYCCaNhanViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Quản lý yêu cầu"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_qlyc_ca_nhan
    }

    override fun initObserver() {
        viewModel.mLiveData.observe(this, {
            mList.clear()
            mList.addAll(it)
//            adapter = RequestItemAdapter(it)
//            rvRequestItem.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }

    override fun initData() {

//        mList.add(BussinesRequestModel("", "NEW", "2021-01-01", "", 1))
        adapter = RequestItemAdapter(mList)
//        productAdapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestItem.layoutManager = linearLayoutManager
        rvRequestItem.adapter = adapter

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
        edtStatus.setOnClickListener(this::onChooseStatus)
        btnSearch.setOnClickListener(this::onSubmitData)
    }

    private fun onChooseStatus(view: View) {
        var doc = DialogList()
        var mArrayList = GetListDataDemo.getListStatus(Objects.requireNonNull(context))
        doc.show(
            activity, mArrayList,
            getString(R.string.status),
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
//                edtStatus.setText("")
//                genderType = ""
                return@show
            }
            status = item.id
            edtStatus.setText(item.name)
        }
    }

    private fun onSubmitData(view: View) {
        var fromDate =
            AppDateUtils.changeDateFormat(FORMAT_2, FORMAT_5, edtStartDate.text.toString())
        var endDate =
            AppDateUtils.changeDateFormat(FORMAT_2, FORMAT_5, edtEndDate.text.toString())
        viewModel.onSubmitData(status, fromDate, endDate)
    }
}